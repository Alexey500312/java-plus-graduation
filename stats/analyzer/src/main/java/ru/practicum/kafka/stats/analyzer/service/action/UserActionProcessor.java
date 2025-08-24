package ru.practicum.kafka.stats.analyzer.service.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.stats.analyzer.client.Client;
import ru.practicum.kafka.stats.analyzer.config.TopicType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.kafka.stats.analyzer.service.OffsetManager.manageOffsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionProcessor implements Runnable {
    private static final int AMOUNT_PART_COMMIT = 10;

    private final Client userActionClient;
    private final UserActionHandler userActionHandler;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(userActionClient.getConsumer()::wakeup));
        try {
            userActionClient.getConsumer().subscribe(List.of(userActionClient.getTopics().get(TopicType.USER_ACTIONS)));
            while (true) {
                try {
                    ConsumerRecords<String, SpecificRecordBase> records =
                            userActionClient.getConsumer().poll(userActionClient.getPollTimeout());
                    int count = 0;
                    for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                        UserActionAvro action = (UserActionAvro) record.value();
                        log.info("{}", action);
                        userActionHandler.handle(action);
                        manageOffsets(currentOffsets, AMOUNT_PART_COMMIT, record, count, userActionClient.getConsumer());
                    }
                    userActionClient.getConsumer().commitAsync();
                } catch (WakeupException e) {
                    throw new WakeupException();
                } catch (Exception e) {
                    log.error("Ошибка ", e);
                }
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                userActionClient.getConsumer().commitSync(currentOffsets);
            } finally {
                log.info("Закрываем UserActionsConsumer");
                userActionClient.stop();
            }
        }
    }
}
