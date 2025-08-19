package ewm.comment;

import ewm.feign.EventClient;
import ewm.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.event.EventFeignDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.IncorrectlyException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.pageble.PageOffset;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * Feign клиент для сущности "Пользователь".
     */
    private final UserClient userClient;

    /**
     * Feign клиент данных для сущности "Событие".
     */
    private final EventClient eventClient;

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
        Collection<Comment> comments = null;
        CommentMapperContext context = null;
        if (params.getUserId() == null && params.getEventId() == null) {
            throw new IncorrectlyException("Необходимо передать или userId или eventId или оба вместе");
        } else if (params.getUserId() != null && params.getEventId() == null) {
            comments = commentRepository.findByUserId(params.getUserId(), pageable).getContent();

            context = CommentMapperContext.builder()
                    .users(getUsers(comments))
                    .events(getEvents(comments))
                    .build();

            return CommentMapper.INSTANCE.toCommentDtoCollection(comments, context);
        } else if (params.getUserId() == null) {
            comments = commentRepository.findByEventId(params.getEventId(), pageable).getContent();

            context = CommentMapperContext.builder()
                    .users(getUsers(comments))
                    .events(getEvents(comments))
                    .build();

            return CommentMapper.INSTANCE.toCommentDtoCollection(comments, context);
        } else {
            comments = commentRepository.findByUserIdAndEventId(params.getUserId(), params.getEventId(), pageable).getContent();

            context = CommentMapperContext.builder()
                    .users(getUsers(comments))
                    .events(getEvents(comments))
                    .build();

            return CommentMapper.INSTANCE.toCommentDtoCollection(comments, context);
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
        UserDto user = findUserById(params.getUserId());
        EventFeignDto event = getEvent(params.getEventId());
        CommentMapperParams mapperParams = CommentMapperParams.builder()
                .createCommentDto(params.getCreateCommentDto())
                .user(user)
                .event(event)
                .build();

        Comment comment = commentRepository.save(CommentMapper.INSTANCE.toCommentCreate(mapperParams));

        CommentMapperContext context = CommentMapperContext.builder()
                .users(getUsers(List.of(comment)))
                .events(getEvents(List.of(comment)))
                .build();

        return CommentMapper.INSTANCE.toCommentDto(comment, context);
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
        UserDto user = findUserById(params.getUserId());
        EventFeignDto event = getEvent(params.getEventId());
        if (!event.getId().equals(comment.getEventId())) {
            throw new IncorrectlyException(String.format("Комментарий не относится к событию с id = %d", event.getId()));
        }
        if (!user.getId().equals(comment.getUserId())) {
            throw new IncorrectlyException(String.format("Комментарий не принадлежит пользователю с id = %d", user.getId()));
        }
        CommentMapperParams mapperParams = CommentMapperParams.builder()
                .updateCommentDto(params.getUpdateCommentDto())
                .user(user)
                .event(event)
                .comment(comment)
                .build();

        comment = commentRepository.save(CommentMapper.INSTANCE.toCommentUpdate(mapperParams));

        CommentMapperContext context = CommentMapperContext.builder()
                .users(getUsers(List.of(comment)))
                .events(getEvents(List.of(comment)))
                .build();

        return CommentMapper.INSTANCE.toCommentDto(comment, context);
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
        UserDto user = findUserById(params.getUserId());
        EventFeignDto event = getEvent(params.getEventId());
        if (!event.getId().equals(comment.getEventId())) {
            throw new IncorrectlyException(String.format("Комментарий не относится к событию с id = %d", event.getId()));
        }
        if (!user.getId().equals(comment.getUserId())) {
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
    private UserDto findUserById(Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        return userClient.getUsers(userIds, 0, 1).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с id = %d", userId)));
    }

    private Map<Long, UserDto> getUsers(Collection<Comment> comments) {
        if (comments == null) {
            return new HashMap<>();
        }
        List<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();
        if (!userIds.isEmpty()) {
            return userClient.getUsers(userIds, 0, userIds.size()).stream()
                    .collect(Collectors.toMap(UserDto::getId, Function.identity()));
        }
        return new HashMap<>();
    }

    /**
     * Поиск события
     *
     * @param eventId ИД события
     * @return объект сущности "Событие"
     */
    private EventFeignDto getEvent(Long eventId) {
        return Optional.ofNullable(eventClient.findEventById(eventId))
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено событие с id = %d", eventId)));
    }

    private Map<Long, EventFeignDto> getEvents(Collection<Comment> comments) {
        if (comments == null) {
            return new HashMap<>();
        }
        List<Long> eventIds = comments.stream()
                .map(Comment::getEventId)
                .distinct()
                .toList();
        if (!eventIds.isEmpty()) {
            return eventClient.findEventCollection(eventIds).stream()
                    .collect(Collectors.toMap(EventFeignDto::getId, Function.identity()));
        }
        return new HashMap<>();
    }

    private Map<Long, EventFeignDto> getEvents(EventFeignDto event) {
        Map<Long, EventFeignDto> events = new HashMap<>();
        events.put(event.getId(), event);
        return events;
    }
}
