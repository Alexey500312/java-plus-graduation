package ewm.comment;

import lombok.Builder;
import lombok.Data;

/**
 * Класс для передачи параметров в сервис комментария
 */
@Builder(toBuilder = true)
@Data
public class CommentParams {
    /**
     * ИД пользователя, оставившего комментарий
     */
    private Long userId;

    /**
     * ИД события
     */
    private Long eventId;

    /**
     * ИД комментария
     */
    private Long commentId;

    /**
     * Трансферный объект, содержащий данные для добавления комментария.
     */
    private CreateCommentDto createCommentDto;

    /**
     * Трансферный объект, содержащий данные для изменения комментария.
     */
    private UpdateCommentDto updateCommentDto;

    /**
     * Количество объектов, которые нужно пропустить
     */
    private int from;

    /**
     * Количество объектов в наборе
     */
    private int size;

    /**
     * Направление сортировки по дате создания (ASC, DESC)
     */
    private CommentSort sort;
}
