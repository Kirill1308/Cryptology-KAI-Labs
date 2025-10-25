package com.popov.hw.operation;

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
public class OperationExecutorFactory {

    private final List<OperationExecutor> executors;
    private Map<CryptoAlgorithm, OperationExecutor> executorMap;

    private Map<CryptoAlgorithm, OperationExecutor> getExecutorMap() {
        if (executorMap == null) {
            executorMap = executors.stream()
                    .collect(Collectors.toMap(
                            OperationExecutor::getSupportedAlgorithm,
                            Function.identity()
                    ));
        }
        return executorMap;
    }

    public OperationExecutor getExecutor(CryptoAlgorithm algorithm) {
        OperationExecutor executor = getExecutorMap().get(algorithm);
        if (executor == null) {
            throw new InvalidInputException("No executor found for algorithm: " + algorithm);
        }
        return executor;
    }
}
