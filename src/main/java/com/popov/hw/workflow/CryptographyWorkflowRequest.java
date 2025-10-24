package com.popov.hw.workflow;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;

public record CryptographyWorkflowRequest(CryptoAlgorithm algorithm, CipherOperation operation,
                                          String inputFilePath, String outputFilePath, Object parameters) {
}
