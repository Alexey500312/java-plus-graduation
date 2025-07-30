package ewm.request;

import java.util.Collection;

public interface ParticipationRequestService {
    Collection<ParticipationRequestDto> getParticipationRequestOtherEvents(Long userId);

    Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(Long userId, Long eventId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    ResultParticipationRequestStatusDto updateParticipationRequestStatus(
            Long userId, long eventId, UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto);
}
