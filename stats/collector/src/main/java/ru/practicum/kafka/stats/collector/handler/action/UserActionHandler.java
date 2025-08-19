package ru.practicum.kafka.stats.collector.handler.action;

import ru.practicum.ewm.stats.action.UserActionProto;

public interface UserActionHandler {
    void handle(UserActionProto userAction);
}
