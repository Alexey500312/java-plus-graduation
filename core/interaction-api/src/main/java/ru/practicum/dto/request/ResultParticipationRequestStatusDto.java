package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Builder(toBuilder = true)
@Data
public class ResultParticipationRequestStatusDto {
    Collection<ParticipationRequestDto> confirmedRequests;

    Collection<ParticipationRequestDto> rejectedRequests;
}
