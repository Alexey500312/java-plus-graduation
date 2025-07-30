package ewm.hit;

import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;
import ewm.exception.InvalidRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody @Valid CreateEndpointHitDto createEndpointHitDto) {
        endpointHitService.createEndpointHit(createEndpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EndpointStatDto> viewStats(@RequestParam(name = "start") String start,
                                                 @RequestParam(name = "end") String end,
                                                 @RequestParam(name = "uris", required = false) List<String> uris,
                                                 @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException("Start can't be greater than End");
        }

        return endpointHitService.viewStats(startDate, endDate, uris, unique);
    }
}
