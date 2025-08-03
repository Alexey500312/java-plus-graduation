package ewm.category;

import jakarta.persistence.*;
import lombok.*;

/**
 * Категория.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "categories", schema = "public")
@ToString
public class Category {
    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название категории.
     */
    @Column(name = "name", nullable = false)
    private String name;
}
