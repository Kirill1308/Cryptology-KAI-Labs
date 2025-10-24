package com.popov.hw.operation.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.service.EllipticCurveService;
import com.popov.hw.workflow.CryptographyWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.popov.hw.enums.CryptoAlgorithm.ELLIPTIC_CURVE;
import static com.popov.hw.model.CryptoParameters.EllipticCurveParameters;

@Component
@RequiredArgsConstructor
public class EllipticCurveOperationExecutor implements CryptographicOperationExecutor {

    private final EllipticCurveService ellipticCurveService;

    @Override
    public void execute(CryptographyWorkflowRequest request) {
        var params = (EllipticCurveParameters) request.parameters();

        try {
            switch (request.operation()) {
                case ENCRYPT -> ellipticCurveService.encryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        (EllipticCurveService.ECParameters) params.curve(),
                        (EllipticCurveService.ECPoint) params.publicKey()
                );
                case DECRYPT -> ellipticCurveService.decryptFile(
                        request.inputFilePath(),
                        request.outputFilePath(),
                        (EllipticCurveService.ECParameters) params.curve(),
                        params.privateKey()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Elliptic Curve operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return ELLIPTIC_CURVE;
    }
}
