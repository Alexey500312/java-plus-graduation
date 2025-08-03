package ewm.comment;

import ewm.event.Event;
import ewm.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Комментарий
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "comments")
@ToString
public class Comment {
    /**
     * Уникальный идентификатор комментария
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь оставивший комментарий
     */
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    /**
     * Событие по которому оставлен комментарий
     */
    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Event event;

    /**
     * Текст комментария
     */
    private String text;

    /**
     * Дата и время создания комментария
     */
    private LocalDateTime created;

    /**
     * Дата и время изменения комментария
     */
    private LocalDateTime updated;
}
