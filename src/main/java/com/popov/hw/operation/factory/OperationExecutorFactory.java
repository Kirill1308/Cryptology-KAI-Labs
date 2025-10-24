package com.popov.hw.operation.factory;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.operation.CryptographicOperationExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OperationExecutorFactory {

    private final Map<CryptoAlgorithm, CryptographicOperationExecutor> executorMap;

    public OperationExecutorFactory(List<CryptographicOperationExecutor> executors) {
        this.executorMap = executors.stream()
                .collect(Collectors.toMap(
                        CryptographicOperationExecutor::getSupportedAlgorithm,
                        Function.identity()
                ));
    }

    public CryptographicOperationExecutor getExecutor(CryptoAlgorithm algorithm) {
        CryptographicOperationExecutor executor = executorMap.get(algorithm);
        if (executor == null) {
            throw new IllegalArgumentException("No operation executor found for algorithm: " + algorithm);
        }
        return executor;
    }
}
