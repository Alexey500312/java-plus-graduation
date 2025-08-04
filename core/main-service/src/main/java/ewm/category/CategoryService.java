package ewm.category;

import java.util.Collection;

/**
 * Контракт сервиса для сущности "Категория".
 */
public interface CategoryService {

    /**
     * Добавить новую категорию.
     *
     * @param createCategoryDto трансферный объект, содержащий данные для добавления новой категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    CategoryDto createCategory(CreateCategoryDto createCategoryDto);

    /**
     * Получить коллекцию категорий.
     *
     * @param from количество категорий, которое необходимо пропустить.
     * @param size количество категорий, которое необходимо получить.
     * @return коллекция категорий.
     */
    Collection<CategoryDto> getCategories(int from, int size);

    /**
     * Получить категорию.
     *
     * @param categoryId идентификатор категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    CategoryDto getCategory(long categoryId);

    /**
     * Обновить категорию.
     *
     * @param categoryId        идентификатор категории.
     * @param updateCategoryDto трансферный объект, содержащий данные для обновления категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    CategoryDto updateCategory(long categoryId, UpdateCategoryDto updateCategoryDto);

    /**
     * Удалить категорию.
     *
     * @param categoryId идентификатор категории.
     */
    void deleteCategory(long categoryId);
}
