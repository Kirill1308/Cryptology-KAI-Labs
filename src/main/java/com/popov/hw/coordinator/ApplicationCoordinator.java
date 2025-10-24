package com.popov.hw.coordinator;

import com.popov.hw.ui.UserInterface;
import com.popov.hw.workflow.WorkflowExecutor;
import com.popov.hw.workflow.WorkflowRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationCoordinator {

    private final WorkflowRequestBuilder requestBuilder;
    private final WorkflowExecutor workflowExecutor;
    private final UserInterface userInterface;

    public void execute() {
        try {
            var request = requestBuilder.buildRequest();

            workflowExecutor.execute(request);

            userInterface.displaySuccess("Operation completed successfully!");

        } catch (IllegalArgumentException ex) {
            userInterface.displayError(ex.getMessage());
        } catch (Exception ex) {
            userInterface.displayError(ex.getMessage());
            ex.printStackTrace();
        } finally {
            userInterface.close();
        }
    }
}
