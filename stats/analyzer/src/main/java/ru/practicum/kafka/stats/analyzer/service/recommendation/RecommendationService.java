package ru.practicum.kafka.stats.analyzer.service.recommendation;

import ru.practicum.ewm.stats.interactions.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.predictions.UserPredictionsRequestProto;
import ru.practicum.ewm.stats.recommended.RecommendedEventProto;
import ru.practicum.ewm.stats.similar.SimilarEventsRequestProto;

import java.util.Collection;

public interface RecommendationService {
    Collection<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequest);

    Collection<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto similarEventsRequest);

    Collection<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto interactionsCountRequest);
}
