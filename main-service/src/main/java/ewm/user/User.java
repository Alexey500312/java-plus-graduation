package ewm.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * Пользователь.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "users", schema = "public")
@ToString
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "email", nullable = false)
    private String email;
}
