package ewm.category;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект, содержащий данные о категории.
 */
@Builder(toBuilder = true)
@Data
public class CategoryDto {
    /**
     * Уникальный идентификатор категории.
     */
    private Long id;

    /**
     * Название категории.
     */
    private String name;
}
