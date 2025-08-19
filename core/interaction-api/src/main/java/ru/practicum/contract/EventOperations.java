package ru.practicum.contract;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFeignDto;

import java.util.Collection;

public interface EventOperations {
    @GetMapping("/{eventId}")
    EventFeignDto findEventById(@PathVariable @Positive Long eventId);

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    Collection<EventFeignDto> findEventCollection(@RequestParam @NotNull Collection<Long> eventIds);

    @PutMapping("/{eventId}/requests")
    void changeConfirmedRequests(@PathVariable @Positive Long eventId,
                                 @RequestParam @Min(0) Integer confirmedRequests);
}
