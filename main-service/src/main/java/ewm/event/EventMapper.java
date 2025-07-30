package ewm.event;

import ewm.client.StatsClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    @Mapping(target = "state", expression = "java(EventState.PENDING)")
    Event toEvent(CreateEventDto createEventDto);

    /**
     * Преобразовать объект события в трансферный объект, содержащий данные о событии.
     *
     * @param event объект события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @Mapping(target = "views", source = "id", qualifiedByName = "getEventViews")
    EventDto toEventDto(Event event);

    /**
     * Преобразовать объект события в трансферный объект, содержащий краткую информацию о событии.
     *
     * @param event объект события.
     * @return трансферный объект, содержащий краткую информацию о событии.
     */
    @Mapping(target = "views", source = "id", qualifiedByName = "getEventViews")
    EventShortDto toEventShortDto(Event event);

    /**
     * Преобразовать коллекцию объектов событий в коллекцию трансферных объектов, содержащих информацию о событиях.
     *
     * @param events коллекция объектов события.
     * @return коллекция трансферных объектов, содержащих информацию о событиях.
     */
    Collection<EventDto> toEventDtoCollection(Collection<Event> events);

    /**
     * Преобразовать коллекцию объектов событий в коллекцию трансферных объектов, содержащих краткую информацию о событиях.
     *
     * @param events коллекция объектов события.
     * @return коллекция трансферных объектов, содержащих краткую информацию о событиях.
     */
    Collection<EventShortDto> toEventShortDtoCollection(Collection<Event> events);

    @Named("getEventViews")
    static int getEventViews(Long eventId) {
        StatsClient statsClient = new StatsClient();

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);

        try {
            return Objects.requireNonNull(statsClient.getStats(start, end, List.of("/events/" + eventId), true).getBody()).size();
        } catch (Throwable ex) {
            return 0;
        }
    }
}
