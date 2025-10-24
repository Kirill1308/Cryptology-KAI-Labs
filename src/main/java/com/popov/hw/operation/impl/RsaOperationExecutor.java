package com.popov.hw.operation.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.service.RsaService;
import com.popov.hw.workflow.CryptographyWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.popov.hw.enums.CryptoAlgorithm.RSA;
import static com.popov.hw.model.CryptoParameters.*;

@Component
@RequiredArgsConstructor
public class RsaOperationExecutor implements CryptographicOperationExecutor {

    private final RsaService rsaService;

    @Override
    public void execute(CryptographyWorkflowRequest request) {
        var params = (RsaParameters) request.parameters();

        try {
            switch (request.operation()) {
                case ENCRYPT -> rsaService.encryptFileWithParams(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.e(),
                        params.n()
                );
                case DECRYPT -> rsaService.decryptFileWithParams(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.d(),
                        params.n()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("RSA operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RSA;
    }
}
