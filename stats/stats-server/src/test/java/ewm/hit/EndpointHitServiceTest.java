package ewm.hit;

import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EndpointHitServiceTest {
    private final EndpointHitService endpointHitService;

    @Test
    @DisplayName("Создать ресурс hit")
    public void shouldCreateEndpointHit() throws UnknownHostException {
        EndpointHit endpointHit = new EndpointHit(
                5L,
                "/newApp",
                "/newApp/test",
                "10.0.0.10",
                LocalDateTime.now());

        CreateEndpointHitDto createEndpointHitDto = new CreateEndpointHitDto(
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        endpointHitService.createEndpointHit(createEndpointHitDto);
    }

    @Test
    @DisplayName("Статистика по запросам")
    public void shouldViewStats() {
        LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 2, 23, 59, 59);
        List<EndpointStatDto> stats = List.of(
                new EndpointStatDto("/test", "/test/test", 3L),
                new EndpointStatDto("/test2", "/test2/test", 1L));

        List<EndpointStatDto> statsUnique = List.of(
                new EndpointStatDto("/test", "/test/test", 2L),
                new EndpointStatDto("/test2", "/test2/test", 1L));

        List<String> uris = List.of("/test/test");

        List<EndpointStatDto> statsWithUris = List.of(
                new EndpointStatDto("/test", "/test/test", 3L));

        List<EndpointStatDto> statsWithUrisUnique = List.of(
                new EndpointStatDto("/test", "/test/test", 2L));

        Optional<Collection<EndpointStatDto>> statsOptional = Optional.of(
                endpointHitService.viewStats(start, end, null, false));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(stats);

        statsOptional = Optional.of(
                endpointHitService.viewStats(start, end, null, true));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(statsUnique);

        statsOptional = Optional.of(
                endpointHitService.viewStats(start, end, uris, false));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(statsWithUris);

        statsOptional = Optional.of(
                endpointHitService.viewStats(start, end, uris, true));

        assertThat(statsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(statsWithUrisUnique);
    }
}
