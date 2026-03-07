package ltd.idcu.est.workflow.core.node;

import ltd.idcu.est.workflow.api.WorkflowContext;

public class InclusiveGateway extends GatewayNode {
    
    public InclusiveGateway(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getType() {
        return "INCLUSIVE_GATEWAY";
    }
    
    @Override
    public void execute(WorkflowContext context) {
    }
}
