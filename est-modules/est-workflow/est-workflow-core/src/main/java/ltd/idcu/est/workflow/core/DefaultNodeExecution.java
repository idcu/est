package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.NodeExecution;
import ltd.idcu.est.workflow.api.NodeStatus;

import java.util.Date;

public class DefaultNodeExecution implements NodeExecution {
    
    private final String executionId;
    private final String nodeId;
    private final String nodeName;
    private NodeStatus status;
    private Date startTime;
    private Date endTime;
    private Throwable error;
    
    public DefaultNodeExecution(String executionId, String nodeId, String nodeName) {
        this.executionId = executionId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.status = NodeStatus.PENDING;
    }
    
    @Override
    public String getExecutionId() {
        return executionId;
    }
    
    @Override
    public String getNodeId() {
        return nodeId;
    }
    
    @Override
    public String getNodeName() {
        return nodeName;
    }
    
    @Override
    public NodeStatus getStatus() {
        return status;
    }
    
    public void setStatus(NodeStatus status) {
        this.status = status;
    }
    
    @Override
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    @Override
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public long getDuration() {
        if (startTime != null && endTime != null) {
            return endTime.getTime() - startTime.getTime();
        }
        return 0;
    }
    
    @Override
    public Throwable getError() {
        return error;
    }
    
    public void setError(Throwable error) {
        this.error = error;
    }
    
    @Override
    public String getErrorMessage() {
        return error != null ? error.getMessage() : null;
    }
}
