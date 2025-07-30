package ewm.category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Контракт хранилища данных для сущности "Категория".
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
