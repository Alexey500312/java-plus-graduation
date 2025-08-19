package ewm.event;

import ewm.client.StatsClient;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserDto;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class EventMapperContext {
    private StatsClient statsClient;

    private Map<Long, UserDto> users;
}
