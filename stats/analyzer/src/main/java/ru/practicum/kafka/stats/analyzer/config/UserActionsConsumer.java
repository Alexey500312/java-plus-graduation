package ru.practicum.kafka.stats.analyzer.config;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Properties;

public interface UserActionsConsumer {
    Properties getUserActionsProperties();

    EnumMap<TopicType, String> getUserActionsTopics();

    Duration getUserActionsPollTimeout();
}
