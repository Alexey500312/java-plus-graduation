package ewm.compilation;

import ewm.event.Event;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * Трансферный объект, содержащий данные о подборке событий.
 */
@Builder(toBuilder = true)
@Data
public class CompilationDto {
    /**
     * Уникальный идентификатор подборки.
     */
    private Long id;

    /**
     * Заголовок подборки.
     */
    private String title;

    /**
     * Коллекция событий, входящих в подборку.
     */
    private Collection<Event> events;

    /**
     * Признак, закреплена ли подборка на главной странице сайта.
     */
    private boolean pinned;
}
