package com.popov.hw.operation;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.workflow.CryptographyWorkflowRequest;

public interface CryptographicOperationExecutor {

    void execute(CryptographyWorkflowRequest request);

    CryptoAlgorithm getSupportedAlgorithm();
}
