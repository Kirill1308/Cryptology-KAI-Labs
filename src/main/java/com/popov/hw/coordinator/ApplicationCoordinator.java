package com.popov.hw.coordinator;

import com.popov.hw.exception.CryptoOperationException;
import com.popov.hw.exception.InvalidInputException;
import com.popov.hw.ui.UserInterface;
import com.popov.hw.workflow.WorkflowExecutor;
import com.popov.hw.workflow.WorkflowRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationCoordinator {

    private final WorkflowRequestBuilder requestBuilder;
    private final WorkflowExecutor workflowExecutor;
    private final UserInterface userInterface;

    public void run() {
        try {
            userInterface.displaySuccess();
            var request = requestBuilder.build();
            workflowExecutor.execute(request);
            userInterface.displaySuccess();
        } catch (InvalidInputException | CryptoOperationException e) {
            userInterface.displayError(e.getMessage());
            log.error("Application error", e);
        } catch (Exception e) {
            userInterface.displayError(e.getMessage());
            log.error("Unexpected error", e);
        } finally {
            userInterface.close();
        }
    }
}
