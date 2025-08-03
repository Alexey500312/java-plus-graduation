package ewm.compilation;

import com.querydsl.core.BooleanBuilder;
import ewm.event.EventRepository;
import ewm.exception.NotFoundException;
import ewm.pageble.PageOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для сущности "Подборка событий".
 */
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    /**
     * Хранилище данных для сущности "Подборка событий".
     */
    private final CompilationRepository compilationRepository;

    /**
     * Хранилище данных для сущности "Событие".
     */
    private final EventRepository eventRepository;

    /**
     * Добавить новую подборку событий.
     *
     * @param createCompilationDto трансферный объект, содержащий данные для создания подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @Override
    public CompilationDto createCompilation(CreateCompilationDto createCompilationDto) {
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(createCompilationDto);

        if (createCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(createCompilationDto.getEvents()));
        }

        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    /**
     * Получить коллекцию подборок событий.
     *
     * @param pinned признак, необходимо ли извлекать только подборки событий, закрепленные на главной странице сайта.
     * @param from   количество подборок событий, которое необходимо пропустить.
     * @param size   количество подборок событий, которое необходимо извлечь.
     * @return коллекция трансферных объектов, содержащих данные о подборках событий.
     */
    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        BooleanBuilder predicate = new BooleanBuilder();
        Pageable pageable = PageOffset.of(from, size);

        if (pinned != null) {
            predicate.and(QCompilation.compilation.pinned.eq(pinned));
        }

        return CompilationMapper.INSTANCE.toCompilationDtoCollection(compilationRepository.findAll(predicate, pageable).getContent());
    }

    /**
     * Получить подборку событий по её идентификатору.
     *
     * @param compilationId идентификатор подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d not found: ", compilationId))));
    }

    /**
     * Обновить подборку событий.
     *
     * @param compilationId        идентификатор подборки событий.
     * @param updateCompilationDto трансферный объект, содержащий данные для обновления подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d not found: ", compilationId)));

        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }

        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(updateCompilationDto.getEvents()));
        }

        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    /**
     * Удалить подборку событий.
     *
     * @param compilationId идентификатор подборки событий.
     */
    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }
}
