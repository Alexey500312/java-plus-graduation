package ewm.compilation;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для публичной части API подборки событий.
 */
@RequestMapping("/compilations")
@RequiredArgsConstructor
@RestController
@Validated
public class PublicCompilationController {
    /**
     * Сервис для сущности "Подборка событий".
     */
    private final CompilationService compilationService;

    /**
     * Получить коллекцию подборок событий.
     *
     * @param pinned признак, необходимо ли извлекать только подборки событий, закрепленные на главной странице сайта.
     * @param from   количество подборок событий, которое необходимо пропустить.
     * @param size   количество подборок событий, которое необходимо извлечь.
     * @return коллекция трансферных объектов, содержащих данные о подборках событий.
     */
    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * Получить подборку событий по её идентификатору.
     *
     * @param compilationId идентификатор подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable @Positive Long compilationId) {
        return compilationService.getCompilationById(compilationId);
    }
}
