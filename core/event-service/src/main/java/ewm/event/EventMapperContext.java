package ewm.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserDto;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class EventMapperContext {
    private Map<Long, Double> ratingEvents;

    private Map<Long, UserDto> users;
}
