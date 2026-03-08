package ltd.idcu.est.workflow.core.integration;

import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.fixed.FixedRateSchedulers;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowEngine;
import ltd.idcu.est.workflow.api.WorkflowException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScheduledWorkflowTrigger {
    
    private final WorkflowEngine workflowEngine;
    private final Scheduler scheduler;
    
    public ScheduledWorkflowTrigger(WorkflowEngine workflowEngine, Scheduler scheduler) {
        this.workflowEngine = workflowEngine;
        this.scheduler = scheduler;
    }
    
    public String scheduleAtFixedRate(String workflowId, long initialDelay, long period, TimeUnit unit) {
        return scheduleAtFixedRate(workflowId, null, initialDelay, period, unit);
    }
    
    public String scheduleAtFixedRate(String workflowId, Map<String, Object> variables, 
                                       long initialDelay, long period, TimeUnit unit) {
        Task task = FixedRateSchedulers.wrap(() -> {
            try {
                WorkflowContext context = createContext(workflowId, variables);
                workflowEngine.startWorkflow(workflowId, context);
            } catch (Exception e) {
                throw new WorkflowException("Failed to start scheduled workflow: " + workflowId, e);
            }
        });
        return scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
    
    public String scheduleWithFixedDelay(String workflowId, long initialDelay, long delay, TimeUnit unit) {
        return scheduleWithFixedDelay(workflowId, null, initialDelay, delay, unit);
    }
    
    public String scheduleWithFixedDelay(String workflowId, Map<String, Object> variables,
                                          long initialDelay, long delay, TimeUnit unit) {
        Task task = FixedRateSchedulers.wrap(() -> {
            try {
                WorkflowContext context = createContext(workflowId, variables);
                workflowEngine.startWorkflow(workflowId, context);
            } catch (Exception e) {
                throw new WorkflowException("Failed to start scheduled workflow: " + workflowId, e);
            }
        });
        return scheduler.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }
    
    public String scheduleCron(String workflowId, String cronExpression) {
        return scheduleCron(workflowId, null, cronExpression);
    }
    
    public String scheduleCron(String workflowId, Map<String, Object> variables, String cronExpression) {
        Task task = FixedRateSchedulers.wrap(() -> {
            try {
                WorkflowContext context = createContext(workflowId, variables);
                workflowEngine.startWorkflow(workflowId, context);
            } catch (Exception e) {
                throw new WorkflowException("Failed to start scheduled workflow: " + workflowId, e);
            }
        });
        return scheduler.scheduleCron(task, cronExpression);
    }
    
    public boolean cancelSchedule(String taskId) {
        return scheduler.cancel(taskId);
    }
    
    private WorkflowContext createContext(String workflowId, Map<String, Object> variables) {
        WorkflowContext context = null;
        if (workflowEngine.getRepository() != null) {
            context = workflowEngine.getRepository().findDefinitionById(workflowId)
                    .map(def -> {
                        WorkflowContext ctx = new ltd.idcu.est.workflow.core.DefaultWorkflowContext(
                                "scheduled-" + System.currentTimeMillis());
                        if (variables != null) {
                            ctx.setVariables(variables);
                        }
                        return ctx;
                    })
                    .orElse(null);
        }
        if (context == null) {
            context = new ltd.idcu.est.workflow.core.DefaultWorkflowContext(
                    "scheduled-" + System.currentTimeMillis());
            if (variables != null) {
                context.setVariables(variables);
            }
        }
        return context;
    }
}
