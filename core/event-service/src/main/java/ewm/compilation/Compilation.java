package ewm.compilation;

import ewm.event.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Подборка событий.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "compilations", schema = "public")
public class Compilation {
    /**
     * Уникальный идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заголовок подборки.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Список событий, входящих в подборку.
     */
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ManyToMany
    private Collection<Event> events;

    /**
     * Признак, закреплена ли подборка на главной странице сайта.
     */
    @Column(name = "pinned")
    private boolean pinned;
}
