package ewm.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.Collection;

@Mapper
public interface ParticipationRequestMapper {
    ParticipationRequestMapper INSTANCE = Mappers.getMapper(ParticipationRequestMapper.class);

    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "requester", source = "requesterId")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    Collection<ParticipationRequestDto> toParticipationRequestDtoCollection(Collection<ParticipationRequest> requests);
}
