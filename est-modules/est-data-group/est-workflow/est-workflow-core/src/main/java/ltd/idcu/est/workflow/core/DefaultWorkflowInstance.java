package ltd.idcu.est.workflow.core;

import ltd.idcu.est.workflow.api.NodeExecution;
import ltd.idcu.est.workflow.api.NodeStatus;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowInstance;
import ltd.idcu.est.workflow.api.WorkflowStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultWorkflowInstance implements WorkflowInstance {
    
    private final String instanceId;
    private final String workflowId;
    private final String workflowName;
    private WorkflowStatus status;
    private final WorkflowContext context;
    private Date startTime;
    private Date endTime;
    private final List<NodeExecution> executions;
    private NodeExecution currentExecution;
    
    public DefaultWorkflowInstance(String instanceId, String workflowId, String workflowName, WorkflowContext context) {
        this.instanceId = instanceId;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.status = WorkflowStatus.CREATED;
        this.context = context;
        this.executions = new ArrayList<>();
    }
    
    @Override
    public String getInstanceId() {
        return instanceId;
    }
    
    @Override
    public String getWorkflowId() {
        return workflowId;
    }
    
    @Override
    public String getWorkflowName() {
        return workflowName;
    }
    
    @Override
    public WorkflowStatus getStatus() {
        return status;
    }
    
    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }
    
    @Override
    public WorkflowContext getContext() {
        return context;
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
        if (startTime != null) {
            Date end = endTime != null ? endTime : new Date();
            return end.getTime() - startTime.getTime();
        }
        return 0;
    }
    
    @Override
    public List<NodeExecution> getExecutions() {
        return new ArrayList<>(executions);
    }
    
    public void addExecution(NodeExecution execution) {
        executions.add(execution);
        this.currentExecution = execution;
    }
    
    @Override
    public NodeExecution getCurrentExecution() {
        return currentExecution;
    }
    
    @Override
    public List<NodeExecution> getExecutionsByStatus(NodeStatus status) {
        return executions.stream()
                .filter(e -> e.getStatus() == status)
                .collect(Collectors.toList());
    }
}
