package com.popov.hw.workflow;

import com.popov.hw.input.factory.InputHandlerFactory;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowRequestBuilder {

    private final UserInterface userInterface;
    private final InputHandlerFactory inputHandlerFactory;

    public CryptographyWorkflowRequest buildRequest() {
        // Get algorithm selection
        userInterface.displayAlgorithmMenu();
        var algorithm = userInterface.getAlgorithmSelection();

        // Get file paths
        String[] filePaths = userInterface.getFilePaths();
        String inputFile = filePaths[0];
        String outputFile = filePaths[1];

        // Get cipher operation
        var operation = userInterface.getCipherOperation();

        // Get algorithm-specific parameters
        var inputHandler = inputHandlerFactory.getHandler(algorithm);
        var parameters = inputHandler.collectParameters(userInterface);

        return new CryptographyWorkflowRequest(algorithm, operation, inputFile, outputFile, parameters);
    }
}
