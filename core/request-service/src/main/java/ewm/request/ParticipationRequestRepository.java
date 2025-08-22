package ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Collection<ParticipationRequest> findByRequesterId(Long requesterId);

    Collection<ParticipationRequest> findByEventId(Long eventId);

    Collection<ParticipationRequest> findByIdIn(Collection<Long> ids);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long userId, Long eventId);
}
