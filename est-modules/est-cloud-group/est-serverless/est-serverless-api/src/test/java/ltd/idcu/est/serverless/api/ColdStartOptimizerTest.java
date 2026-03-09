package ltd.idcu.est.serverless.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ColdStartOptimizerTest {

    private ColdStartOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = ColdStartOptimizer.getInstance();
        optimizer.reset();
    }

    @AfterEach
    void tearDown() {
        optimizer.reset();
    }

    @Test
    void testGetInstance() {
        ColdStartOptimizer instance1 = ColdStartOptimizer.getInstance();
        ColdStartOptimizer instance2 = ColdStartOptimizer.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testRecordColdStart() {
        optimizer.recordColdStart("test-function");
        
        Map<String, Object> stats = optimizer.getStatistics();
        assertEquals(1L, stats.get("totalColdStarts"));
        assertEquals(0L, stats.get("totalWarmStarts"));
    }

    @Test
    void testRecordWarmStart() {
        optimizer.recordWarmStart("test-function");
        
        Map<String, Object> stats = optimizer.getStatistics();
        assertEquals(0L, stats.get("totalColdStarts"));
        assertEquals(1L, stats.get("totalWarmStarts"));
    }

    @Test
    void testColdStartRate() {
        optimizer.recordColdStart("func1");
        optimizer.recordColdStart("func1");
        optimizer.recordWarmStart("func1");
        optimizer.recordWarmStart("func1");
        optimizer.recordWarmStart("func1");
        
        Map<String, Object> stats = optimizer.getStatistics();
        double rate = (Double) stats.get("coldStartRate");
        assertEquals(0.4, rate, 0.001);
    }

    @Test
    void testShouldWarmupNewFunction() {
        assertTrue(optimizer.shouldWarmup("new-function"));
    }

    @Test
    void testPreWarm() {
        AtomicBoolean warmed = new AtomicBoolean(false);
        
        optimizer.preWarm("test-function", () -> {
            warmed.set(true);
        });
        
        assertTrue(warmed.get());
        assertFalse(optimizer.shouldWarmup("test-function"));
    }

    @Test
    void testReset() {
        optimizer.recordColdStart("func1");
        optimizer.recordWarmStart("func2");
        
        optimizer.reset();
        
        Map<String, Object> stats = optimizer.getStatistics();
        assertEquals(0L, stats.get("totalColdStarts"));
        assertEquals(0L, stats.get("totalWarmStarts"));
    }

    @Test
    void testMultipleFunctions() {
        optimizer.recordColdStart("func1");
        optimizer.recordColdStart("func1");
        optimizer.recordWarmStart("func1");
        optimizer.recordColdStart("func2");
        optimizer.recordWarmStart("func2");
        optimizer.recordWarmStart("func2");
        
        Map<String, Object> stats = optimizer.getStatistics();
        assertEquals(3L, stats.get("totalColdStarts"));
        assertEquals(4L, stats.get("totalWarmStarts"));
    }
}
