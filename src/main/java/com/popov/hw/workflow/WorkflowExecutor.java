package com.popov.hw.workflow;

import com.popov.hw.operation.CryptographicOperationExecutor;
import com.popov.hw.operation.factory.OperationExecutorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final OperationExecutorFactory operationExecutorFactory;

    public void execute(CryptographyWorkflowRequest request) {
        CryptographicOperationExecutor executor = operationExecutorFactory.getExecutor(request.algorithm());
        executor.execute(request);
    }
}
