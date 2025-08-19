package ewm.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.contract.EventOperations;

@FeignClient(name = "event-service", path = "/private/events", fallback = EventFallback.class)
public interface EventClient extends EventOperations {
}
