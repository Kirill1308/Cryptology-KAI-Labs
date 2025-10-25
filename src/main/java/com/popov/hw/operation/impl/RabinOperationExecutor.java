package com.popov.hw.operation.impl;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.exception.CryptoOperationException;
import com.popov.hw.i18n.MessageService;
import com.popov.hw.operation.OperationExecutor;
import com.popov.hw.service.crypto.RabinCryptoService;
import com.popov.hw.workflow.WorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabinOperationExecutor implements OperationExecutor {

    private final RabinCryptoService cryptoService;
    private final MessageService messageService;

    @Override
    public void execute(WorkflowRequest request) {
        try {
            if (request.operation() == CipherOperation.ENCRYPT) {
                cryptoService.encrypt(request.inputFilePath(), request.outputFilePath(), request.parameters());
            } else {
                cryptoService.decrypt(request.inputFilePath(), request.outputFilePath(), request.parameters());
            }
        } catch (Exception e) {
            throw new CryptoOperationException(
                    messageService.getOperationFailedError("Rabin", e.getMessage()), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return CryptoAlgorithm.RABIN;
    }
}

