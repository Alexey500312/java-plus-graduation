package ru.practicum.kafka.stats.aggregator.config;

import java.util.EnumMap;

public interface Weight {
    EnumMap<WeightType, Double> getWeights();
}
