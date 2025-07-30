package ewm.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

/**
 * Mapper для моделей, содержащих информацию о комментарии.
 */
@Mapper
public interface CommentMapper {
    /**
     * Экземпляр mapper для моделей, содержащих информацию о комментарии.
     */
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Преобразовать параметры, содержащие данные для добавления нового комментария, в объект комментария.
     *
     * @param params объект для передачи параметров в mapper комментария, содержащий данные для добавления нового комментария.
     * @return объект комментарий.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createCommentDto.text", target = "text")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updated", ignore = true)
    Comment toCommentCreate(CommentMapperParams params);

    /**
     * Преобразовать параметры, содержащие данные для изменения комментария, в объект комментария.
     *
     * @param params объект для передачи параметров в mapper комментария, содержащий данные для изменения комментария.
     * @return объект комментарий.
     */
    @Mapping(source = "comment.id", target = "id")
    @Mapping(source = "updateCommentDto.text", target = "text")
    @Mapping(source = "comment.created", target = "created")
    @Mapping(target = "updated", expression = "java(java.time.LocalDateTime.now())")
    Comment toCommentUpdate(CommentMapperParams params);

    /**
     * Преобразовать объект комментарий в трансферный объект, содержащий данные о комментарии.
     *
     * @param comment объект комментария.
     * @return трансферный объект, содержащий данные о комментарии.
     */
    CommentDto toCommentDto(Comment comment);

    /**
     * Преобразовать коллекцию объектов событий в коллекцию трансферных объектов, содержащих информацию о событиях.
     *
     * @param comments коллекция объектов комментария.
     * @return коллекция трансферных объектов, содержащих информацию о комментариях.
     */
    Collection<CommentDto> toCommentDtoCollection(Collection<Comment> comments);
}
