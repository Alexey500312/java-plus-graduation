package ewm.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface CompilationMapper {
    /**
     * Экземпляр маппера для моделей, содержащих информацию о подборке событий.
     */
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    /**
     * Преобразовать трансферный объект, содержащий данные для создания подборки событий, в объект подборки событий.
     *
     * @param createCompilationDto трансферный объект, содержащий данные для создания подборки событий.
     * @return объект подборки событий.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(CreateCompilationDto createCompilationDto);

    /**
     * Преобразовать объект подборки событий в трансферный объект, содержащий данные о подборке событий.
     *
     * @param compilation объект подборки событий.
     * @return трансферный объект, содержащий данные о подборке событий.
     */
    CompilationDto toCompilationDto(Compilation compilation);

    /**
     * Преобразовать коллекцию объектов подборок событий в коллекцию трансферных объектов, содержащих данные о подборках событий.
     *
     * @param compilations коллекция объектов подборок событий.
     * @return коллекция трансферных объектов, содержащих данные о подборках событий.
     */
    Collection<CompilationDto> toCompilationDtoCollection(Collection<Compilation> compilations);
}
