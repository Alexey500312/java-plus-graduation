package ru.practicum.kafka.stats.aggregator.config;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Properties;

public interface KafkaConsumer {
    Properties getConsumerProperties();

    EnumMap<TopicType, String> getConsumerTopics();

    Duration getPollTimeout();
}
