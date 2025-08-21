package ru.practicum.kafka.stats.aggregator.service.client;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import ru.practicum.kafka.stats.aggregator.config.TopicType;
import ru.practicum.kafka.stats.aggregator.config.WeightType;

import java.time.Duration;
import java.util.EnumMap;

public interface Client {
    Producer<String, SpecificRecordBase> getProducer();

    EnumMap<TopicType, String> getProducerTopics();

    Consumer<String, SpecificRecordBase> getConsumer();

    EnumMap<TopicType, String> getConsumerTopics();

    Duration getConsumerPollTimeout();

    EnumMap<WeightType, Double> getActionWeights();

    void stop();
}
