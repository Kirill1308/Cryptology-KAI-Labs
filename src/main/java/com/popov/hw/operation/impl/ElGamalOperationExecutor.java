package com.popov.hw.operation.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.model.CryptoParameters.ElGamalParameters;
import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.service.ElGamalService;
import com.popov.hw.workflow.CryptographyWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.popov.hw.enums.CryptoAlgorithm.EL_GAMAL;

@Component
@RequiredArgsConstructor
public class ElGamalOperationExecutor implements CryptographicOperationExecutor {

    private final ElGamalService elGamalService;

    @Override
    public void execute(CryptographyWorkflowRequest request) {
        var params = (ElGamalParameters) request.parameters();

        try {
            switch (request.operation()) {
                case ENCRYPT -> elGamalService.encryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.p(),
                        params.g(),
                        params.publicKey()
                );
                case DECRYPT -> elGamalService.decryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        params.p(),
                        params.x()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("El-Gamal operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return EL_GAMAL;
    }
}
