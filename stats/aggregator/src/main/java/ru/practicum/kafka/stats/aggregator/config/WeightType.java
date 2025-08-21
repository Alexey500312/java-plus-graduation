package ru.practicum.kafka.stats.aggregator.config;

public enum WeightType {
    VIEW,
    REGISTER,
    LIKE;

    public static WeightType toWeightsType(String type) {
        for (WeightType value : values()) {
            if (value.name().equalsIgnoreCase(type.replace("-", "_"))) {
                return value;
            }
        }
        return null;
    }
}
