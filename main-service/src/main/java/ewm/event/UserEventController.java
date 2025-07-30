package ewm.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для пользовательской части API событий.
 */
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@RestController
@Validated
public class UserEventController {
    /**
     * Сервис для сущности "Событие".
     */
    private final EventService eventService;

    /**
     * Добавить новое событие.
     *
     * @param userId         идентификатор текущего пользователя.
     * @param createEventDto трансферный объект, содержащий данные для добавления нового события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable @Positive Long userId,
                                @RequestBody @Valid CreateEventDto createEventDto) {
        return eventService.createEvent(userId, createEventDto);
    }

    /**
     * Получить коллекцию событий
     *
     * @param userId идентификатор текущего пользователя.
     * @param from   количество событий, которое необходимо пропустить.
     * @param size   количество событий, которое необходимо получить.
     * @return трансферный объект, содержащий данные о событии.
     */
    @GetMapping
    public Collection<EventDto> getEvents(@PathVariable @Positive Long userId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getEvents(userId, from, size);
    }

    /**
     * Получить информацию о событии.
     *
     * @param userId  идентификатор текущего пользователя.
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long userId,
                                 @PathVariable @Positive Long eventId) {
        return eventService.getEventById(userId, eventId);
    }

    /**
     * Обновить событие.
     *
     * @param userId         идентификатор пользователя.
     * @param eventId        идентификатор события.
     * @param updateEventDto трансферный объект, содержащий данные для обновления события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long userId,
                                @PathVariable @Positive Long eventId,
                                @RequestBody @Valid UpdateEventDto updateEventDto) {
        return eventService.updateEventByUser(userId, eventId, updateEventDto);
    }
}
