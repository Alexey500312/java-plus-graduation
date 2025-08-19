package ru.practicum.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.ResultParticipationRequestStatusDto;
import ru.practicum.dto.request.UpdateParticipationRequestStatusDto;

import java.util.Collection;

public interface RequestOperations {
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(@PathVariable @Positive Long userId,
                                                                          @PathVariable @Positive Long eventId);

    @PutMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    ResultParticipationRequestStatusDto updateParticipationRequestStatus(@RequestBody @Valid UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto,
                                                                         @PathVariable @Positive Long userId,
                                                                         @PathVariable @Positive Long eventId);
}
