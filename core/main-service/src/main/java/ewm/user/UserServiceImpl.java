package ewm.user;

import ewm.exception.NotFoundException;
import ewm.pageble.PageOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для сущности "Пользователь".
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    /**
     * Хранилище данных для сущности "Пользователь".
     */
    private final UserRepository userRepository;

    /**
     * Добавить нового пользователя.
     *
     * @param createUserDto трансферный объект, содержащий данные для добавления нового пользователя.
     * @return новый пользователь.
     */
    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        return UserMapper.INSTANCE.toUserDto(userRepository.save(UserMapper.INSTANCE.toUser(createUserDto)));
    }

    /**
     * Получить коллекцию пользователей.
     *
     * @param userIds коллекция идентификаторов пользователей, которых надо получить.
     * @param from    количество пользователей, которое необходимо пропустить.
     * @param size    количество пользователей, которое необходимо получить.
     * @return коллекция пользователей.
     */
    @Override
    public Collection<UserDto> getUsers(Collection<Long> userIds, int from, int size) {
        if (userIds != null && !userIds.isEmpty()) {
            return UserMapper.INSTANCE.toUserDtoCollection(userRepository.findAllById(userIds));
        }

        return UserMapper.INSTANCE.toUserDtoCollection(userRepository.findAll(PageOffset.of(from, size)).getContent());
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @Override
    public void deleteUser(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }

        userRepository.deleteById(userId);
    }
}
