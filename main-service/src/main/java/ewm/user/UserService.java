package ewm.user;

import java.util.Collection;

/**
 * Контракт сервиса для сущности "Пользователь".
 */
public interface UserService {
    /**
     * Добавить нового пользователя.
     *
     * @param createUserDto трансферный объект, содержащий данные для добавления нового пользователя.
     * @return новый пользователь.
     */
    UserDto createUser(CreateUserDto createUserDto);

    /**
     * Получить коллекцию пользователей.
     *
     * @param userIds коллекция идентификаторов пользователей, которых надо получить.
     * @param from    количество пользователей, которое необходимо пропустить.
     * @param size    количество пользователей, которое необходимо получить.
     * @return коллекция пользователей.
     */
    Collection<UserDto> getUsers(Collection<Long> userIds, int from, int size);

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    void deleteUser(long userId);
}
