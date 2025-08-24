package ru.practicum.kafka.stats.analyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.kafka.stats.analyzer.model.EventSimilarity;

@Mapper
public interface EventSimilarityMapper {
    EventSimilarityMapper INSTANCE = Mappers.getMapper(EventSimilarityMapper.class);

    @Mapping(source = "eventA", target = "id.eventIdA")
    @Mapping(source = "eventB", target = "id.eventIdB")
    @Mapping(source = "timestamp", target = "actionDate")
    EventSimilarity toEventSimilarity(EventSimilarityAvro eventSimilarityAvro);
}
