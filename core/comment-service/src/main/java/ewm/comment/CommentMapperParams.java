package ewm.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.comment.CreateCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.dto.event.EventFeignDto;
import ru.practicum.dto.user.UserDto;

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
    private UserDto user;

    /**
     * Событие (используется при добавлении и изменении комментария)
     */
    private EventFeignDto event;

    /**
     * Трансферный объект, содержащий данные для добавления комментария.
     */
    private CreateCommentDto createCommentDto;

    /**
     * Трансферный объект, содержащий данные для изменения комментария.
     */
    private UpdateCommentDto updateCommentDto;
}
