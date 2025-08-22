package ru.practicum.kafka.stats.analyzer.client;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import ru.practicum.kafka.stats.analyzer.config.TopicType;

import java.time.Duration;
import java.util.EnumMap;

public interface Client {
    Consumer<String, SpecificRecordBase> getConsumer();

    EnumMap<TopicType, String> getTopics();

    Duration getPollTimeout();

    void stop();
}
