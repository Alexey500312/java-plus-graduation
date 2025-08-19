package ru.practicum.contract;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserOperations {
    /**
     * Получить коллекцию пользователей.
     *
     * @param userIds коллекция идентификаторов пользователей, которых надо получить.
     * @param from    количество пользователей, которое необходимо пропустить.
     * @param size    количество пользователей, которое необходимо получить.
     * @return коллекция пользователей.
     */
    @GetMapping
    Collection<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> userIds,
                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                 @RequestParam(defaultValue = "10") @Min(1) int size);
}
