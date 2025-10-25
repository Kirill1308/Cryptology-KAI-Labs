package com.popov.hw.workflow;

import com.popov.hw.operation.OperationExecutorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final OperationExecutorFactory executorFactory;

    public void execute(WorkflowRequest request) {
        var executor = executorFactory.getExecutor(request.algorithm());
        executor.execute(request);
    }
}
