package ewm.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFeignDto;

import java.util.Collection;

@RequestMapping("/private/events")
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFeignDto findEventById(@PathVariable @Positive Long eventId) {
        return eventService.findEventById(eventId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventFeignDto> findEventCollection(@RequestParam @NotNull Collection<Long> eventIds) {
        return eventService.findEventCollection(eventIds);
    }

    @PutMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public void changeConfirmedRequests(@PathVariable @Positive Long eventId,
                                        @RequestParam @Min(0) Integer confirmedRequests) {
        eventService.changeConfirmedRequests(eventId, confirmedRequests);
    }
}
