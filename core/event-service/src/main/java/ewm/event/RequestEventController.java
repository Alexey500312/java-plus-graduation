package ewm.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.ResultParticipationRequestStatusDto;
import ru.practicum.dto.request.UpdateParticipationRequestStatusDto;

import java.util.Collection;

@RequestMapping("/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
@RestController
@Validated
public class RequestEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(@PathVariable @Positive Long userId,
                                                                                 @PathVariable @Positive Long eventId) {
        return eventService.getParticipationRequestsFortEvent(userId, eventId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultParticipationRequestStatusDto updateParticipationRequestStatus(@PathVariable @Positive Long userId,
                                                                                @PathVariable @Positive Long eventId,
                                                                                @RequestBody @Valid UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto) {
        return eventService.updateParticipationRequestStatus(userId, eventId, updateParticipationRequestStatusDto);
    }
}