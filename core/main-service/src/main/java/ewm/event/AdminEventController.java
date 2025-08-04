package ewm.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Контроллер для админской части API событий.
 */
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class AdminEventController {
    /**
     * Сервис для сущности "Событие".
     */
    private final EventService eventService;

    /**
     * Получить коллекцию событий.
     *
     * @param users      коллекция идентификаторов пользователей, чьи события нужно найти
     * @param states     коллекция состояний в которых находятся искомые события.
     * @param categories коллекция идентификаторов категорий, в которых будет вестись поиск.
     * @param rangeStart дата и время, не раньше которых должно произойти событие.
     * @param rangeEnd   дата и время, не позже которых должно произойти событие.
     * @param from       количество событий, которое нужно пропустить.
     * @param size       количество событий, которое нужно извлечь.
     * @return коллекция событий.
     */
    @GetMapping
    public Collection<EventDto> getEvents(@RequestParam(required = false) Collection<Long> users,
                                          @RequestParam(required = false) Collection<EventState> states,
                                          @RequestParam(required = false) Collection<Long> categories,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        EventSearch eventSearch = EventSearch.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart != null ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .rangeEnd(rangeEnd != null ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .from(from)
                .size(size)
                .build();

        return eventService.getEvents(eventSearch);
    }

    /**
     * Обновить событие.
     *
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable @Positive Long eventId,
                                @RequestBody @Valid UpdateEventDto updateEventDto) {
        return eventService.updateEventByAdmin(eventId, updateEventDto);
    }
}
