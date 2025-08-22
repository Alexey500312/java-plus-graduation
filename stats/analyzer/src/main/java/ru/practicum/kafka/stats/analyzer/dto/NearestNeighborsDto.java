package ru.practicum.kafka.stats.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NearestNeighborsDto {
    private Long eventId;

    private Long neighborEventId;

    private Double score;
}
