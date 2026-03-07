package ltd.idcu.est.workflow.core.node;

import ltd.idcu.est.workflow.api.WorkflowContext;

public class ParallelGateway extends GatewayNode {
    
    public ParallelGateway(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getType() {
        return "PARALLEL_GATEWAY";
    }
    
    @Override
    public void execute(WorkflowContext context) {
    }
}
