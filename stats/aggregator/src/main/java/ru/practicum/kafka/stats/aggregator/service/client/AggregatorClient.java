 package ru.practicum.kafka.stats.aggregator.service.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.stats.aggregator.config.*;

import java.time.Duration;
import java.util.EnumMap;

@Component
@RequiredArgsConstructor
public class AggregatorClient implements Client {
    private final ru.practicum.kafka.stats.aggregator.config.KafkaProducer producerConfig;
    private final ru.practicum.kafka.stats.aggregator.config.KafkaConsumer consumerConfig;
    private final Weight weightConfig;
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;

    @Override
    public Producer<String, SpecificRecordBase> getProducer() {
        if (producer == null) {
            initProducer();
        }
        return producer;
    }

    @Override
    public EnumMap<TopicType, String> getProducerTopics() {
        return producerConfig.getProducerTopics();
    }

    @Override
    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null) {
            initConsumer();
        }
        return consumer;
    }

    @Override
    public EnumMap<TopicType, String> getConsumerTopics() {
        return consumerConfig.getConsumerTopics();
    }

    @Override
    public Duration getConsumerPollTimeout() {
        return consumerConfig.getPollTimeout();
    }

    @Override
    public EnumMap<WeightType, Double> getActionWeights() {
        return weightConfig.getWeights();
    }

    @Override
    public void stop() {
        if (producer != null) {
            producer.close();
        }
        if (consumer != null) {
            consumer.close();
        }
    }

    private void initProducer() {
        producer = new KafkaProducer<>(producerConfig.getProducerProperties());
    }

    private void initConsumer() {
        consumer = new KafkaConsumer<>(consumerConfig.getConsumerProperties());
    }
}
