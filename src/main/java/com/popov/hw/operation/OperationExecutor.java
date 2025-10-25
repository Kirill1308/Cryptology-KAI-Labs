package com.popov.hw.operation;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.workflow.WorkflowRequest;

public interface OperationExecutor {

    void execute(WorkflowRequest request);

    CryptoAlgorithm getSupportedAlgorithm();
}
