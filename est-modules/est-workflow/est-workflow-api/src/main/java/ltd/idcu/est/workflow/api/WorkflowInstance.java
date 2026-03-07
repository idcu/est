package ltd.idcu.est.workflow.api;

import java.util.Date;
import java.util.List;

public interface WorkflowInstance {
    
    String getInstanceId();
    
    String getWorkflowId();
    
    String getWorkflowName();
    
    WorkflowStatus getStatus();
    
    WorkflowContext getContext();
    
    Date getStartTime();
    
    Date getEndTime();
    
    long getDuration();
    
    List<NodeExecution> getExecutions();
    
    NodeExecution getCurrentExecution();
    
    List<NodeExecution> getExecutionsByStatus(NodeStatus status);
}
