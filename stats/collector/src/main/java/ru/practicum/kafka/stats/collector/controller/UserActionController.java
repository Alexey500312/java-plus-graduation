package ru.practicum.kafka.stats.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.action.UserActionProto;
import ru.practicum.ewm.stats.grpc.collector.UserActionControllerGrpc;
import ru.practicum.kafka.stats.collector.handler.action.UserActionHandler;

@Slf4j
@GrpcService
public class UserActionController extends UserActionControllerGrpc.UserActionControllerImplBase {
    private final UserActionHandler userActionHandler;

    public UserActionController(UserActionHandler userActionHandler) {
        this.userActionHandler = userActionHandler;
    }

    @Override
    public void collectUserAction(UserActionProto userAction, StreamObserver<Empty> responseObserver) {
        try {
            log.warn("UserAction: {}", userAction);
            userActionHandler.handle(userAction);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}
