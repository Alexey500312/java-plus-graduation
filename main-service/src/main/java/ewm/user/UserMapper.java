package ewm.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

/**
 * Маппер для моделей, содержащий информацию о пользователе.
 */
@Mapper
public interface UserMapper {
    /**
     * Экземпляр маппера для моделей, содержащих информацию о пользователе.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразовать трансферный объект, содержащий данные для добавления нового пользователя, в объект пользователя.
     *
     * @param createUserDto трансферный объект, содержащий данные для добавления нового пользователя.
     * @return объект пользователя.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(createUserDto.getName() != null ? createUserDto.getName().trim() : null)")
    @Mapping(target = "email", expression = "java(createUserDto.getEmail() != null ? createUserDto.getEmail().trim() : null)")
    User toUser(CreateUserDto createUserDto);

    /**
     * Преобразовать объект пользователя в трансферный объект, содержащий информацию о пользователе.
     *
     * @param user объект пользователя.
     * @return трансферный объект, содержащий информацию о пользователе.
     */
    UserDto toUserDto(User user);

    /**
     * Преобразовать объект пользователя в трансферный объект, содержащий краткую информацию о пользователе.
     *
     * @param user объект пользователя.
     * @return трансферный объект, содержащий краткую информацию о пользователе.
     */
    UserShortDto toUserShortDto(User user);

    /**
     * Преобразовать коллекцию объектов пользователей в коллекцию трансферных объектов, содержащих информацию о пользователях.
     *
     * @param users коллекция объектов пользователей.
     * @return коллекция трансферных объектов, содержащих информацию о пользователях.
     */
    Collection<UserDto> toUserDtoCollection(Collection<User> users);
}
