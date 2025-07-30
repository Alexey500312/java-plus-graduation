package ewm.event;

import ewm.CreateEndpointHitDto;
import ewm.client.StatsClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * Клиент сервиса статистики.
     */
    private final StatsClient statsClient;

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
        try {
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
        } finally {
            try {
                statsClient.sendHit(new CreateEndpointHitDto("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    /**
     * Получить информацию об опубликованном событии.
     *
     * @param eventId идентификатор события.
     * @return трансферный объект, содержащий данные о событии.
     */
    @GetMapping("/{eventId}")
    public EventDto getPublishedEventById(@PathVariable @Positive Long eventId, HttpServletRequest request) {
        try {
            return eventService.getPublishedEventById(eventId);
        } finally {
            try {
                statsClient.sendHit(new CreateEndpointHitDto("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
