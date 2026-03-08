package ltd.idcu.est.workflow.api;

import java.util.Date;

public interface NodeExecution {
    
    String getExecutionId();
    
    String getNodeId();
    
    String getNodeName();
    
    NodeStatus getStatus();
    
    Date getStartTime();
    
    Date getEndTime();
    
    long getDuration();
    
    Throwable getError();
    
    String getErrorMessage();
}
