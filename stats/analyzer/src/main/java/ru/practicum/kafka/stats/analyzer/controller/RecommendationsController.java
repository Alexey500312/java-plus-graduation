package ru.practicum.kafka.stats.analyzer.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.grpc.analyzer.RecommendationsControllerGrpc;
import ru.practicum.ewm.stats.interactions.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.predictions.UserPredictionsRequestProto;
import ru.practicum.ewm.stats.recommended.RecommendedEventProto;
import ru.practicum.ewm.stats.similar.SimilarEventsRequestProto;
import ru.practicum.kafka.stats.analyzer.service.recommendation.RecommendationService;

import java.util.Collection;

@Slf4j
@GrpcService
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {
    private final RecommendationService recommendationService;

    public RecommendationsController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public void getRecommendationsForUser(UserPredictionsRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("UserPredictionsRequestProto: {}", request);
        Collection<RecommendedEventProto> recommendedEvents = recommendationService.getRecommendationsForUser(request);
        generateResponse(recommendedEvents, responseObserver);
    }

    @Override
    public void getSimilarEvents(SimilarEventsRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("SimilarEventsRequestProto: {}", request);
        Collection<RecommendedEventProto> recommendedEvents = recommendationService.getSimilarEvents(request);
        generateResponse(recommendedEvents, responseObserver);
    }

    @Override
    public void getInteractionsCount(InteractionsCountRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("InteractionsCountRequestProto: {}", request);
        Collection<RecommendedEventProto> recommendedEvents = recommendationService.getInteractionsCount(request);
        generateResponse(recommendedEvents, responseObserver);
    }

    private void generateResponse(Collection<RecommendedEventProto> recommendedEvents, StreamObserver<RecommendedEventProto> responseObserver) {
        try {
            for (RecommendedEventProto recommendedEvent : recommendedEvents) {
                responseObserver.onNext(recommendedEvent);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}
