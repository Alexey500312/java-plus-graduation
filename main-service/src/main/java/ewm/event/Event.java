package ewm.event;

import ewm.category.Category;
import ewm.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Событие.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "events", schema = "public")
@ToString
public class Event {
    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания события.
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    /**
     * Инициатор события.
     */
    @JoinColumn(name = "initiator_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User initiator;

    /**
     * Заголовок события.
     */
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    /**
     * Краткое описание события.
     */
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    /**
     * Полное описание события.
     */
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    /**
     * Дата и время на которые намечено событие.
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Категория события.
     */
    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Category category;

    /**
     * Широта и долгота места проведения события.
     */
    @Column(name = "location", nullable = false)
    @Convert(converter = LocationConverter.class)
    private Location location;

    /**
     * Дата и время публикации события.
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    /**
     * Признак, нужно ли оплачивать событие.
     */
    @Column(name = "paid")
    private boolean paid;

    /**
     * Ограничение на количество участников.
     * 0 - означает отсутствие ограничения.
     */
    @Column(name = "participant_limit")
    private int participantLimit;

    /**
     * Признак, нужна ли пре-модерация заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     */
    @Column(name = "request_moderation")
    private boolean requestModeration;

    /**
     * Количество одобренных заявок на участие в данном событии.
     */
    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    /**
     * Состояние события.
     */
    @Column(name = "state")
    @Convert(converter = EventStateConverter.class)
    private EventState state;
}
