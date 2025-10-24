package com.popov.hw.operation.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.model.CryptoParameters.ShamirParameters;
import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.service.ShamirService;
import com.popov.hw.workflow.CryptographyWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.popov.hw.enums.CryptoAlgorithm.SHAMIR;

@Component
@RequiredArgsConstructor
public class ShamirOperationExecutor implements CryptographicOperationExecutor {

    private final ShamirService shamirService;

    @Override
    public void execute(CryptographyWorkflowRequest request) {
        var params = (ShamirParameters) request.parameters();

        try {
            switch (request.operation()) {
                case ENCRYPT -> shamirService.encryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.p(),
                        params.keyPair()[0],
                        params.keyPair()[1]
                );
                case DECRYPT -> shamirService.decryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.p(),
                        params.keyPair()[1]
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Shamir operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return SHAMIR;
    }
}
