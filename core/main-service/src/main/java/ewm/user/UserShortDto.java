package ewm.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект, содержащий краткую информацию о пользователе.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserShortDto {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;
}
