package ltd.idcu.est.workflow.core.node;

import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowException;

import java.util.function.Consumer;

public class TaskNode implements Node {
    
    private final String id;
    private final String name;
    private final Consumer<WorkflowContext> task;
    
    public TaskNode(String id, String name, Consumer<WorkflowContext> task) {
        this.id = id;
        this.name = name;
        this.task = task;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getType() {
        return "TASK";
    }
    
    @Override
    public void execute(WorkflowContext context) throws WorkflowException {
        try {
            task.accept(context);
        } catch (Exception e) {
            throw new WorkflowException("Task execution failed: " + name, e);
        }
    }
}
