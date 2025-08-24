package ru.practicum.kafka.stats.analyzer.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.stats.analyzer.config.Weight;
import ru.practicum.kafka.stats.analyzer.config.WeightType;
import ru.practicum.kafka.stats.analyzer.model.UserAction;

@Mapper
public interface UserActionMapper {
    UserActionMapper INSTANCE = Mappers.getMapper(UserActionMapper.class);

    @Mapping(source = "eventId", target = "id.eventId")
    @Mapping(source = "userId", target = "id.userId")
    @Mapping(source = "actionType", target = "weight", qualifiedByName = "getWeight")
    @Mapping(source = "timestamp", target = "actionDate")
    UserAction toUserAction(UserActionAvro userActionAvro, @Context Weight weight);

    @Named("getWeight")
    static Double getWeight(ActionTypeAvro actionTypeAvro, @Context Weight weight) {
        WeightType weightType = WeightType.valueOf(actionTypeAvro.name());
        return weight.getWeights().get(weightType);
    }
}
