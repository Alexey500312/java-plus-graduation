package ru.practicum.kafka.stats.analyzer.config;

import java.util.EnumMap;

public interface Weight {
    EnumMap<WeightType, Double> getWeights();
}
