package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
public class EventFeignDto {
    /**
     * Уникальный идентификатор события.
     */
    private Long id;

    /**
     * Дата и время создания события.
     */
    private LocalDateTime createdOn;

    /**
     * Инициатор события.
     */
    private UserShortDto initiator;

    /**
     * Заголовок события.
     */
    private String title;

    /**
     * Краткое описание события.
     */
    private String annotation;

    /**
     * Полное описание события.
     */
    private String description;

    /**
     * Дата и время на которые намечено событие.
     */
    private LocalDateTime eventDate;

    /**
     * Категория события.
     */
    private CategoryDto category;

    /**
     * Широта и долгота места проведения события.
     */
    private Location location;

    /**
     * Дата и время публикации события.
     */
    private LocalDateTime publishedOn;

    /**
     * Признак, нужно ли оплачивать событие.
     */
    private boolean paid;

    /**
     * Ограничение на количество участников.
     * 0 - означает отсутствие ограничения.
     */
    private int participantLimit;

    /**
     * Признак, нужна ли пре-модерация заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     */
    private boolean requestModeration;

    /**
     * Количество одобренных заявок на участие в данном событии.
     */
    private int confirmedRequests;

    /**
     * Состояние события.
     */
    private EventState state;

    /**
     * Рейтинг события.
     */
    private double rating;
}
