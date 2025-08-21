package ru.practicum.kafka.stats.aggregator.config;

import java.util.EnumMap;
import java.util.Properties;

public interface KafkaProducer {
    Properties getProducerProperties();

    EnumMap<TopicType, String> getProducerTopics();
}
