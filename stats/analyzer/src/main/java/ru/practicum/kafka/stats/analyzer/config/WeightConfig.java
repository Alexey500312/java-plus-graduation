package ru.practicum.kafka.stats.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
@ToString
@ConfigurationProperties("analyzer")
public class WeightConfig implements Weight {
    private EnumMap<WeightType, Double> weights = new EnumMap<>(WeightType.class);

    public WeightConfig(Map<String, Double> actionWeight) {
        for (Map.Entry<String, Double> entry : actionWeight.entrySet()) {
            WeightType weightType = WeightType.toWeightsType(entry.getKey());
            if (weightType != null) {
                this.weights.put(weightType, entry.getValue());
            }
        }
    }
}
