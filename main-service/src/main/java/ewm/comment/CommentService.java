package ewm.comment;

import java.util.Collection;

/**
 * Контракт сервиса для сущности "Комментарий".
 */
public interface CommentService {
    /**
     * Поиск комментариев по переданным параметрам
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    Collection<CommentDto> getComments(CommentParams params);

    /**
     * Добавить новый комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    CommentDto createComment(CommentParams params);

    /**
     * Изменить комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    CommentDto updateComment(CommentParams params);

    /**
     * Удалить комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     */
    void deleteComment(CommentParams params);
}
