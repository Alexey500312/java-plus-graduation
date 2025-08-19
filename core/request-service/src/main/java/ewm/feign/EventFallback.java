package ewm.feign;

import ru.practicum.contract.EventOperations;
import ru.practicum.dto.event.EventFeignDto;

import java.util.Collection;
import java.util.List;

public class EventFallback implements EventOperations {
    @Override
    public EventFeignDto findEventById(Long eventId) {
        return EventFeignDto.builder()
                .id(-1L)
                .build();
    }

    @Override
    public Collection<EventFeignDto> findEventCollection(Collection<Long> eventIds) {
        return List.of();
    }

    @Override
    public void changeConfirmedRequests(Long eventId, Integer confirmedRequests) {
    }
}
