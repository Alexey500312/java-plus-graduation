package ru.practicum.kafka.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserActionPrimaryKey implements Serializable {
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;
}
