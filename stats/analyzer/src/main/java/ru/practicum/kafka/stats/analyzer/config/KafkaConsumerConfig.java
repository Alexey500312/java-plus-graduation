package ru.practicum.kafka.stats.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("analyzer.kafka")
public class KafkaConsumerConfig implements UserActionsConsumer, EventSimilarityConsumer {
    private UserActionConfig actionConsumer;
    private EventSimilarityConfig similarityConsumer;

    @Override
    public Properties getUserActionsProperties() {
        return actionConsumer.getProperties();
    }

    @Override
    public EnumMap<TopicType, String> getUserActionsTopics() {
        return actionConsumer.getTopics();
    }

    @Override
    public Duration getUserActionsPollTimeout() {
        return actionConsumer.getPollTimeout();
    }

    @Override
    public Properties getEventSimilarityProperties() {
        return similarityConsumer.getProperties();
    }

    @Override
    public EnumMap<TopicType, String> getEventSimilarityTopics() {
        return similarityConsumer.getTopics();
    }

    @Override
    public Duration getEventSimilarityPollTimeout() {
        return similarityConsumer.getPollTimeout();
    }

    @Getter
    public static class UserActionConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
        private final Duration pollTimeout;

        public UserActionConfig(Properties properties, Map<String, String> topics, Duration pollTimeout) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.toTopicsType(entry.getKey()), entry.getValue());
            }
            this.pollTimeout = pollTimeout;
        }
    }

    @Getter
    public static class EventSimilarityConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
        private final Duration pollTimeout;

        public EventSimilarityConfig(Properties properties, Map<String, String> topics, Duration pollTimeout) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.toTopicsType(entry.getKey()), entry.getValue());
            }
            this.pollTimeout = pollTimeout;
        }
    }
}
