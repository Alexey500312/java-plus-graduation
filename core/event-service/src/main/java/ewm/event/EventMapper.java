package ewm.event;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.event.CreateEventDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventFeignDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;

import java.util.Collection;

/**
 * Маппер для моделей, содержащих информацию о событии.
 */
@Mapper
public interface EventMapper {
    /**
     * Экземпляр маппера для моделей, содержащих информацию о событии.
     */
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    /**
     * Преобразовать трансферный объект, содержащий данные для добавления нового события, в объект события.
     *
     * @param createEventDto трансферный объект, содержащий данные для добавления нового события.
     * @return объект события.
     */
    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", expression = "java(ru.practicum.dto.event.EventState.PENDING)")
    Event toEvent(CreateEventDto createEventDto);

    /**
     * Преобразовать объект события в трансферный объект, содержащий данные о событии.
     *
     * @param event объект события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @Mapping(target = "rating", source = "id", qualifiedByName = "getEventRating")
    @Mapping(target = "initiator", source = "initiatorId", qualifiedByName = "getUserShortDto")
    EventDto toEventDto(Event event, @Context EventMapperContext context);

    /**
     * Преобразовать объект события в трансферный объект, содержащий краткую информацию о событии.
     *
     * @param event объект события.
     * @return трансферный объект, содержащий краткую информацию о событии.
     */
    @Mapping(target = "rating", source = "id", qualifiedByName = "getEventRating")
    @Mapping(target = "initiator", source = "initiatorId", qualifiedByName = "getUserShortDto")
    EventShortDto toEventShortDto(Event event, @Context EventMapperContext context);

    @Mapping(target = "rating", source = "id", qualifiedByName = "getEventRating")
    @Mapping(target = "initiator", source = "initiatorId", qualifiedByName = "getUserShortDto")
    EventFeignDto toEventFeignDto(Event event, @Context EventMapperContext context);

    /**
     * Преобразовать коллекцию объектов событий в коллекцию трансферных объектов, содержащих информацию о событиях.
     *
     * @param events коллекция объектов события.
     * @return коллекция трансферных объектов, содержащих информацию о событиях.
     */
    Collection<EventDto> toEventDtoCollection(Collection<Event> events, @Context EventMapperContext context);

    /**
     * Преобразовать коллекцию объектов событий в коллекцию трансферных объектов, содержащих краткую информацию о событиях.
     *
     * @param events коллекция объектов события.
     * @return коллекция трансферных объектов, содержащих краткую информацию о событиях.
     */
    Collection<EventShortDto> toEventShortDtoCollection(Collection<Event> events, @Context EventMapperContext context);

    Collection<EventFeignDto> toEventFeignDtoCollection(Collection<Event> events, @Context EventMapperContext context);

    @Named("getEventRating")
    static Double getEventRating(Long eventId, @Context EventMapperContext context) {
        if (context.getRatingEvents() == null) {
            return 0.0;
        }
        return context.getRatingEvents().getOrDefault(eventId, 0.0);
    }

    @Named("getUserShortDto")
    static UserShortDto getUserShortDto(Long initiatorId, @Context EventMapperContext context) {
        if (context.getUsers() == null) {
            return null;
        }
        UserDto user = context.getUsers().get(initiatorId);
        if (user != null) {
            return UserShortDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        }
        return null;
    }
}
