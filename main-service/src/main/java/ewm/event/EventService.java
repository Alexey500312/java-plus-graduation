package ewm.event;

import java.util.Collection;

/**
 * Контракт сервиса для сущности "Событие".
 */
public interface EventService {
    /**
     * Добавить новое событие.
     *
     * @param userId         идентификатор текущего пользователя.
     * @param createEventDto трансферный объект, содержащий данные для добавления нового события.
     * @return трансферный объект, содержащий данные о событии.
     */
    EventDto createEvent(Long userId, CreateEventDto createEventDto);

    /**
     * Получить коллекцию событий.
     *
     * @param userId идентификатор пользователя.
     * @param from   количество событий, которое необходимо пропустить.
     * @param size   количество событий, которое необходимо получить.
     * @return коллекция событий.
     */
    Collection<EventDto> getEvents(Long userId, int from, int size);

    /**
     * Получить коллекцию событий.
     *
     * @param search объект, содержащий параметры поиска событий.
     * @return коллекция трансферных объектов, содержащий краткую информацию по событиям.
     */
    Collection<EventDto> getEvents(EventSearch search);

    /**
     * Получить коллекцию событий.
     *
     * @param search объект, содержащий параметры поиска событий.
     * @return коллекция трансферных объектов, содержащий краткую информацию по событиям.
     */
    Collection<EventShortDto> getPublishedEvents(EventSearch search);

    /**
     * Получить событие по его идентификатору.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return событие.
     */
    EventDto getEventById(Long userId, Long eventId);

    /**
     * Получить опубликованное событие по его идентификатору.
     *
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    EventDto getPublishedEventById(Long eventId);

    /**
     * Обновить событие.
     *
     * @param userId         идентификатор текущего пользователя.
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    EventDto updateEventByUser(Long userId, Long eventId, UpdateEventDto updateEventDto);

    /**
     * Обновить событие.
     *
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    EventDto updateEventByAdmin(Long eventId, UpdateEventDto updateEventDto);
}
