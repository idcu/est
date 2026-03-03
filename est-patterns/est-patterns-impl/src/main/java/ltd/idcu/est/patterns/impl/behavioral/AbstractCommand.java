package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Command;

public abstract class AbstractCommand implements Command {
    
    private final String description;
    private boolean executed = false;
    
    protected AbstractCommand(String description) {
        this.description = description != null ? description : "Unnamed Command";
    }
    
    @Override
    public void execute() {
        doExecute();
        executed = true;
    }
    
    @Override
    public void undo() {
        if (canUndo()) {
            doUndo();
            executed = false;
        }
    }
    
    @Override
    public boolean canUndo() {
        return executed;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    protected abstract void doExecute();
    
    protected abstract void doUndo();
}
