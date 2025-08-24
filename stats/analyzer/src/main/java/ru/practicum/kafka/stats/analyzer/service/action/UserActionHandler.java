package ru.practicum.kafka.stats.analyzer.service.action;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionHandler {
    void handle(UserActionAvro userAction);
}
