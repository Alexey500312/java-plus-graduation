package ru.practicum.kafka.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EventSimilarityPrimaryKey {
    @Column(name = "event_id_a")
    private Long eventIdA;

    @Column(name = "event_id_b")
    private Long eventIdB;
}
