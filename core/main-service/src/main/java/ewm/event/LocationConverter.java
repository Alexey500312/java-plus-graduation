package ewm.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class LocationConverter implements AttributeConverter<Location, String> {
    @Override
    public String convertToDatabaseColumn(Location attribute) {
        try {
            return new ObjectMapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Location convertToEntityAttribute(String dbData) {
        try {
            return new ObjectMapper().readValue(dbData, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
