package ewm.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * Трансферный объект, содержащий данные для обновления события.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class UpdateEventDto {
    /**
     * Заголовок события.
     */
    @Length(min = 3, message = "Заголовок события не может быть меньше 3 символов")
    @Length(max = 120, message = "Заголовок события не может быть больше 120 символов")
    private String title;

    /**
     * Краткое описание события.
     */
    @Length(min = 20, message = "Краткое описание события не может быть меньше 20 символов")
    @Length(max = 2000, message = "Краткое описание события не может быть больше 2000 символов")
    private String annotation;

    /**
     * Полное описание события.
     */
    @Length(min = 20, message = "Полное описание события не может быть меньше 20 символов")
    @Length(max = 7000, message = "Полное описание события не может быть больше 7000 символов")
    private String description;

    /**
     * Дата и время на которые намечено событие.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    /**
     * Идентификатор категории, к которой относится событие.
     */
    private Long category;

    /**
     * Широта и долгота места проведения события.
     */
    private Location location;

    /**
     * Признак, нужно ли оплачивать событие.
     */
    private Boolean paid;

    /**
     * Ограничение на количество участников.
     * 0 - означает отсутствие ограничения.
     */
    @PositiveOrZero
    private Integer participantLimit;

    /**
     * Признак, нужна ли пре-модерация заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

    /**
     * Изменение состояния объекта.
     */
    private EventStateAction stateAction;
}
