package ru.practicum.kafka.stats.collector.handler;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.stats.collector.config.KafkaConfig;
import ru.practicum.kafka.stats.collector.config.TopicType;

import java.util.EnumMap;

@Getter
@Component
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final EnumMap<TopicType, String> topics;

    public KafkaEventProducer(KafkaConfig config) {
        producer = new KafkaProducer<>(config.getProperties());
        topics = config.getTopics();
    }
}
