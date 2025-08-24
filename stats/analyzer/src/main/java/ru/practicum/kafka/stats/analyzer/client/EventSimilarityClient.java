package ru.practicum.kafka.stats.analyzer.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.stats.analyzer.config.EventSimilarityConsumer;
import ru.practicum.kafka.stats.analyzer.config.TopicType;

import java.time.Duration;
import java.util.EnumMap;

@Component("eventSimilarityClient")
@RequiredArgsConstructor
public class EventSimilarityClient implements Client {
    private final EventSimilarityConsumer config;
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
        return config.getEventSimilarityTopics();
    }

    @Override
    public Duration getPollTimeout() {
        return config.getEventSimilarityPollTimeout();
    }

    @Override
    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private void init() {
        consumer = new KafkaConsumer<>(config.getEventSimilarityProperties());
    }
}
