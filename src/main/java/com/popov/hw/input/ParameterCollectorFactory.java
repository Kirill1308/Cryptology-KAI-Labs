package com.popov.hw.input;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParameterCollectorFactory {

    private final List<ParameterCollector<?>> collectors;
    private Map<CryptoAlgorithm, ParameterCollector<?>> collectorMap;

    private Map<CryptoAlgorithm, ParameterCollector<?>> getCollectorMap() {
        if (collectorMap == null) {
            collectorMap = collectors.stream()
                    .collect(Collectors.toMap(
                            ParameterCollector::getSupportedAlgorithm,
                            Function.identity()
                    ));
        }
        return collectorMap;
    }

    public ParameterCollector<?> getCollector(CryptoAlgorithm algorithm) {
        ParameterCollector<?> collector = getCollectorMap().get(algorithm);
        if (collector == null) {
            throw new InvalidInputException("No collector found for algorithm: " + algorithm);
        }
        return collector;
    }
}
