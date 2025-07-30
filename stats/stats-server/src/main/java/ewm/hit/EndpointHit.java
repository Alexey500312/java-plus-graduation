package ewm.hit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "hits")
@ToString
public class EndpointHit {
    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор сервиса, в который был отправлен запрос.
     */
    @Column(name = "app")
    private String app;

    /**
     * Адрес запроса.
     */
    @Column(name = "uri")
    private String uri;

    /**
     * IP-адрес пользователя, сделавшего запрос.
     */
    @Column(name = "ip")
    private String ip;

    /**
     * Дата и время, когда был совершен запрос в формате "yyyy-MM-dd HH:mm:ss".
     */
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
