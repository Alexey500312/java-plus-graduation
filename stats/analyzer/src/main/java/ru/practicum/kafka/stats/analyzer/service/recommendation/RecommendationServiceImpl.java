package ru.practicum.kafka.stats.analyzer.service.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.interactions.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.predictions.UserPredictionsRequestProto;
import ru.practicum.ewm.stats.recommended.RecommendedEventProto;
import ru.practicum.ewm.stats.similar.SimilarEventsRequestProto;
import ru.practicum.kafka.stats.analyzer.dto.NearestNeighborsDto;
import ru.practicum.kafka.stats.analyzer.dto.RecommendedEventDto;
import ru.practicum.kafka.stats.analyzer.mapper.RecommendedEventMapper;
import ru.practicum.kafka.stats.analyzer.model.UserAction;
import ru.practicum.kafka.stats.analyzer.repository.EventSimilarityRepository;
import ru.practicum.kafka.stats.analyzer.repository.UserActionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final UserActionRepository userActionRepository;
    private final EventSimilarityRepository eventSimilarityRepository;

    @Override
    public Collection<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequest) {
        // Этап 1 Подбор мероприятий, с которыми пользователь ещё не взаимодействовал.

        Sort sort = Sort.by(Sort.Order.desc("actionDate"));
        Pageable pageable = PageRequest.of(0, userPredictionsRequest.getMaxResults(), sort);
        Collection<Long> userActionIds = userActionRepository.findByIdUserId(userPredictionsRequest.getUserId(), pageable).stream()
                .map(ua -> ua.getId().getEventId())
                .toList();
        if (userActionIds.isEmpty()) {
            return List.of();
        }
        Collection<Long> similarEvents = eventSimilarityRepository.getSimilarEventsForRecommended(
                userActionIds,
                userPredictionsRequest.getUserId(),
                userPredictionsRequest.getMaxResults());

        // Этап 2 Вычисление оценки для каждого нового мероприятия.

        Map<Long, List<NearestNeighborsDto>> nearestNeighbors = eventSimilarityRepository.getNearestNeighbors(similarEvents, userPredictionsRequest.getUserId()).stream()
                .collect(Collectors.groupingBy(NearestNeighborsDto::getEventId));

        Map<Long, Double> nearestNeighborActions = userActionRepository.findByIdEventIdInAndIdUserId(
                        nearestNeighbors.values().stream()
                                .flatMap(l -> l.stream()
                                        .map(NearestNeighborsDto::getNeighborEventId))
                                .distinct()
                                .toList(),
                        userPredictionsRequest.getUserId()).stream()
                .collect(Collectors.toMap(
                        ua -> ua.getId().getEventId(),
                        UserAction::getWeight));

        Collection<RecommendedEventProto> result = new ArrayList<>();
        for (Map.Entry<Long, List<NearestNeighborsDto>> entry : nearestNeighbors.entrySet()) {
            double sumRating = 0.0;
            double sumSimilar = 0.0;
            for (NearestNeighborsDto item : entry.getValue()) {
                sumRating += (nearestNeighborActions.getOrDefault(item.getNeighborEventId(), 0.0) * item.getScore());
                sumSimilar += item.getScore();
            }
            double score = sumRating / sumSimilar;
            result.add(RecommendedEventProto.newBuilder()
                    .setEventId(entry.getKey())
                    .setScore(score)
                    .build());
        }

        return result.stream()
                .sorted(Comparator.comparing(RecommendedEventProto::getScore).reversed())
                .toList();
    }

    @Override
    public Collection<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto similarEventsRequest) {
        return eventSimilarityRepository.getSimilarEvents(
                        similarEventsRequest.getEventId(),
                        similarEventsRequest.getUserId(),
                        similarEventsRequest.getMaxResults()).stream()
                .map(RecommendedEventMapper.INSTANCE::toRecommendedEventProto)
                .toList();
    }

    @Override
    public Collection<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto interactionsCountRequest) {
        Collection<RecommendedEventDto> rating = userActionRepository.getInteractionsCount(interactionsCountRequest.getEventIdList());
        return rating.stream()
                .map(RecommendedEventMapper.INSTANCE::toRecommendedEventProto)
                .toList();
    }
}
