package ru.practicum.kafka.stats.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_actions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class UserAction {
    @EmbeddedId
    private UserActionPrimaryKey id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "action_date")
    private Instant actionDate;
}
