package ltd.idcu.est.workflow.core.node;

import ltd.idcu.est.workflow.api.WorkflowContext;

public class ExclusiveGateway extends GatewayNode {
    
    public ExclusiveGateway(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getType() {
        return "EXCLUSIVE_GATEWAY";
    }
    
    @Override
    public void execute(WorkflowContext context) {
    }
}
