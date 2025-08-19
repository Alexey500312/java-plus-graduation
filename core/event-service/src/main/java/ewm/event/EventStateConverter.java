package ewm.event;

import jakarta.persistence.AttributeConverter;
import ru.practicum.dto.event.EventState;

public class EventStateConverter  implements AttributeConverter<EventState, String> {
    @Override
    public String convertToDatabaseColumn(EventState attribute) {
        return attribute.name();
    }

    @Override
    public EventState convertToEntityAttribute(String dbData) {
        return EventState.valueOf(dbData);
    }
}
