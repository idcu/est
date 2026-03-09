package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PerformanceUtilsTest {

    @BeforeEach
    public void setUp() {
        PerformanceUtils.clearTimings();
    }

    @AfterEach
    public void tearDown() {
        PerformanceUtils.clearTimings();
    }

    @Test
    public void testStartAndStopTiming() {
        String name = "test-timing";
        PerformanceUtils.startTiming(name);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long duration = PerformanceUtils.stopTiming(name);
        assertTrue(duration > 0);
    }

    @Test
    public void testStopTimingNonExistent() {
        long duration = PerformanceUtils.stopTiming("non-existent");
        assertEquals(-1, duration);
    }

    @Test
    public void testGetTiming() {
        String name = "test-get-timing";
        PerformanceUtils.startTiming(name);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long duration = PerformanceUtils.getTiming(name, TimeUnit.MILLISECONDS);
        assertTrue(duration >= 0);
    }

    @Test
    public void testGetAverageTiming() {
        String name = "test-average";
        for (int i = 0; i < 5; i++) {
            PerformanceUtils.startTiming(name);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            PerformanceUtils.stopTiming(name);
        }
        long average = PerformanceUtils.getAverageTiming(name, TimeUnit.MILLISECONDS);
        assertTrue(average >= 0);
    }

    @Test
    public void testGetAverageTimingNonExistent() {
        long average = PerformanceUtils.getAverageTiming("non-existent", TimeUnit.MILLISECONDS);
        assertEquals(-1, average);
    }

    @Test
    public void testGetMemorySnapshot() {
        PerformanceUtils.MemorySnapshot snapshot = PerformanceUtils.getMemorySnapshot();
        assertNotNull(snapshot);
        assertTrue(snapshot.getHeapInit() >= 0);
        assertTrue(snapshot.getHeapUsed() >= 0);
        assertTrue(snapshot.getHeapCommitted() >= 0);
        assertTrue(snapshot.getHeapMax() >= 0);
        assertTrue(snapshot.getNonHeapInit() >= 0);
        assertTrue(snapshot.getNonHeapUsed() >= 0);
        assertTrue(snapshot.getNonHeapCommitted() >= 0);
        assertTrue(snapshot.getNonHeapMax() >= 0);
        assertTrue(snapshot.getTimestamp() > 0);
    }

    @Test
    public void testGetUsedHeapMemory() {
        long used = PerformanceUtils.getUsedHeapMemory();
        assertTrue(used >= 0);
    }

    @Test
    public void testGetMaxHeapMemory() {
        long max = PerformanceUtils.getMaxHeapMemory();
        assertTrue(max > 0);
    }

    @Test
    public void testGetHeapMemoryUsagePercent() {
        double percent = PerformanceUtils.getHeapMemoryUsagePercent();
        assertTrue(percent >= 0 && percent <= 100);
    }

    @Test
    public void testGetMemoryOptimizationTips() {
        var tips = PerformanceUtils.getMemoryOptimizationTips();
        assertNotNull(tips);
        assertFalse(tips.isEmpty());
    }

    @Test
    public void testGetStartupOptimizationTips() {
        var tips = PerformanceUtils.getStartupOptimizationTips();
        assertNotNull(tips);
        assertFalse(tips.isEmpty());
    }

    @Test
    public void testGetTimingStats() {
        String name1 = "test-stats-1";
        String name2 = "test-stats-2";
        
        for (int i = 0; i < 3; i++) {
            PerformanceUtils.startTiming(name1);
            PerformanceUtils.stopTiming(name1);
        }
        
        for (int i = 0; i < 2; i++) {
            PerformanceUtils.startTiming(name2);
            PerformanceUtils.stopTiming(name2);
        }
        
        var stats = PerformanceUtils.getTimingStats();
        assertNotNull(stats);
        assertTrue(stats.containsKey(name1));
        assertTrue(stats.containsKey(name2));
    }

    @Test
    public void testClearTimings() {
        String name = "test-clear";
        PerformanceUtils.startTiming(name);
        PerformanceUtils.stopTiming(name);
        
        var statsBefore = PerformanceUtils.getTimingStats();
        assertFalse(statsBefore.isEmpty());
        
        PerformanceUtils.clearTimings();
        
        var statsAfter = PerformanceUtils.getTimingStats();
        assertTrue(statsAfter.isEmpty());
    }
}
