package ru.practicum.kafka.stats.analyzer.service.similarity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.kafka.stats.analyzer.model.EventSimilarity;
import ru.practicum.kafka.stats.analyzer.mapper.EventSimilarityMapper;
import ru.practicum.kafka.stats.analyzer.repository.EventSimilarityRepository;

@Component
@RequiredArgsConstructor
public class EventSimilarityHandlerImpl implements EventSimilarityHandler {
    private final EventSimilarityRepository eventSimilarityRepository;

    @Override
    public void handle(EventSimilarityAvro eventSimilarityAvro) {
        EventSimilarity eventSimilarity = EventSimilarityMapper.INSTANCE.toEventSimilarity(eventSimilarityAvro);
        eventSimilarityRepository.save(eventSimilarity);
    }
}
