package ru.practicum.kafka.stats.analyzer.service.similarity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.kafka.stats.analyzer.client.Client;
import ru.practicum.kafka.stats.analyzer.config.TopicType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.kafka.stats.analyzer.service.OffsetManager.manageOffsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSimilarityProcessor {
    private static final int AMOUNT_PART_COMMIT = 10;

    private final Client eventSimilarityClient;
    private final EventSimilarityHandler eventSimilarityHandler;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(eventSimilarityClient.getConsumer()::wakeup));
        try {
            eventSimilarityClient.getConsumer().subscribe(List.of(eventSimilarityClient.getTopics().get(TopicType.EVENTS_SIMILARITY)));
            while (true) {
                try {
                    ConsumerRecords<String, SpecificRecordBase> records = eventSimilarityClient.getConsumer().poll(eventSimilarityClient.getPollTimeout());
                    int count = 0;
                    for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                        EventSimilarityAvro eventSimilarity = (EventSimilarityAvro) record.value();
                        log.info("{}", eventSimilarity);
                        eventSimilarityHandler.handle(eventSimilarity);
                        manageOffsets(currentOffsets, AMOUNT_PART_COMMIT, record, count, eventSimilarityClient.getConsumer());
                    }
                    eventSimilarityClient.getConsumer().commitAsync();
                } catch (WakeupException e) {
                    throw new WakeupException();
                } catch (Exception e) {
                    log.error("Ошибка ", e);
                }
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                eventSimilarityClient.getConsumer().commitSync(currentOffsets);
            } finally {
                log.info("Закрываем EventSimilarityClient");
                eventSimilarityClient.stop();
            }
        }
    }
}
