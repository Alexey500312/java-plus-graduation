package ewm.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Трансферный объект, содержащий данные для добавления новой категории.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class CreateCategoryDto {
    /**
     * Название категории.
     */
    @Length(min = 2, message = "Название категории не может быть меньше 2 символов")
    @Length(max = 50, message = "Название категории не может быть больше 50 символов")
    @NotBlank(message = "Название категории не может быть пустым")
    private String name;
}
