package com.popov.hw.workflow;

import com.popov.hw.input.ParameterCollectorFactory;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowRequestBuilder {

    private final UserInterface userInterface;
    private final ParameterCollectorFactory collectorFactory;

    public WorkflowRequest build() {
        var algorithm = userInterface.selectAlgorithm();
        var operation = userInterface.selectOperation();
        var inputFile = userInterface.getInputFilePath();
        var outputFile = userInterface.getOutputFilePath();

        var collector = collectorFactory.getCollector(algorithm);
        var parameters = collector.collectParameters(userInterface);

        return WorkflowRequest.builder()
                .algorithm(algorithm)
                .operation(operation)
                .inputFilePath(inputFile)
                .outputFilePath(outputFile)
                .parameters(parameters)
                .build();
    }
}
