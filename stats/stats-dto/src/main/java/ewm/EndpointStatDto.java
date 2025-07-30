package ewm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект, содержащий статистику запросов.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class EndpointStatDto {
    /**
     * Идентификатор сервиса, в который был отправлен запрос.
     */
    private final String app;

    /**
     * Адрес запроса.
     */
    private final String uri;

    /**
     * Количество запросов.
     */
    private final Long hits;
}
