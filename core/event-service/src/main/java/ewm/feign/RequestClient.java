package ewm.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.contract.RequestOperations;

@FeignClient(name = "request-service", path = "", fallback = RequestFallBack.class)
public interface RequestClient extends RequestOperations {
}
