package ru.practicum.kafka.stats.analyzer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.kafka.stats.analyzer.dto.RecommendedEventDto;
import ru.practicum.kafka.stats.analyzer.model.UserAction;
import ru.practicum.kafka.stats.analyzer.model.UserActionPrimaryKey;

import java.util.Collection;

public interface UserActionRepository extends JpaRepository<UserAction, UserActionPrimaryKey> {
    Page<UserAction> findByIdUserId(Long userId, Pageable pageable);

    Collection<UserAction> findByIdEventIdInAndIdUserId(Collection<Long> eventIds, Long userId);

    @Query("""
            select new ru.practicum.kafka.stats.analyzer.dto.RecommendedEventDto(ua.id.eventId, sum(ua.weight))
            from UserAction as ua
            where ua.id.eventId in (?1)
            group by ua.id.eventId
            order by sum(ua.weight) desc
            """)
    Collection<RecommendedEventDto> getInteractionsCount(Collection<Long> eventIds);
}
