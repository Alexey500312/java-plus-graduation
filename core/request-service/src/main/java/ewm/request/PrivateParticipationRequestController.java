package ewm.request;

import ewm.client.CollectorClient;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.Collection;

@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@RestController
public class PrivateParticipationRequestController {
    private final ParticipationRequestService participationRequestService;
    private final CollectorClient collectorClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> getParticipationRequestOtherEvents(@PathVariable @Positive Long userId) {
        return participationRequestService.getParticipationRequestOtherEvents(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable @Positive Long userId,
                                                              @RequestParam @Positive Long eventId) {
        ParticipationRequestDto result = participationRequestService.createParticipationRequest(userId, eventId);

        try {
            collectorClient.saveRegister(userId, eventId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return result;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Positive Long userId,
                                                              @PathVariable @Positive Long requestId) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }
}
