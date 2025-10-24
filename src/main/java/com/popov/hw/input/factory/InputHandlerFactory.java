package com.popov.hw.input.factory;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InputHandlerFactory {

    private final Map<CryptoAlgorithm, InputHandler<?>> handlerMap;

    public InputHandlerFactory(List<InputHandler<?>> inputHandlers) {
        this.handlerMap = inputHandlers.stream()
                .collect(Collectors.toMap(
                        InputHandler::getSupportedAlgorithm,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    public <T> InputHandler<T> getHandler(CryptoAlgorithm algorithm) {
        InputHandler<?> handler = handlerMap.get(algorithm);
        if (handler == null) {
            throw new IllegalArgumentException("No input handler found for algorithm: " + algorithm);
        }
        return (InputHandler<T>) handler;
    }
}
