package ewm.event;

import ewm.client.StatsClient;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventStatsRetriever {
    private final StatsClient statsClient;

    @Named("getEventViews")
    public int getEventViews(Long eventId) {
        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);

        return Objects.requireNonNull(statsClient.getStats(start, end, List.of("/events/" + eventId), true).getBody()).size();
    }
}
