package ewm.event;

import jakarta.persistence.AttributeConverter;

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
