package ewm.request;

import jakarta.persistence.AttributeConverter;
import ru.practicum.dto.request.ParticipationRequestStatus;

public class ParticipationRequestStatusConverter implements AttributeConverter<ParticipationRequestStatus, String> {
    @Override
    public String convertToDatabaseColumn(ParticipationRequestStatus participationRequestStatus) {
        return participationRequestStatus.name();
    }

    @Override
    public ParticipationRequestStatus convertToEntityAttribute(String s) {
        return ParticipationRequestStatus.valueOf(s);
    }
}
