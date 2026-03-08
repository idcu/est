package ltd.idcu.est.workflow.core.node;

import ltd.idcu.est.workflow.api.Node;
import ltd.idcu.est.workflow.api.WorkflowContext;
import ltd.idcu.est.workflow.api.WorkflowException;

public abstract class GatewayNode implements Node {
    
    protected final String id;
    protected final String name;
    
    public GatewayNode(String id, String name) {
        this.id = id;
        this.name = name;
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
    public abstract String getType();
    
    @Override
    public abstract void execute(WorkflowContext context) throws WorkflowException;
}
