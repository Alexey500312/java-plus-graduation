package ewm.hit;

import ewm.EndpointStatDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EndpointHitRepositoryTest {
    private final EndpointHitRepository endpointHitRepository;

    @Test
    @DisplayName("Статистика по запросам без списка uri")
    public void shouldGetStats() {
        LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 2, 23, 59, 59);
        List<EndpointStatDto> stats = List.of(
                new EndpointStatDto("/test", "/test/test", 3L),
                new EndpointStatDto("/test2", "/test2/test", 1L));

        List<EndpointStatDto> statsUnique = List.of(
                new EndpointStatDto("/test", "/test/test", 2L),
                new EndpointStatDto("/test2", "/test2/test", 1L));

        Optional<List<EndpointStatDto>> statsOptional = Optional.of(
                endpointHitRepository.findByTimestampBetween(start, end));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(stats);

        statsOptional = Optional.of(
                endpointHitRepository.findByTimestampBetweenDistinctByUri(start, end));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(statsUnique);
    }

    @Test
    @DisplayName("Статистика по запросам со списком uri")
    public void shouldGetStatsWithUris() {
        LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 2, 23, 59, 59);

        List<String> uris = List.of("/test/test");

        List<EndpointStatDto> expectedStats = List.of(
                new EndpointStatDto("app", "/test/test", 1L)); // ожидаем 1 hit

        endpointHitRepository.deleteAll();

        endpointHitRepository.save(EndpointHit.builder()
                .app("app")
                .uri("/test/test")
                .ip("192.168.0.1")
                .timestamp(start)
                .build());

        List<EndpointStatDto> actualStats = endpointHitRepository
                .findByTimestampBetweenAndUriIn(start, end, uris);

        assertThat(actualStats).usingRecursiveComparison().isEqualTo(expectedStats);
    }
}
