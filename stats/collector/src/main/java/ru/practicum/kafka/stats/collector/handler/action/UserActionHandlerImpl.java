package ru.practicum.kafka.stats.collector.handler.action;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.action.UserActionProto;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.stats.collector.config.TopicType;
import ru.practicum.kafka.stats.collector.handler.KafkaEventProducer;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UserActionHandlerImpl implements UserActionHandler {
    protected final KafkaEventProducer producer;

    @Override
    public void handle(UserActionProto userAction) {
        Instant timestamp = userAction.hasTimestamp()
                ? Instant.ofEpochSecond(userAction.getTimestamp().getSeconds(), userAction.getTimestamp().getNanos())
                : Instant.now();
        UserActionAvro userActionAvro = UserActionAvro.newBuilder()
                .setUserId(userAction.getUserId())
                .setEventId(userAction.getEventId())
                .setActionType(ActionTypeAvro.values()[userAction.getActionType().getNumber()])
                .setTimestamp(timestamp)
                .build();

        producer.getProducer().send(new ProducerRecord<>(producer.getTopics().get(TopicType.USER_ACTIONS), userActionAvro));
    }
}
