package ewm.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import ewm.category.Category;
import ewm.category.CategoryRepository;
import ewm.client.StatsClient;
import ewm.feign.RequestClient;
import ewm.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.ResultParticipationRequestStatusDto;
import ru.practicum.dto.request.UpdateParticipationRequestStatusDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.CreateEntityException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UpdateEntityException;
import ru.practicum.pageble.PageOffset;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для сущности "Событие".
 */
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    /**
     * Хранилище данных для сущности "Категория".
     */
    private final CategoryRepository categoryRepository;

    /**
     * Хранилище данных для сущности "Событие".
     */
    private final EventRepository eventRepository;

    /**
     * Feign клиент для сущности "Пользователь".
     */
    private final UserClient userClient;

    /**
     * Клиент статистики
     */
    private final StatsClient statsClient;

    /**
     * Feign клиент для сущности "Запрос на участие".
     */
    private final RequestClient requestClient;

    /**
     * Добавить новое событие.
     *
     * @param userId         идентификатор текущего пользователя.
     * @param createEventDto трансферный объект, содержащий данные для добавления нового события.
     * @return трансферный объект, содержащий данные о событии.
     */
    public EventDto createEvent(Long userId, CreateEventDto createEventDto) {
        UserDto user = findUserById(userId);
        Category category = categoryRepository.findById(createEventDto.getCategory()).orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", createEventDto.getCategory())));

        if (createEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new CreateEntityException("Дата и время события должна быть больше текущих даты и времени не менее, чем на 2 часа");
        }

        Event event = EventMapper.INSTANCE.toEvent(createEventDto);
        event.setInitiatorId(user.getId());
        event.setCategory(category);

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(user))
                .build();

        return EventMapper.INSTANCE.toEventDto(eventRepository.save(event), context);
    }

    /**
     * Получить коллекцию событий.
     *
     * @param userId идентификатор пользователя.
     * @param from   количество событий, которое необходимо пропустить.
     * @param size   количество событий, которое необходимо получить.
     * @return коллекция событий.
     */
    public Collection<EventDto> getEvents(Long userId, int from, int size) {
        UserDto user = findUserById(userId);

        Predicate predicate = QEvent.event.initiatorId.eq(userId);
        PageOffset pageOffset = PageOffset.of(from, size, Sort.by("id").ascending());

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(user))
                .build();

        return EventMapper.INSTANCE.toEventDtoCollection(eventRepository.findAll(predicate, pageOffset).getContent(), context);
    }

    /**
     * Получить коллекцию событий.
     *
     * @param search объект, содержащий параметры поиска событий.
     * @return коллекция трансферных объектов, содержащий краткую информацию по событиям.
     */
    public Collection<EventDto> getEvents(EventSearch search) {
        Collection<Event> events = getEventsInternal(search);

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(events))
                .build();

        return EventMapper.INSTANCE.toEventDtoCollection(events, context);
    }

    /**
     * Получить коллекцию опубликованных событий.
     *
     * @param search объект, содержащий параметры поиска событий.
     * @return коллекция трансферных объектов, содержащий краткую информацию по событиям.
     */
    public Collection<EventShortDto> getPublishedEvents(EventSearch search) {
        Collection<Event> events = getEventsInternal(search);

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(events))
                .build();

        return EventMapper.INSTANCE.toEventShortDtoCollection(events, context);
    }

    /**
     * Получить коллекцию событий.
     *
     * @param search объект, содержащий параметры поиска событий.
     * @return коллекция трансферных объектов, содержащий краткую информацию по событиям.
     */
    private Collection<Event> getEventsInternal(EventSearch search) {
        QEvent event = QEvent.event;

        BooleanBuilder predicate = new BooleanBuilder();

        if (search.getText() != null && !search.getText().isBlank()) {
            predicate.and(event.annotation.contains(search.getText()).or(event.description.contains(search.getText())));
        }

        if (search.getUsers() != null && !search.getUsers().isEmpty()) {
            predicate.and(event.initiatorId.in(search.getUsers()));
        }

        if (search.getStates() != null && !search.getStates().isEmpty()) {
            predicate.and(event.state.in(search.getStates()));
        }

        if (search.getCategories() != null && !search.getCategories().isEmpty()) {
            predicate.and(event.category.id.in(search.getCategories()));
        }

        if (search.getPaid() != null) {
            predicate.and(event.paid.eq(search.getPaid()));
        }

        if (search.getRangeStart() != null) {
            predicate.and(event.eventDate.after(search.getRangeStart()));
        }

        if (search.getRangeEnd() != null) {
            predicate.and(event.eventDate.before(search.getRangeEnd()));
        }

        if (search.isOnlyAvailable()) {
            predicate.and(event.participantLimit.eq(0).or(event.confirmedRequests.lt(event.participantLimit)));
        }

        Pageable pageable = PageOffset.of(search.getFrom(), search.getSize());
        if (search.getSort() != null) {
            switch (search.getSort()) {
                case EVENT_DATE ->
                        pageable = PageOffset.of(search.getFrom(), search.getSize(), Sort.Direction.ASC, "eventDate");
                case VIEWS -> pageable = PageOffset.of(search.getFrom(), search.getSize(), Sort.Direction.ASC, "views");
            }
        }

        List<Event> result = new ArrayList<>();
        eventRepository.findAll(predicate, pageable).forEach(result::add);
        return result;
    }

    /**
     * Получить событие по его идентификатору.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    public EventDto getEventById(Long userId, Long eventId) {
        UserDto user = findUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));

        if (!Objects.equals(event.getInitiatorId(), user.getId())) {
            throw new ForbiddenException(String.format("Доступ к событию с id = %d запрещён", eventId));
        }

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(user))
                .build();

        return EventMapper.INSTANCE.toEventDto(event, context);
    }

    /**
     * Получить опубликованное событие по его идентификатору.
     *
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    public EventDto getPublishedEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new NotFoundException(String.format("Событие с id = %d не найдено", eventId));
        }

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(List.of(event)))
                .build();

        return EventMapper.INSTANCE.toEventDto(event, context);
    }

    /**
     * Обновить событие.
     *
     * @param userId         идентификатор текущего пользователя.
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    public EventDto updateEventByUser(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        UserDto user = findUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));

        if (!Objects.equals(event.getInitiatorId(), user.getId())) {
            throw new ForbiddenException(String.format("Доступ к событию с id = %d запрещён", eventId));
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(String.format("Событие с id = %d запрещено для редактирования", eventId));
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getEventDate() != null) {
            if (updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new UpdateEntityException("Дата и время события должна быть больше текущих даты и времени не менее, чем на 2 часа");
            }
            event.setEventDate(updateEventDto.getEventDate());
        }

        if (updateEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", updateEventDto.getCategory())));
            event.setCategory(category);
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(updateEventDto.getLocation());
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        if (updateEventDto.getStateAction() != null) {
            if (event.getState() == EventState.PUBLISHED) {
                throw new ForbiddenException("Only pending or canceled events can be changed");
            }

            if (updateEventDto.getStateAction() == EventStateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (updateEventDto.getStateAction() == EventStateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(user))
                .build();

        return EventMapper.INSTANCE.toEventDto(eventRepository.save(event), context);
    }

    /**
     * Обновить событие.
     *
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    public EventDto updateEventByAdmin(Long eventId, UpdateEventDto updateEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ForbiddenException("Нельзя редактировать событие, до наступления которого осталось меньше часа");
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getEventDate() != null) {
            if (updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new UpdateEntityException("Дата и время события должна быть больше текущих даты и времени не менее, чем на 2 часа");
            }
            event.setEventDate(updateEventDto.getEventDate());
        }

        if (updateEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", updateEventDto.getCategory())));
            event.setCategory(category);
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(updateEventDto.getLocation());
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        if (updateEventDto.getStateAction() != null) {
            if (updateEventDto.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ForbiddenException("Cannot publish the event because it's not in the right state: PENDING");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventDto.getStateAction() == EventStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ForbiddenException("Cannot reject the event because it's in the state: PUBLISHED");
                }
                event.setState(EventState.REJECTED);
            }
        }

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(List.of(event)))
                .build();

        return EventMapper.INSTANCE.toEventDto(eventRepository.save(event), context);
    }

    @Override
    public EventFeignDto findEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(List.of(event)))
                .build();

        return EventMapper.INSTANCE.toEventFeignDto(event, context);
    }

    @Override
    public Collection<EventFeignDto> findEventCollection(Collection<Long> eventIds) {
        Collection<Event> events = eventRepository.findAllById(eventIds);

        EventMapperContext context = EventMapperContext.builder()
                .statsClient(statsClient)
                .users(getUsers(events))
                .build();

        return EventMapper.INSTANCE.toEventFeignDtoCollection(events, context);
    }

    public void changeConfirmedRequests(Long eventId, Integer confirmedRequests) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %d не найдено", eventId)));
        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
    }

    @Override
    public Collection<ParticipationRequestDto> getParticipationRequestsFortEvent(Long userId, Long eventId) {
        return requestClient.getParticipationRequestsFortEvent(userId, eventId);
    }

    @Override
    public ResultParticipationRequestStatusDto updateParticipationRequestStatus(Long userId, long eventId, UpdateParticipationRequestStatusDto updateParticipationRequestStatusDto) {
        return requestClient.updateParticipationRequestStatus(updateParticipationRequestStatusDto, userId, eventId);
    }

    /**
     * Поиск пользователя
     *
     * @param userId ИД пользователя
     * @return объект сущности "Пользователь"
     */
    private UserDto findUserById(Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        return userClient.getUsers(userIds, 0, 1).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с id = %d", userId)));
    }

    private Map<Long, UserDto> getUsers(Collection<Event> events) {
        if (events == null) {
            return new HashMap<>();
        }
        List<Long> userIds = events.stream()
                .map(Event::getInitiatorId)
                .distinct()
                .toList();
        if (!userIds.isEmpty()) {
            return userClient.getUsers(userIds, 0, userIds.size()).stream()
                    .collect(Collectors.toMap(UserDto::getId, Function.identity()));
        }
        return new HashMap<>();
    }

    private Map<Long, UserDto> getUsers(UserDto userDto) {
        Map<Long, UserDto> result = new HashMap<>();
        if (userDto != null) {
            result.put(userDto.getId(), userDto);
        }
        return result;

    }
}
