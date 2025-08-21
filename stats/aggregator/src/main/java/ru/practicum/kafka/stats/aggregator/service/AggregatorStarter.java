package ru.practicum.kafka.stats.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.stats.aggregator.config.TopicType;
import ru.practicum.kafka.stats.aggregator.config.WeightType;
import ru.practicum.kafka.stats.aggregator.service.client.Client;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {
    private static final int AMOUNT_PART_COMMIT = 10;

    private final Client client;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final Map<Long, Map<Long, Double>> weights = new HashMap<>();
    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();
    private final Map<Long, Double> eventWeightSums = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(client.getConsumer()::wakeup));

        try {
            client.getConsumer().subscribe(List.of(client.getConsumerTopics().get(TopicType.USER_ACTIONS)));
            while (true) {
                try {
                    ConsumerRecords<String, SpecificRecordBase> records = client.getConsumer().poll(client.getConsumerPollTimeout());
                    int count = 0;
                    for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                        log.info("{}", record);
                        UserActionAvro userAction = (UserActionAvro) record.value();
                        calcScore(userAction);
                        manageOffsets(record, count, client.getConsumer());
                    }
                    client.getConsumer().commitAsync();
                } catch (WakeupException e) {
                    throw new WakeupException();
                } catch (Exception e) {
                    log.error("Ошибка ", e);
                }
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                client.getConsumer().commitSync(currentOffsets);
            } finally {
                log.info("Закрываем продюсер и консьюмер");
                client.stop();
            }
        }
    }

    private void manageOffsets(
            ConsumerRecord<String, SpecificRecordBase> record,
            int count,
            Consumer<String, SpecificRecordBase> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % AMOUNT_PART_COMMIT == 0) {
            consumer.commitAsync(currentOffsets, (offsets, e) -> {
                if (e != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, e);
                }
            });
        }
    }

    private void calcScore(UserActionAvro userAction) {
        Double oldWeightEventA = weights
                .computeIfAbsent(userAction.getEventId(), e -> new HashMap<>())
                .getOrDefault(userAction.getUserId(), 0.0);
        Double newWeightEventA = Math.max(
                client.getActionWeights()
                        .getOrDefault(WeightType.valueOf(userAction.getActionType().name()), 0.0),
                oldWeightEventA);
        if (oldWeightEventA.equals(newWeightEventA)) {
            return;
        }
        Double oldWeightSumEventA = eventWeightSums.getOrDefault(userAction.getEventId(), 0.0);
        Double newWeightSumEventA = oldWeightSumEventA + (newWeightEventA - oldWeightEventA);
        weights.get(userAction.getEventId()).put(userAction.getUserId(), newWeightEventA);
        eventWeightSums.put(userAction.getEventId(), newWeightSumEventA);
        for (Long eventId : weights.keySet()) {
            if (eventId.equals(userAction.getEventId())) { //Не сравнивать событие A само с собой
                continue;
            }
            Double oldWeightEventB = weights.get(eventId).getOrDefault(userAction.getUserId(), 0.0);
            if (oldWeightEventB.equals(0.0)) {  //Если пользователь не взаимодействовал с событием B расчет не производим
                continue;
            }
            Double oldWeightSumEventB = eventWeightSums.getOrDefault(eventId, 0.0);
            Double oldMin = Math.min(oldWeightEventA, oldWeightEventB);
            Double newMin = Math.min(newWeightEventA, oldWeightEventB);
            Double oldMinWeightsSums = getMinWeightsSums(userAction.getEventId(), eventId);
            Double newMinWeightsSums = oldMinWeightsSums + (newMin - oldMin);
            if (!oldMin.equals(newMin)) {
                putMinWeightsSums(userAction.getEventId(), eventId, newMinWeightsSums);
            }
            Double score = newMinWeightsSums / (Math.sqrt(newWeightSumEventA) * Math.sqrt(oldWeightSumEventB));

            Long eventA = Math.min(userAction.getEventId(), eventId);
            Long eventB = Math.max(userAction.getEventId(), eventId);
            sendEventSimilarity(eventA, eventB, score, userAction.getTimestamp());
        }
    }

    private void sendEventSimilarity(Long eventA, Long eventB, Double score, Instant actionDate) {
        EventSimilarityAvro eventSimilarity = EventSimilarityAvro.newBuilder()
                .setEventA(eventA)
                .setEventB(eventB)
                .setScore(score)
                .setTimestamp(actionDate)
                .build();
        client.getProducer().send(
                new ProducerRecord<>(client.getProducerTopics().get(TopicType.EVENTS_SIMILARITY), eventSimilarity));
        log.info("{}", eventSimilarity);
    }

    private void putMinWeightsSums(Long eventA, Long eventB, Double sum) {
        Long first = Math.min(eventA, eventB);
        Long second = Math.max(eventA, eventB);

        minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .put(second, sum);
    }

    private Double getMinWeightsSums(Long eventA, Long eventB) {
        Long first = Math.min(eventA, eventB);
        Long second = Math.max(eventA, eventB);

        return minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .getOrDefault(second, 0.0);
    }
}
