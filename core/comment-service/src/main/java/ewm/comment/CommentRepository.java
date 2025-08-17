package ewm.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Контракт хранилища данных для сущности "Комментарии".
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Поиск комментариев по событию
     *
     * @param eventId ИД события
     * @param pageable пагинация
     * @return страница с найденными комментариями
     */
    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    /**
     * Поиск комментариев по пользователю, оставившему комментарий
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param pageable пагинация
     * @return страница с найденными комментариями
     */
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    /**
     * Поиск комментариев по пользователю, оставившему комментарий и событию
     *
     * @param userId ИД пользователя, оставившего комментарий
     * @param eventId ИД события
     * @param pageable пагинация
     * @return страница с найденными комментариями
     */
    Page<Comment> findByUserIdAndEventId(Long userId, Long eventId, Pageable pageable);
}
