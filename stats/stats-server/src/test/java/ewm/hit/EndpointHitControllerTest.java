package ewm.hit;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.CreateEndpointHitDto;
import ewm.EndpointStatDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EndpointHitController.class)
@AutoConfigureMockMvc
public class EndpointHitControllerTest {
    @MockBean
    private EndpointHitService endpointHitService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Создать ресурс hit")
    public void shouldCreateEndpointHit() throws Exception {
        CreateEndpointHitDto createEndpointHitDto = new CreateEndpointHitDto(
                "/newApp",
                "/newApp/test",
                "10.0.0.10",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(createEndpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(endpointHitService, times(1)).createEndpointHit(createEndpointHitDto);
    }

    @Test
    @DisplayName("Статистика по запросам")
    public void shouldViewStats() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 2, 23, 59, 59);
        List<String> uris = List.of("/test/test");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<EndpointStatDto> statsWithUrisUnique = List.of(
                new EndpointStatDto("/test", "/test/test", 2L));

        when(endpointHitService.viewStats(start, end, uris, true)).thenReturn(statsWithUrisUnique);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("start", start.format(formatter));
        params.add("end", end.format(formatter));
        params.add("uris", uris.getFirst());
        params.add("unique", "true");
        mvc.perform(get("/stats")
                        .params(params)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(statsWithUrisUnique)));

        verify(endpointHitService, times(1)).viewStats(start, end, uris, true);
    }
}
