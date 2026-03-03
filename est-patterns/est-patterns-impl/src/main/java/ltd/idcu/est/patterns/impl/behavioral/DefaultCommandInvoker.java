package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Command;
import ltd.idcu.est.patterns.api.behavioral.CommandInvoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DefaultCommandInvoker implements CommandInvoker {
    
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private final List<String> history = new ArrayList<>();
    private final int maxHistorySize;
    
    public DefaultCommandInvoker() {
        this(100);
    }
    
    public DefaultCommandInvoker(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize > 0 ? maxHistorySize : 100;
    }
    
    @Override
    public void executeCommand(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        addToHistory(command.getDescription());
    }
    
    @Override
    public void undoLast() {
        if (!canUndo()) {
            return;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        addToHistory("Undo: " + command.getDescription());
    }
    
    @Override
    public void redoLast() {
        if (!canRedo()) {
            return;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        addToHistory("Redo: " + command.getDescription());
    }
    
    @Override
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    @Override
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    @Override
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
    
    @Override
    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        history.clear();
    }
    
    private void addToHistory(String entry) {
        if (history.size() >= maxHistorySize) {
            history.remove(0);
        }
        history.add(entry);
    }
}
