package ltd.idcu.est.codecli.ux;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class CommandHistoryTest {

    private CommandHistory history;

    @BeforeEach
    void beforeEach() {
        history = new CommandHistory();
    }

    @Test
    void testDefaultMaxSize() {
        assertEquals(0, history.size());
    }

    @Test
    void testCustomMaxSize() {
        CommandHistory customHistory = new CommandHistory(50);
        assertEquals(0, customHistory.size());
    }

    @Test
    void testAddCommand() {
        history.add("command1");
        assertEquals(1, history.size());
    }

    @Test
    void testAddNullCommand() {
        history.add(null);
        assertEquals(0, history.size());
    }

    @Test
    void testAddEmptyCommand() {
        history.add("");
        assertEquals(0, history.size());
    }

    @Test
    void testAddWhitespaceCommand() {
        history.add("   ");
        assertEquals(0, history.size());
    }

    @Test
    void testAddDuplicateCommands() {
        history.add("command1");
        history.add("command1");
        assertEquals(1, history.size());
    }

    @Test
    void testAddDuplicateWithWhitespace() {
        history.add("command1");
        history.add("  command1  ");
        assertEquals(1, history.size());
    }

    @Test
    void testGetPreviousEmpty() {
        assertNull(history.getPrevious());
    }

    @Test
    void testGetNextEmpty() {
        assertNull(history.getNext());
    }

    @Test
    void testGetPreviousSingle() {
        history.add("command1");
        assertEquals("command1", history.getPrevious());
        assertNull(history.getPrevious());
    }

    @Test
    void testGetNextSingle() {
        history.add("command1");
        assertNull(history.getNext());
    }

    @Test
    void testNavigateBackAndForth() {
        history.add("command1");
        history.add("command2");
        history.add("command3");
        
        assertEquals("command3", history.getPrevious());
        assertEquals("command2", history.getPrevious());
        assertEquals("command1", history.getPrevious());
        assertNull(history.getPrevious());
        
        assertEquals("command2", history.getNext());
        assertEquals("command3", history.getNext());
        assertNull(history.getNext());
    }

    @Test
    void testGetAll() {
        history.add("command1");
        history.add("command2");
        history.add("command3");
        
        List<String> all = history.getAll();
        assertNotNull(all);
        assertEquals(3, all.size());
        assertEquals("command1", all.get(0));
        assertEquals("command2", all.get(1));
        assertEquals("command3", all.get(2));
    }

    @Test
    void testGetAllEmpty() {
        List<String> all = history.getAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    void testClear() {
        history.add("command1");
        history.add("command2");
        history.clear();
        
        assertEquals(0, history.size());
        assertTrue(history.getAll().isEmpty());
        assertNull(history.getPrevious());
        assertNull(history.getNext());
    }

    @Test
    void testClearEmpty() {
        history.clear();
        assertEquals(0, history.size());
    }

    @Test
    void testSize() {
        assertEquals(0, history.size());
        history.add("command1");
        assertEquals(1, history.size());
        history.add("command2");
        assertEquals(2, history.size());
    }

    @Test
    void testSearch() {
        history.add("git status");
        history.add("git commit");
        history.add("git push");
        history.add("mvn clean install");
        
        List<String> results = history.search("git");
        assertNotNull(results);
        assertEquals(3, results.size());
        assertTrue(results.contains("git status"));
        assertTrue(results.contains("git commit"));
        assertTrue(results.contains("git push"));
    }

    @Test
    void testSearchCaseInsensitive() {
        history.add("Git Status");
        history.add("git commit");
        
        List<String> results = history.search("GIT");
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void testSearchNoResults() {
        history.add("command1");
        history.add("command2");
        
        List<String> results = history.search("nonexistent");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchEmpty() {
        List<String> results = history.search("anything");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testMaxSizeLimit() {
        CommandHistory smallHistory = new CommandHistory(3);
        smallHistory.add("command1");
        smallHistory.add("command2");
        smallHistory.add("command3");
        assertEquals(3, smallHistory.size());
        
        smallHistory.add("command4");
        assertEquals(3, smallHistory.size());
        
        List<String> all = smallHistory.getAll();
        assertEquals("command2", all.get(0));
        assertEquals("command3", all.get(1));
        assertEquals("command4", all.get(2));
    }

    @Test
    void testNavigateAfterMaxSize() {
        CommandHistory smallHistory = new CommandHistory(3);
        smallHistory.add("command1");
        smallHistory.add("command2");
        smallHistory.add("command3");
        smallHistory.add("command4");
        
        assertEquals("command4", smallHistory.getPrevious());
        assertEquals("command3", smallHistory.getPrevious());
        assertEquals("command2", smallHistory.getPrevious());
        assertNull(smallHistory.getPrevious());
    }

    @Test
    void testTrimWhitespace() {
        history.add("  command with spaces  ");
        List<String> all = history.getAll();
        assertEquals("command with spaces", all.get(0));
    }

    @Test
    void testMultipleCommands() {
        for (int i = 1; i <= 10; i++) {
            history.add("command" + i);
        }
        assertEquals(10, history.size());
    }
}
