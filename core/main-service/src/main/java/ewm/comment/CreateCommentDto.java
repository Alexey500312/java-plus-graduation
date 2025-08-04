package ewm.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Трансферный объект, содержащий данные для добавления комментария.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class CreateCommentDto {
    /**
     * Текст комментария
     */
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Length(max = 4000, message = "Текст комментария не может быть длиннее 4000 символов")
    private String text;
}
