package ru.practicum.kafka.stats.analyzer.service.similarity;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityHandler {
    void handle(EventSimilarityAvro eventSimilarityAvro);
}
