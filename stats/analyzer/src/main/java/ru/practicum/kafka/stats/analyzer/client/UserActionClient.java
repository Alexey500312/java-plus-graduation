package ru.practicum.kafka.stats.analyzer.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.stats.analyzer.config.TopicType;
import ru.practicum.kafka.stats.analyzer.config.UserActionsConsumer;

import java.time.Duration;
import java.util.EnumMap;

@Component("userActionClient")
@RequiredArgsConstructor
public class UserActionClient implements Client {
    private final UserActionsConsumer config;
    private Consumer<String, SpecificRecordBase> consumer;

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null) {
            init();
        }
        return consumer;
    }

    @Override
    public EnumMap<TopicType, String> getTopics() {
        return config.getUserActionsTopics();
    }

    @Override
    public Duration getPollTimeout() {
        return config.getUserActionsPollTimeout();
    }

    @Override
    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private void init() {
        consumer = new KafkaConsumer<>(config.getUserActionsProperties());
    }
}
