package ewm.category;

import ewm.exception.NotFoundException;
import ewm.pageble.PageOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для сущности "Категория".
 */
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    /**
     * Хранилище данных для сущности "Категория".
     */
    private final CategoryRepository categoryRepository;

    /**
     * Добавить новую категорию.
     *
     * @param createCategoryDto трансферный объект, содержащий данные для добавления новой категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    @Override
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.save(CategoryMapper.INSTANCE.toCategory(createCategoryDto)));
    }

    /**
     * Получить коллекцию категорий.
     *
     * @param from количество категорий, которое необходимо пропустить.
     * @param size количество категорий, которое необходимо получить.
     * @return коллекция категорий.
     */
    @Override
    public Collection<CategoryDto> getCategories(int from, int size) {
        return CategoryMapper.INSTANCE.toCategoryDtoCollection(categoryRepository.findAll(PageOffset.of(from, size, Sort.by("id").ascending())).getContent());
    }

    /**
     * Получить категорию по её идентификатору.
     *
     * @param categoryId идентификатор категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", categoryId)));
    }

    /**
     * Получить категорию.
     *
     * @param categoryId идентификатор категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    @Override
    public CategoryDto getCategory(long categoryId) {
        return CategoryMapper.INSTANCE.toCategoryDto(getCategoryById(categoryId));
    }

    /**
     * Обновить категорию.
     *
     * @param categoryId        идентификатор категории.
     * @param updateCategoryDto трансферный объект, содержащий данные для обновления категории.
     * @return трансферный объект, содержащий данные о категории.
     */
    @Override
    public CategoryDto updateCategory(long categoryId, UpdateCategoryDto updateCategoryDto) {
        Category category = getCategoryById(categoryId);
        category.setName(updateCategoryDto.getName());

        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.save(category));
    }

    /**
     * Удалить категорию.
     *
     * @param categoryId идентификатор категории.
     */
    @Override
    public void deleteCategory(long categoryId) {
        categoryRepository.delete(getCategoryById(categoryId));
    }
}
