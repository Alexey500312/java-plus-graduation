package ewm.request;

import ewm.feign.EventClient;
import ewm.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFeignDto;
import ru.practicum.dto.event.EventState;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.ParticipationRequestStatus;
import ru.practicum.dto.request.ResultParticipationRequestStatusDto;
import ru.practicum.dto.request.UpdateParticipationRequestStatusDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.IncorrectlyException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestSeviceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventClient eventClient;
    private final UserClient userClient;

    @Override
    public Collection<ParticipationRequestDto> getParticipationRequestOtherEvents(Long userId) {
        UserDto user = findUserById(userId);
        Collection<ParticipationRequest> requests = participationRequestRepository.findByRequesterId(user.getId());
        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDtoCollection(requests);
    }

    @Override
    public Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(Long userId, Long eventId) {
        UserDto user = findUserById(userId);
        EventFeignDto event = findEventById(eventId, user.getId());
        Collection<ParticipationRequest> requests =
                participationRequestRepository.findByEventId(event.getId());
        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDtoCollection(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        UserDto user = findUserById(userId);
        EventFeignDto event = findEventById(eventId);
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ForbiddenException("Достигнут лимит запросов на участие");
        }
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventId(event.getId())
                .requesterId(user.getId())
                .status(event.getParticipantLimit() > 0 && event.isRequestModeration()
                        ? ParticipationRequestStatus.PENDING
                        : ParticipationRequestStatus.CONFIRMED)
                .build();
        if (ParticipationRequestStatus.CONFIRMED.equals(participationRequest.getStatus())) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventClient.changeConfirmedRequests(event.getId(), event.getConfirmedRequests());
        }
        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(
                participationRequestRepository.save(participationRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        UserDto user = findUserById(userId);
        ParticipationRequest request = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден запрос на участие с id = %d", requestId)));
        if (!request.getRequesterId().equals(user.getId())) {
            throw new ForbiddenException("Можно отменить только свой запрос на участие");
        }
        request.setStatus(ParticipationRequestStatus.CANCELED);
        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(
                participationRequestRepository.save(request));
    }

    @Override
    @Transactional
    public ResultParticipationRequestStatusDto updateParticipationRequestStatus(
            Long userId, long eventId, UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto) {
        UserDto user = findUserById(userId);
        EventFeignDto event = findEventById(eventId, user.getId());
        if (!ParticipationRequestStatus.CONFIRMED.equals(updateParticipationRequestStatusDto.getStatus()) &&
                !ParticipationRequestStatus.REJECTED.equals(updateParticipationRequestStatusDto.getStatus())) {
            throw new IncorrectlyException("Указан недопустимый статус. Допустимые статусы CONFIRMED и REJECTED");
        }
        Collection<ParticipationRequest> requests = participationRequestRepository.findByIdIn(updateParticipationRequestStatusDto.getRequestIds());
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return getResultParticipationRequestStatusDto(requests);
        }
        Optional<ParticipationRequest> requestWithNotValidStatus = requests.stream()
                .filter(r -> !ParticipationRequestStatus.PENDING.equals(r.getStatus()))
                .findFirst();
        if (requestWithNotValidStatus.isPresent()) {
            throw new ForbiddenException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }
        if (ParticipationRequestStatus.CONFIRMED.equals(updateParticipationRequestStatusDto.getStatus()) &&
                event.getParticipantLimit() > 0 &&
                event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ForbiddenException("Достигнут лимит по заявкам на данное событие");
        }
        for (ParticipationRequest participationRequest : requests) {
            if (ParticipationRequestStatus.CONFIRMED.equals(updateParticipationRequestStatusDto.getStatus())) {
                if (event.getParticipantLimit() > 0 && event.getParticipantLimit() > event.getConfirmedRequests()) {
                    participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
                }
            } else if (ParticipationRequestStatus.REJECTED.equals(updateParticipationRequestStatusDto.getStatus())) {
                participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
            }
        }
        eventClient.changeConfirmedRequests(event.getId(), event.getConfirmedRequests());
        participationRequestRepository.saveAll(requests);
        return getResultParticipationRequestStatusDto(requests);
    }

    private UserDto findUserById(Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        return userClient.getUsers(userIds, 0, 1).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с id = %d", userId)));
    }

    private EventFeignDto findEventById(Long eventId) {
        Optional<EventFeignDto> event = Optional.ofNullable(eventClient.findEventById(eventId));
        return event
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено событие с id = %d", eventId)));
    }

    private EventFeignDto findEventById(Long eventId, Long userId) {
        EventFeignDto event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException(String.format("Доступ к событию с id = %d запрещён", event.getId()));
        }
        return event;
    }

    private ResultParticipationRequestStatusDto getResultParticipationRequestStatusDto(Collection<ParticipationRequest> participationRequests) {
        Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest participationRequest : participationRequests) {
            if (ParticipationRequestStatus.CONFIRMED.equals(participationRequest.getStatus())) {
                confirmedRequests.add(ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(participationRequest));
            } else if (ParticipationRequestStatus.REJECTED.equals(participationRequest.getStatus())) {
                rejectedRequests.add(ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(participationRequest));
            }
        }
        return ResultParticipationRequestStatusDto.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
