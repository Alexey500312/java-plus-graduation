package ru.practicum.kafka.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "event_similarity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class EventSimilarity {
    @EmbeddedId
    private EventSimilarityPrimaryKey id;

    @Column(name = "score")
    private Double score;

    @Column(name = "action_date")
    private Instant actionDate;
}
