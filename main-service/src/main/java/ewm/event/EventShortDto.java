package ewm.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.category.CategoryDto;
import ewm.user.UserShortDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Краткая информация о событии.
 */
@Builder(toBuilder = true)
@Data
public class EventShortDto {
    /**
     * Уникальный идентификатор события.
     */
    private Long id;

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
     * Дата и время на которые намечено событие.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    /**
     * Категория события.
     */
    private CategoryDto category;

    /**
     * Признак, нужно ли оплачивать событие.
     */
    private boolean paid;

    /**
     * Количество одобренных заявок на участие в данном событии.
     */
    private int confirmedRequests;

    /**
     * Количество просмотров события.
     */
    private int views;
}
