package ewm.comment;

import ewm.event.Event;
import ewm.user.User;
import lombok.Builder;
import lombok.Data;

/**
 * Класс для передачи параметров в mapper комментария
 */
@Builder(toBuilder = true)
@Data
public class CommentMapperParams {
    /**
     * Комментарий (используется при изменении комментария)
     */
    private Comment comment;

    /**
     * Пользователь, оставивший комментарий (используется при добавлении и изменении комментария)
     */
    private User user;

    /**
     * Событие (используется при добавлении и изменении комментария)
     */
    private Event event;

    /**
     * Трансферный объект, содержащий данные для добавления комментария.
     */
    private CreateCommentDto createCommentDto;

    /**
     * Трансферный объект, содержащий данные для изменения комментария.
     */
    private UpdateCommentDto updateCommentDto;
}
