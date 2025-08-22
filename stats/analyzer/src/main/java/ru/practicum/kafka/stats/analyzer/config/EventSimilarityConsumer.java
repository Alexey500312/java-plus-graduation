package ru.practicum.kafka.stats.analyzer.config;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Properties;

public interface EventSimilarityConsumer {
    Properties getEventSimilarityProperties();

    EnumMap<TopicType, String> getEventSimilarityTopics();

    Duration getEventSimilarityPollTimeout();
}
