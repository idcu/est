package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class DefaultCommandInvokerTest {

    @Test
    public void testExecuteCommand() {
        DefaultCommandInvoker invoker = new DefaultCommandInvoker();
        TestCommand command = new TestCommand("Test Command");
        
        invoker.executeCommand(command);
        
        Assertions.assertTrue(command.isExecuted());
        Assertions.assertTrue(invoker.canUndo());
        Assertions.assertFalse(invoker.canRedo());
        Assertions.assertEquals(1, invoker.getHistory().size());
    }

    @Test
    public void testUndoAndRedo() {
        DefaultCommandInvoker invoker = new DefaultCommandInvoker();
        TestCommand command = new TestCommand("Test Command");
        
        invoker.executeCommand(command);
        Assertions.assertTrue(command.isExecuted());
        
        invoker.undoLast();
        Assertions.assertFalse(command.isExecuted());
        Assertions.assertFalse(invoker.canUndo());
        Assertions.assertTrue(invoker.canRedo());
        
        invoker.redoLast();
        Assertions.assertTrue(command.isExecuted());
        Assertions.assertTrue(invoker.canUndo());
        Assertions.assertFalse(invoker.canRedo());
    }

    @Test
    public void testClearHistory() {
        DefaultCommandInvoker invoker = new DefaultCommandInvoker();
        TestCommand command1 = new TestCommand("Command 1");
        TestCommand command2 = new TestCommand("Command 2");
        
        invoker.executeCommand(command1);
        invoker.executeCommand(command2);
        
        Assertions.assertEquals(2, invoker.getHistory().size());
        
        invoker.clearHistory();
        
        Assertions.assertFalse(invoker.canUndo());
        Assertions.assertFalse(invoker.canRedo());
        Assertions.assertEquals(0, invoker.getHistory().size());
    }

    @Test
    public void testMaxHistorySize() {
        DefaultCommandInvoker invoker = new DefaultCommandInvoker(2);
        TestCommand command1 = new TestCommand("Command 1");
        TestCommand command2 = new TestCommand("Command 2");
        TestCommand command3 = new TestCommand("Command 3");
        
        invoker.executeCommand(command1);
        invoker.executeCommand(command2);
        invoker.executeCommand(command3);
        
        Assertions.assertEquals(2, invoker.getHistory().size());
    }

    @Test
    public void testExecuteNullCommandThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DefaultCommandInvoker invoker = new DefaultCommandInvoker();
            invoker.executeCommand(null);
        });
    }

    private static class TestCommand extends AbstractCommand {
        private boolean executed = false;

        TestCommand(String description) {
            super(description);
        }

        @Override
        protected void doExecute() {
            this.executed = true;
        }

        @Override
        protected void doUndo() {
            this.executed = false;
        }

        boolean isExecuted() {
            return executed;
        }
    }
}
