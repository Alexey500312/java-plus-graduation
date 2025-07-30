package ewm.user;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект, содержащий информацию о пользователе.
 */
@Builder(toBuilder = true)
@Data
public class UserDto {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;
}
