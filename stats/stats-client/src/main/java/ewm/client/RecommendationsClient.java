package ewm.client;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.grpc.analyzer.RecommendationsControllerGrpc;
import ru.practicum.ewm.stats.interactions.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.predictions.UserPredictionsRequestProto;
import ru.practicum.ewm.stats.recommended.RecommendedEventProto;
import ru.practicum.ewm.stats.similar.SimilarEventsRequestProto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RecommendationsClient {
    @GrpcClient("analyzer")
    RecommendationsControllerGrpc.RecommendationsControllerBlockingStub recommendationsClient;

    public Stream<RecommendedEventProto> getRecommendationsForUser(Long userId, Integer maxResults) {
        log.info("getRecommendationsForUser userId: {}, maxResults: {}", userId, maxResults);

        UserPredictionsRequestProto request = UserPredictionsRequestProto.newBuilder()
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        Iterator<RecommendedEventProto> iterator = recommendationsClient.getRecommendationsForUser(request);

        return asStream(iterator);
    }

    public Stream<RecommendedEventProto> getSimilarEvents(Long eventId, Long userId, Integer maxResults) {
        log.info("getRecommendationsForUser eventId: {}, userId: {}, maxResults: {}", eventId, userId, maxResults);

        SimilarEventsRequestProto request = SimilarEventsRequestProto.newBuilder()
                .setEventId(eventId)
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        Iterator<RecommendedEventProto> iterator = recommendationsClient.getSimilarEvents(request);

        return asStream(iterator);
    }

    public Stream<RecommendedEventProto> getInteractionsCount(Collection<Long> eventIds) {
        log.info("getInteractionsCount eventIds: {}", eventIds);

        InteractionsCountRequestProto request = InteractionsCountRequestProto.newBuilder()
                .addAllEventId(eventIds)
                .build();

        Iterator<RecommendedEventProto> iterator = recommendationsClient.getInteractionsCount(request);

        return asStream(iterator);
    }

    private Stream<RecommendedEventProto> asStream(Iterator<RecommendedEventProto> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                false);
    }
}
