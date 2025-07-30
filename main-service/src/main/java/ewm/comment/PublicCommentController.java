package ewm.comment;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для публичной части API комментариев.
 */
@RequestMapping("/comments")
@RequiredArgsConstructor
@RestController
@Validated
public class PublicCommentController {
    /**
     * Сервис для сущности "Комментарий".
     */
    private final CommentService commentService;

    /**
     * Потск комментариев по параметрам
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param eventId ИД события
     * @param from количество объектов, которые нужно пропустить
     * @param size Количество объектов в наборе
     * @return коллекция комментариев
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getComments(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) Long eventId,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(value = "size", defaultValue = "10") @PositiveOrZero int size,
                                              @RequestParam(value = "sort", defaultValue = "ASC") CommentSort sort) {
        CommentParams params = CommentParams.builder()
                .userId(userId)
                .eventId(eventId)
                .from(from)
                .size(size)
                .sort(sort)
                .build();
        return commentService.getComments(params);
    }
}
