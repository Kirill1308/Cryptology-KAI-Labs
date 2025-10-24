package com.popov.hw.operation.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.service.RabinService;
import com.popov.hw.workflow.CryptographyWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.popov.hw.enums.CryptoAlgorithm.RABIN;
import static com.popov.hw.model.CryptoParameters.RabinParameters;

@Component
@RequiredArgsConstructor
public class RabinOperationExecutor implements CryptographicOperationExecutor {

    private final RabinService rabinService;

    @Override
    public void execute(CryptographyWorkflowRequest request) {
        var params = (RabinParameters) request.parameters();

        try {
            switch (request.operation()) {
                case ENCRYPT -> rabinService.encryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.n()
                );
                case DECRYPT -> rabinService.decryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.p(),
                        params.q()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Rabin operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RABIN;
    }
}
