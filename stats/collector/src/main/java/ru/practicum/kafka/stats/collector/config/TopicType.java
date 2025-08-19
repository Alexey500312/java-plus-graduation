package ru.practicum.kafka.stats.collector.config;

public enum TopicType {
    USER_ACTIONS;

    public static TopicType toTopicsType(String type) {
        for (TopicType value : values()) {
            if (value.name().equalsIgnoreCase(type.replace("-", "_"))) {
                return value;
            }
        }
        return null;
    }
}
