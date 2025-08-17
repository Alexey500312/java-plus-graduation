package ewm.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.request.ParticipationRequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "participation_requests")
@ToString
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "status", nullable = false)
    @Convert(converter = ParticipationRequestStatusConverter.class)
    private ParticipationRequestStatus status;
}
