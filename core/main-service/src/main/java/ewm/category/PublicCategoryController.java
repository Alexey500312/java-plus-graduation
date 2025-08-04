package ewm.category;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для доступа к публичной части API категорий.
 */
@RequestMapping("/categories")
@RequiredArgsConstructor
@RestController
@Validated
public class PublicCategoryController {
    /**
     * Сервис для сущности "Категория".
     */
    private final CategoryService categoryService;

    /**
     * Получить коллекцию категорий.
     *
     * @param from количество категорий, которое необходимо пропустить.
     * @param size количество категорий, которое необходимо получить.
     * @return коллекция категорий.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return categoryService.getCategories(from, size);
    }

    /**
     * Получить категорию.
     *
     * @param categoryId идентификатор категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable Long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}