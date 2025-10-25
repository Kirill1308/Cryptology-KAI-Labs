package com.popov.hw.workflow;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import lombok.Builder;

@Builder
public record WorkflowRequest(
        CryptoAlgorithm algorithm,
        CipherOperation operation,
        String inputFilePath,
        String outputFilePath,
        Object parameters
) {
}

