package ewm.feign;

import ru.practicum.contract.UserOperations;
import ru.practicum.dto.user.UserDto;

import java.util.Collection;
import java.util.List;

public class UserFallback implements UserOperations {
    @Override
    public Collection<UserDto> getUsers(List<Long> userIds, int from, int size) {
        return List.of();
    }
}
