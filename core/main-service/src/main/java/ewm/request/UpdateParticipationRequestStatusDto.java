package ewm.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Builder(toBuilder = true)
@Data
public class UpdateParticipationRequestStatusDto {
    @NotNull(message = "Список заявок на участие не может быть пустым")
    @Size(min = 1, message = "Кол-во заявок на участие должно быть больше нуля")
    private Collection<Long> requestIds;

    @NotNull(message = "Статус заявки на участие не может быть пустым")
    private ParticipationRequestStatus status;
}
