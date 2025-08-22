package ru.practicum.kafka.stats.analyzer.service.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.stats.analyzer.config.Weight;
import ru.practicum.kafka.stats.analyzer.mapper.UserActionMapper;
import ru.practicum.kafka.stats.analyzer.model.UserAction;
import ru.practicum.kafka.stats.analyzer.repository.UserActionRepository;

@Component
@RequiredArgsConstructor
public class UserActionHandlerImpl implements UserActionHandler {
    private final UserActionRepository userActionRepository;
    private final Weight weight;

    @Override
    @Transactional
    public void handle(UserActionAvro userActionAvro) {
        UserAction userAction = UserActionMapper.INSTANCE.toUserAction(userActionAvro, weight);
        UserAction oldUserAction = userActionRepository.findById(userAction.getId())
                .orElse(null);
        userAction.setWeight(oldUserAction == null || oldUserAction.getWeight() < userAction.getWeight()
                ? userAction.getWeight()
                : oldUserAction.getWeight());
        userActionRepository.save(userAction);
    }
}
