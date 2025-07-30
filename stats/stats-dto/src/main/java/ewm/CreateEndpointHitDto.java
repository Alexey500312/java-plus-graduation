package ewm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Трансферный объект для сохранения информации о запросе.
 */
@Data
public class CreateEndpointHitDto {
    /**
     * Идентификатор сервиса, в который был отправлен запрос.
     */
    @NotBlank(message = "Идентификатор сервиса не может быть пустым")
    private final String app;

    /**
     * Адрес запроса.
     */
    @NotBlank(message = "Адрес запроса не может быть пустым")
    private final String uri;

    /**
     * IP-адрес пользователя, сделавшего запрос.
     */
    @NotBlank(message = "IP-адрес пользователя не может быть пустым")
    private final String ip;

    /**
     * Дата и время, когда был совершен запрос в формате "yyyy-MM-dd HH:mm:ss".
     */
    private final String timestamp;
}
