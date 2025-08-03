package ewm.client;

import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private static final String STATS_DEFAULT_URL = "http://stats-server:9090";
    private final DiscoveryClient discoveryClient;
    private int oldIndexService = -1;

    public ResponseEntity<Void> sendHit(CreateEndpointHitDto createEndpointHitDto) {
        return getRestClient().post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(createEndpointHitDto)
                .retrieve()
                .toEntity(Void.class);
    }

    public ResponseEntity<List<EndpointStatDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return getRestClient().get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

    private RestClient getRestClient() {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("stats-server");
        if (serviceInstances != null && !serviceInstances.isEmpty()) {
            oldIndexService = serviceInstances.size() - 1 > oldIndexService ? ++oldIndexService : 0;
            ServiceInstance serviceInstance = serviceInstances.get(oldIndexService);
            return RestClient.builder().baseUrl(serviceInstance.getUri().toString()).build();
        } else {
            return RestClient.builder().baseUrl(STATS_DEFAULT_URL).build();
        }
    }
}
