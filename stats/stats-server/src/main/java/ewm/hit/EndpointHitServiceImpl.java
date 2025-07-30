package ewm.hit;

import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void createEndpointHit(CreateEndpointHitDto createEndpointHitDto) {
        endpointHitRepository.save(EndpointHitMapper.INSTANCE.toEndpointHit(createEndpointHitDto));
    }

    @Override
    public Collection<EndpointStatDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (!unique) {
                return endpointHitRepository.findByTimestampBetween(start, end);
            }
            return endpointHitRepository.findByTimestampBetweenDistinctByUri(start, end);
        }

        if (!unique) {
            return endpointHitRepository.findByTimestampBetweenAndUriIn(start, end, uris);
        }
        return endpointHitRepository.findByTimestampBetweenAndUriInDistinctByUri(start, end, uris);
    }
}
