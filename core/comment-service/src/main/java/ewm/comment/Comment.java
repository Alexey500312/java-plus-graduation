package ewm.comment;

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
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Событие по которому оставлен комментарий
     */
    @Column(name = "event_id", nullable = false)
    private Long eventId;

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
