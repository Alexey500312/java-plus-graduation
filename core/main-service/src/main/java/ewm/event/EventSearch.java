package ewm.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Параметры поиска событий.
 */
@Builder(toBuilder = true)
@Data
public class EventSearch {
    /**
     * Текст для поиска в содержимом аннотации и подробном описании события.
     */
    private String text;

    /**
     * Коллекция идентификаторов пользователей, чьи события нужно найти
     */
    private Collection<Long> users;

    /**
     * Коллекция состояний в которых находятся искомые события.
     */
    private Collection<EventState> states;

    /**
     * Коллекция идентификаторов категорий, в которых будет вестись поиск.
     */
    private Collection<Long> categories;

    /**
     * Поиск только платных/бесплатных событий.
     */
    private Boolean paid;

    /**
     * Дата и время, не раньше которых должно произойти событие.
     */
    private LocalDateTime rangeStart;

    /**
     * Дата и время, не позже которых должно произойти событие.
     */
    private LocalDateTime rangeEnd;

    /**
     * Только события, у которых не исчерпан лимит запросов на участие.
     */
    private boolean onlyAvailable;

    /**
     * Способ сортировки событий.
     */
    private EventSort sort;

    /**
     * Количество событий, которое нужно пропустить.
     */
    private int from;

    /**
     * Количество событий, которое нужно извлечь.
     */
    private int size;
}
