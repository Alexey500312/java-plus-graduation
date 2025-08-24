package ru.practicum.kafka.stats.analyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.stats.recommended.RecommendedEventProto;
import ru.practicum.kafka.stats.analyzer.dto.EventSimilarityDto;
import ru.practicum.kafka.stats.analyzer.dto.RecommendedEventDto;

@Mapper
public interface RecommendedEventMapper {
    RecommendedEventMapper INSTANCE = Mappers.getMapper(RecommendedEventMapper.class);

    RecommendedEventProto toRecommendedEventProto(EventSimilarityDto eventSimilarityDto);

    RecommendedEventProto toRecommendedEventProto(RecommendedEventDto recommendedEventDto);
}
