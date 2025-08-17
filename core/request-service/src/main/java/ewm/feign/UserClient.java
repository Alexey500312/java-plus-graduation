package ewm.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.contract.UserOperations;

@FeignClient(name = "user-service", path = "/admin/users", fallback = UserFallback.class)
public interface UserClient extends UserOperations {
}
