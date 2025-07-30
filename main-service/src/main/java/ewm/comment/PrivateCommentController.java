package ewm.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для приватной части API комментариев.
 */
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@RestController
@Validated
public class PrivateCommentController {
    /**
     * Сервис для сущности "Комментарий".
     */
    private final CommentService commentService;

    /**
     * Добавить новый комментарий
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param eventId ИД события
     * @param createCommentDto трансферный объект, содержащий данные для добавления комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable @Positive Long userId,
                                    @RequestParam @Positive Long eventId,
                                    @RequestBody @Valid CreateCommentDto createCommentDto) {
        CommentParams params = CommentParams.builder()
                .userId(userId)
                .eventId(eventId)
                .createCommentDto(createCommentDto)
                .build();
        return commentService.createComment(params);
    }

    /**
     *
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param commentId ИД комментария
     * @param eventId ИД события
     * @param updateCommentDto трансферный объект, содержащий данные для изменения комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long commentId,
                                    @RequestParam @Positive Long eventId,
                                    @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        CommentParams params = CommentParams.builder()
                .commentId(commentId)
                .userId(userId)
                .eventId(eventId)
                .updateCommentDto(updateCommentDto)
                .build();
        return commentService.updateComment(params);
    }

    /**
     *
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param commentId ИД комментария
     * @param eventId ИД события
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commentId,
                              @RequestParam @Positive Long eventId) {
        CommentParams params = CommentParams.builder()
                .commentId(commentId)
                .userId(userId)
                .eventId(eventId)
                .build();
        commentService.deleteComment(params);
    }
}
