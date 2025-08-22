package ewm.feign;

import ru.practicum.contract.RequestOperations;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.ResultParticipationRequestStatusDto;
import ru.practicum.dto.request.UpdateParticipationRequestStatusDto;

import java.util.Collection;
import java.util.List;

public class RequestFallBack implements RequestOperations {
    @Override
    public Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(Long userId, Long eventId) {
        return List.of();
    }

    @Override
    public ResultParticipationRequestStatusDto updateParticipationRequestStatus(UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto, Long userId, Long eventId) {
        return ResultParticipationRequestStatusDto.builder()
                .confirmedRequests(List.of())
                .rejectedRequests(List.of())
                .build();
    }

    @Override
    public Boolean checkParticipationRequestConfirmed(Long userId, Long eventId) {
        return false;
    }
}
