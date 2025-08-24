package ru.practicum.kafka.stats.collector.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter
@ToString
@ConfigurationProperties("collector.kafka.producer")
public class KafkaConfig {
    private final Properties properties;
    private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

    public KafkaConfig(Properties properties, Map<String, String> topics) {
        this.properties = properties;
        for (Map.Entry<String, String> entry : topics.entrySet()) {
            TopicType topicType = TopicType.toTopicsType(entry.getKey());
            if (topicType != null) {
                this.topics.put(topicType, entry.getValue());
            }
        }
    }
}
