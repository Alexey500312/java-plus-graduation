package ru.practicum.kafka.stats.aggregator.config;

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
@ConfigurationProperties("aggregator.kafka")
public class KafkaConsumerConfig implements KafkaConsumer {
    private ConsumerConfig consumer;

    @Override
    public Properties getConsumerProperties() {
        return consumer.getProperties();
    }

    @Override
    public EnumMap<TopicType, String> getConsumerTopics() {
        return consumer.getTopics();
    }

    @Override
    public Duration getPollTimeout() {
        return consumer.getPollTimeout();
    }

    @Getter
    public static class ConsumerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
        private final Duration pollTimeout;

        public ConsumerConfig(Properties properties, Map<String, String> topics, Duration pollTimeout) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                TopicType topicType = TopicType.toTopicsType(entry.getKey());
                if (topicType != null) {
                    this.topics.put(topicType, entry.getValue());
                }
            }
            this.pollTimeout = pollTimeout;
        }
    }
}
