package ewm.client;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.action.ActionTypeProto;
import ru.practicum.ewm.stats.action.UserActionProto;
import ru.practicum.ewm.stats.grpc.collector.UserActionControllerGrpc;

import java.time.Instant;

@Slf4j
@Service
public class CollectorClient {
    UserActionControllerGrpc.UserActionControllerBlockingStub userActionClient;

    public CollectorClient(@GrpcClient("collector") UserActionControllerGrpc.UserActionControllerBlockingStub userActionClient) {
        this.userActionClient = userActionClient;
    }

    public void saveView(Long userId, Long eventId) {
        saveUserAction(userId, eventId, ActionTypeProto.ACTION_VIEW);
    }

    public void saveRegister(Long userId, Long eventId) {
        saveUserAction(userId, eventId, ActionTypeProto.ACTION_REGISTER);
    }

    public void saveLike(Long userId, Long eventId) {
        saveUserAction(userId, eventId, ActionTypeProto.ACTION_LIKE);
    }

    private void saveUserAction(Long userId, Long eventId, ActionTypeProto actionType) {
        log.info("getRecommendationsForUser userId: {}, eventId: {}, actionType: {}", userId, eventId, actionType);

        Instant actionTimestamp = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(actionTimestamp.getEpochSecond())
                .setNanos(actionTimestamp.getNano())
                .build();

        UserActionProto request = UserActionProto.newBuilder()
                .setUserId(userId)
                .setEventId(eventId)
                .setActionType(actionType)
                .setTimestamp(timestamp)
                .build();

        Empty empty = userActionClient.collectUserAction(request);
    }
}
