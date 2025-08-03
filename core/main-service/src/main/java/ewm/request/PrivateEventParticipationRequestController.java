package ewm.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
@RestController
@Validated
public class PrivateEventParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(@PathVariable @Positive Long userId,
                                                                                 @PathVariable @Positive Long eventId) {
        return participationRequestService.getParticipationRequestsFortEvent(userId, eventId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultParticipationRequestStatusDto updateParticipationRequestStatus(@PathVariable @Positive Long userId,
                                                                                @PathVariable @Positive Long eventId,
                                                                                @RequestBody @Valid UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto) {
        return participationRequestService.updateParticipationRequestStatus(userId, eventId, updateParticipationRequestStatusDto);
    }
}
