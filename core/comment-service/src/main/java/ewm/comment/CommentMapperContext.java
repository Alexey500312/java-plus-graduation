package ewm.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventFeignDto;
import ru.practicum.dto.user.UserDto;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class CommentMapperContext {
    private Map<Long, UserDto> users;

    private Map<Long, EventFeignDto> events;
}
