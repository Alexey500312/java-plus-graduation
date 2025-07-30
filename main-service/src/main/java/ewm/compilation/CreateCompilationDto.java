package ewm.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

/**
 * Трансферный объект, содержащий данные для создания подборки событий.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class CreateCompilationDto {
    /**
     * Заголовок подборки.
     */
    @Length(min = 1, message = "Наименование подборки не может быть меньше 1")
    @Length(max = 50, message = "Наименование подборки не может быть больше 50")
    @NotBlank
    private String title;

    /**
     * Список идентификаторов событий, входящих в подборку.
     */
    private Set<Long> events;

    /**
     * Признак, закреплена ли подборка на главной странице сайта.
     */
    private Boolean pinned;
}
