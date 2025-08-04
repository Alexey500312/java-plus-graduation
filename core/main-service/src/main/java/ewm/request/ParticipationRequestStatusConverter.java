package ewm.request;

import jakarta.persistence.AttributeConverter;

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
