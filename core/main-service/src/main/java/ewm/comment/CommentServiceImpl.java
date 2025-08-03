package ewm.comment;

import ewm.event.Event;
import ewm.event.EventRepository;
import ewm.exception.IncorrectlyException;
import ewm.exception.NotFoundException;
import ewm.pageble.PageOffset;
import ewm.user.User;
import ewm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Сервис для сущности "Комментарий".
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    /**
     * Хранилище данных для сущности "Комментарий".
     */
    private final CommentRepository commentRepository;

    /**
     * Хранилище данных для сущности "Пользователь".
     */
    private final UserRepository userRepository;

    /**
     * Хранилище данных для сущности "Событие".
     */
    private final EventRepository eventRepository;

    /**
     * Поиск комментариев по переданным параметрам
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    @Override
    public Collection<CommentDto> getComments(CommentParams params) {
        Sort sort = switch (params.getSort()) {
            case ASC -> Sort.by(Sort.Order.asc("created"));
            case DESC -> Sort.by(Sort.Order.desc("created"));
        };
        Pageable pageable = PageOffset.of(params.getFrom(), params.getSize(), sort);
        if (params.getUserId() == null && params.getEventId() == null) {
            throw new IncorrectlyException("Необходимо передать или userId или eventId или оба вместе");
        } else if (params.getUserId() != null && params.getEventId() == null) {
            return CommentMapper.INSTANCE.toCommentDtoCollection(
                    commentRepository.findByUserId(params.getUserId(), pageable).getContent());
        } else if (params.getUserId() == null) {
            return CommentMapper.INSTANCE.toCommentDtoCollection(
                    commentRepository.findByEventId(params.getEventId(), pageable).getContent());
        } else {
            return CommentMapper.INSTANCE.toCommentDtoCollection(
                    commentRepository.findByUserIdAndEventId(params.getUserId(), params.getEventId(), pageable).getContent());
        }
    }

    /**
     * Добавить новый комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    @Override
    @Transactional
    public CommentDto createComment(CommentParams params) {
        User user = findUserById(params.getUserId());
        Event event = findEventById(params.getEventId());
        CommentMapperParams mapperParams = CommentMapperParams.builder()
                .createCommentDto(params.getCreateCommentDto())
                .user(user)
                .event(event)
                .build();
        return CommentMapper.INSTANCE.toCommentDto(
                commentRepository.save(CommentMapper.INSTANCE.toCommentCreate(mapperParams)));
    }

    /**
     * Изменить комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     * @return трансферный объект, содержащий данные о комментарии
     */
    @Override
    @Transactional
    public CommentDto updateComment(CommentParams params) {
        Comment comment = findById(params.getCommentId());
        User user = findUserById(params.getUserId());
        Event event = findEventById(params.getEventId());
        if (!event.getId().equals(comment.getEvent().getId())) {
            throw new IncorrectlyException(String.format("Комментарий не относится к событию с id = %d", event.getId()));
        }
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new IncorrectlyException(String.format("Комментарий не принадлежит пользователю с id = %d", user.getId()));
        }
        CommentMapperParams mapperParams = CommentMapperParams.builder()
                .updateCommentDto(params.getUpdateCommentDto())
                .user(user)
                .event(event)
                .comment(comment)
                .build();
        return CommentMapper.INSTANCE.toCommentDto(
                commentRepository.save(CommentMapper.INSTANCE.toCommentUpdate(mapperParams)));
    }

    /**
     * Удалить комментарий
     *
     * @param params объект для передачи параметров в сервис комментария
     */
    @Override
    @Transactional
    public void deleteComment(CommentParams params) {
        Comment comment = findById(params.getCommentId());
        User user = findUserById(params.getUserId());
        Event event = findEventById(params.getEventId());
        if (!event.getId().equals(comment.getEvent().getId())) {
            throw new IncorrectlyException(String.format("Комментарий не относится к событию с id = %d", event.getId()));
        }
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new IncorrectlyException(String.format("Комментарий не принадлежит пользователю с id = %d", user.getId()));
        }
        commentRepository.delete(comment);
    }

    /**
     * Поиск комментария
     *
     * @param commentId ИД комментария
     * @return объект сущности "Комментарий"
     */
    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден комментарий с id = %d", commentId)));
    }

    /**
     * Поиск пользователя
     *
     * @param userId ИД пользователя
     * @return объект сущности "Пользователь"
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с id = %d", userId)));
    }

    /**
     * Поиск события
     *
     * @param eventId ИД события
     * @return объект сущности "Событие"
     */
    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено событие с id = %d", eventId)));
    }
}
