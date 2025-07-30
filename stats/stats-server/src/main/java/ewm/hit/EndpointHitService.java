package ewm.hit;

import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EndpointHitService {
    void createEndpointHit(CreateEndpointHitDto createEndpointHitDto);

    Collection<EndpointStatDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
