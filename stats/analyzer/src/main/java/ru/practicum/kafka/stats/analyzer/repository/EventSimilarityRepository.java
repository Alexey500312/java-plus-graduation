package ru.practicum.kafka.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.kafka.stats.analyzer.dto.EventSimilarityDto;
import ru.practicum.kafka.stats.analyzer.dto.NearestNeighborsDto;
import ru.practicum.kafka.stats.analyzer.model.EventSimilarity;
import ru.practicum.kafka.stats.analyzer.model.EventSimilarityPrimaryKey;

import java.util.Collection;

public interface EventSimilarityRepository extends JpaRepository<EventSimilarity, EventSimilarityPrimaryKey> {
    @Query("""
           select case when not es.id.eventIdA in (?1)
                       then es.id.eventIdA
                       else es.id.eventIdB
                       end as similarEventId
             from EventSimilarity as es
            where (es.id.eventIdA in (?1) or es.id.eventIdB in (?1))
              and not case when not es.id.eventIdA in (?1)
                           then es.id.eventIdA
                           else es.id.eventIdB
                           end in (select ua.id.eventId
                                     from UserAction as ua
                                    where ua.id.userId = ?2)
           group by similarEventId
           order by max(es.score) desc
           limit ?3
           """)
    Collection<Long> getSimilarEventsForRecommended(Collection<Long> eventIds, Long userId, Integer maxResults);

    @Query("""
           select new ru.practicum.kafka.stats.analyzer.dto.NearestNeighborsDto(case when es.id.eventIdA in (?1)
                                                                                     then es.id.eventIdA
                                                                                     else es.id.eventIdB
                                                                                     end as eventId,
                                                                                case when not es.id.eventIdA in (?1)
                                                                                     then es.id.eventIdA
                                                                                     else es.id.eventIdB
                                                                                     end as neighborEventId,
                                                                                es.score)
             from EventSimilarity as es
            where (es.id.eventIdA in (?1) or es.id.eventIdB in (?1))
              and case when not es.id.eventIdA in (?1)
                       then es.id.eventIdA
                       else es.id.eventIdB
                       end in (select ua.id.eventId
                                 from UserAction as ua
                                where ua.id.userId = ?2)
           """)
    Collection<NearestNeighborsDto> getNearestNeighbors(Collection<Long> eventIds, Long userId);

    @Query("""
           select new ru.practicum.kafka.stats.analyzer.dto.EventSimilarityDto(case when es.id.eventIdA != ?1
                                                                                    then es.id.eventIdA
                                                                                    else es.id.eventIdB
                                                                                    end,
                                                                               es.score)
             from EventSimilarity as es
            where (es.id.eventIdA = ?1 or es.id.eventIdB = ?1)
              and not case when es.id.eventIdA != ?1
                           then es.id.eventIdA
                           else es.id.eventIdB
                           end in (select ua.id.eventId
                                     from UserAction as ua
                                    where ua.id.userId = ?2)
           order by es.score desc
           limit ?3
           """)
    Collection<EventSimilarityDto> getSimilarEvents(Long eventId, Long userId, Integer maxResults);
}
