package ru.practicum.kafka.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.stats.analyzer.service.action.UserActionProcessor;
import ru.practicum.kafka.stats.analyzer.service.similarity.EventSimilarityProcessor;

@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    private final UserActionProcessor userActionProcessor;
    private final EventSimilarityProcessor eventSimilarityProcessor;

    @Override
    public void run(String... args) throws Exception {
        Thread hubEventsThread = new Thread(userActionProcessor);
        hubEventsThread.setName("userActionHandlerThread");
        hubEventsThread.start();

        eventSimilarityProcessor.start();
    }
}
