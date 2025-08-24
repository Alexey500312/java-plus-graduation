package ru.practicum.kafka.stats.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("aggregator.kafka")
public class KafkaProducerConfig implements KafkaProducer {
    private ProducerConfig producer;

    @Override
    public Properties getProducerProperties() {
        return producer.getProperties();
    }

    @Override
    public EnumMap<TopicType, String> getProducerTopics() {
        return producer.getTopics();
    }

    @Getter
    public static class ProducerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

        public ProducerConfig(Properties properties, Map<String, String> topics) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                TopicType topicType = TopicType.toTopicsType(entry.getKey());
                if (topicType != null) {
                    this.topics.put(topicType, entry.getValue());
                }
            }
        }
    }
}
