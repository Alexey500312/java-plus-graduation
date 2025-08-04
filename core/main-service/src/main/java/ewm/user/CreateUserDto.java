package ewm.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Трансферный объект, содержащий данные для добавления нового пользователя.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class CreateUserDto {
    /**
     * Имя пользователя.
     */
    @Length(min = 2, message = "Имя пользователя не может быть меньше 2 символов")
    @Length(max = 250, message = "Имя пользователя не может быть больше 250 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    /**
     * Адрес электронной почты пользователя.
     */
    @Email(message = "Email пользователя должен корректным")
    @Length(min = 6, message = "Email пользователя не может быть меньше 6 символов")
    @Length(max = 254, message = "Email пользователя не может быть больше 254 символов")
    @NotBlank(message = "Email пользователя не может быть пустым")
    private String email;
}
