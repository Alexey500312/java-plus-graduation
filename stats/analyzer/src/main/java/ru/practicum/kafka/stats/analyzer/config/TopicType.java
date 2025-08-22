package ru.practicum.kafka.stats.analyzer.config;

public enum TopicType {
    USER_ACTIONS,
    EVENTS_SIMILARITY;

    public static TopicType toTopicsType(String type) {
        for (TopicType value : values()) {
            if (value.name().equalsIgnoreCase(type.replace("-", "_"))) {
                return value;
            }
        }
        return null;
    }
}
