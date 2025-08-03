package ewm.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для доступа к админской части API пользователей.
 */
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminUserController {
    /**
     * Сервис для сущности "Пользователь".
     */
    private final UserService userService;

    /**
     * Добавить нового пользователя.
     *
     * @param createUserDto трансферный объект, содержащий данные для добавления нового пользователя.
     * @return новый пользователь.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    /**
     * Получить коллекцию пользователей.
     *
     * @param userIds коллекция идентификаторов пользователей, которых надо получить.
     * @param from    количество пользователей, которое необходимо пропустить.
     * @param size    количество пользователей, которое необходимо получить.
     * @return коллекция пользователей.
     */
    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> userIds,
                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                        @RequestParam(defaultValue = "10") @Min(1) int size) {
        return userService.getUsers(userIds, from, size);
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
