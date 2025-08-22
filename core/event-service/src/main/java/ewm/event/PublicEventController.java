package ewm.event;

import ewm.client.CollectorClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Контроллер для пользовательской части API событий.
 */
@RequestMapping("/events")
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class PublicEventController {
    /**
     * Сервис для сущности "Событие".
     */
    private final EventService eventService;

    /**
     * Grpc клиент для регистрации активности пользователя по событиям.
     */
    private final CollectorClient collectorClient;

    /**
     * Получить коллекцию событий.
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события.
     * @param categories    коллекция идентификаторов категорий, в которых будет вестись поиск.
     * @param paid          поиск только платных/бесплатных событий.
     * @param rangeStart    дата и время, не раньше которых должно произойти событие.
     * @param rangeEnd      дата и время, не позже которых должно произойти событие.
     * @param onlyAvailable только события, у которых не исчерпан лимит запросов на участие.
     * @param sort          способ сортировки событий.
     * @param from          количество событий, которое нужно пропустить.
     * @param size          количество событий, которое нужно извлечь.
     * @param request       HTTP-запрос.
     * @return коллекция событий.
     */
    @GetMapping
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Collection<@Positive Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(required = false) EventSort sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        EventSearch eventSearch = EventSearch.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart != null ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .rangeEnd(rangeEnd != null ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        return eventService.getPublishedEvents(eventSearch);
    }

    /**
     * Получить информацию об опубликованном событии.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @GetMapping("/{eventId}")
    public EventDto getPublishedEventById(@RequestHeader("X-EWM-USER-ID") @Positive Long userId,
                                          @PathVariable @Positive Long eventId,
                                          HttpServletRequest request) {
        EventDto result = eventService.getPublishedEventById(eventId);

        try {
            collectorClient.saveView(userId, eventId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return result;
    }

    @GetMapping("/recommendations")
    public Collection<EventShortDto> getRecommendations(@RequestHeader("X-EWM-USER-ID") @Positive Long userId,
                                                        @RequestParam(name = "maxResults", defaultValue = "10") Integer maxResults,
                                                        HttpServletRequest request) {
        return eventService.getRecommendations(userId, maxResults);
    }

    @GetMapping("/{eventId}/similar")
    public Collection<EventShortDto> getSimilarEvents(@RequestHeader("X-EWM-USER-ID") @Positive Long userId,
                                                      @PathVariable("eventId") Long eventId,
                                                      @RequestParam(name = "maxResults", defaultValue = "10") Integer maxResults,
                                                      HttpServletRequest request) {
        return eventService.getSimilarEvents(userId, eventId, maxResults);
    }

    @PutMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putLike(@RequestHeader("X-EWM-USER-ID") @Positive Long userId,
                        @PathVariable @Positive Long eventId,
                        HttpServletRequest request) {
        eventService.putLike(userId, eventId);
        try {
            collectorClient.saveLike(userId, eventId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
