package ewm.hit;

import ewm.EndpointStatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
           SELECT
                   new ewm.EndpointStatDto(h.app, h.uri, count(h.id))
           FROM
                   EndpointHit as h
           WHERE
                   h.timestamp between ?1 and ?2
           GROUP BY
                   h.app, h.uri
           ORDER BY
                   count(h.id) DESC
           """)
    List<EndpointStatDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
           SELECT
                   new ewm.EndpointStatDto(h.app, h.uri, count(distinct h.ip))
           FROM
                   EndpointHit as h
           WHERE
                   h.timestamp between ?1 and ?2
           GROUP BY
                   h.app, h.uri
           ORDER BY
                   count(distinct h.ip) DESC
           """)
    List<EndpointStatDto> findByTimestampBetweenDistinctByUri(LocalDateTime start, LocalDateTime end);

    @Query("""
           SELECT
                   new ewm.EndpointStatDto(h.app, h.uri, count(h.id))
           FROM
                   EndpointHit as h
           WHERE
                   h.timestamp between ?1 and ?2
               AND
                   h.uri IN (?3)
           GROUP BY
                   h.app, h.uri
           ORDER BY
                   count(h.id) DESC
           """)
    List<EndpointStatDto> findByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, Collection<String> uris);

    @Query("""
           SELECT
                   new ewm.EndpointStatDto(h.app, h.uri, count(distinct h.ip))
           FROM
                   EndpointHit as h
           WHERE
                   h.timestamp between ?1 and ?2
               AND
                   h.uri IN (?3)
           GROUP BY
                   h.app, h.uri
           ORDER BY
                   count(distinct h.ip) DESC
           """)
    List<EndpointStatDto> findByTimestampBetweenAndUriInDistinctByUri(LocalDateTime start, LocalDateTime end, Collection<String> uris);
}
