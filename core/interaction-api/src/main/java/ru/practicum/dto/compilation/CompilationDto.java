package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventDto;

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
    private Collection<EventDto> events;

    /**
     * Признак, закреплена ли подборка на главной странице сайта.
     */
    private boolean pinned;
}
