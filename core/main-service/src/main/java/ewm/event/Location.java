package ewm.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Широта и долгота места проведения события.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public class Location {
    /**
     * Широта.
     */
    private float lat;

    /**
     * Долгота.
     */
    private float lon;
}
