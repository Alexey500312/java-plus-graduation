package ewm.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@RestController
@Validated
public class AdminCompilationController {
    /**
     * Сервис для сущности "Подборка событий".
     */
    private final CompilationService compilationService;

    /**
     * Добавить новую подборку событий.
     *
     * @param createCompilationDto трансферный объект, содержащий данные для создания подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid CreateCompilationDto createCompilationDto) {
        return compilationService.createCompilation(createCompilationDto);
    }

    /**
     * Обновить подборку событий.
     *
     * @param compilationId        идентификатор подборки событий.
     * @param updateCompilationDto трансферный объект, содержащий данные для обновления подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Long compilationId,
                                            @RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        return compilationService.updateCompilation(compilationId, updateCompilationDto);
    }

    /**
     * Удалить подборку событий.
     *
     * @param compilationId идентификатор подборки событий.
     */
    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }
}
