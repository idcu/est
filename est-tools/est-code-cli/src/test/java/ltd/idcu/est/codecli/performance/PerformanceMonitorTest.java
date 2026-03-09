package ltd.idcu.est.codecli.performance;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.List;
import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class PerformanceMonitorTest {

    private PerformanceMonitor monitor;

    @BeforeEach
    void beforeEach() {
        monitor = new PerformanceMonitor();
    }

    @Test
    void testRecordSingleMeasurement() {
        monitor.record("test-op", 1000000);
        
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(1, snapshot.count());
        assertEquals(1000000, snapshot.totalTimeNs());
        assertEquals(1000000, snapshot.minTimeNs());
        assertEquals(1000000, snapshot.maxTimeNs());
        assertEquals(1000000.0, snapshot.avgTimeNs());
    }

    @Test
    void testRecordMultipleMeasurements() {
        monitor.record("test-op", 1000000);
        monitor.record("test-op", 2000000);
        monitor.record("test-op", 3000000);
        
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(3, snapshot.count());
        assertEquals(6000000, snapshot.totalTimeNs());
        assertEquals(1000000, snapshot.minTimeNs());
        assertEquals(3000000, snapshot.maxTimeNs());
        assertEquals(2000000.0, snapshot.avgTimeNs());
    }

    @Test
    void testGetSnapshotNonExistent() {
        assertNull(monitor.getSnapshot("non-existent"));
    }

    @Test
    void testMeasureWithSupplier() {
        String result = monitor.measure("test-op", () -> "test-value");
        
        assertEquals("test-value", result);
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(1, snapshot.count());
        assertTrue(snapshot.totalTimeNs() > 0);
    }

    @Test
    void testMeasureWithRunnable() {
        final int[] counter = {0};
        
        monitor.measure("test-op", () -> {
            counter[0]++;
        });
        
        assertEquals(1, counter[0]);
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(1, snapshot.count());
        assertTrue(snapshot.totalTimeNs() > 0);
    }

    @Test
    void testGetAllSnapshots() {
        monitor.record("op1", 1000000);
        monitor.record("op2", 2000000);
        
        Map<String, PerformanceMonitor.MetricSnapshot> snapshots = monitor.getAllSnapshots();
        assertNotNull(snapshots);
        assertEquals(2, snapshots.size());
        assertTrue(snapshots.containsKey("op1"));
        assertTrue(snapshots.containsKey("op2"));
    }

    @Test
    void testGetAllSnapshotsEmpty() {
        Map<String, PerformanceMonitor.MetricSnapshot> snapshots = monitor.getAllSnapshots();
        assertNotNull(snapshots);
        assertTrue(snapshots.isEmpty());
    }

    @Test
    void testGetUptime() {
        long uptimeNs = monitor.getUptimeNs();
        double uptimeMs = monitor.getUptimeMs();
        
        assertTrue(uptimeNs > 0);
        assertTrue(uptimeMs > 0);
        assertEquals(uptimeNs / 1_000_000.0, uptimeMs, 0.001);
    }

    @Test
    void testResetAll() {
        monitor.record("op1", 1000000);
        monitor.record("op2", 2000000);
        
        monitor.reset();
        
        assertNull(monitor.getSnapshot("op1"));
        assertNull(monitor.getSnapshot("op2"));
        assertTrue(monitor.getAllSnapshots().isEmpty());
    }

    @Test
    void testResetSingleOperation() {
        monitor.record("op1", 1000000);
        monitor.record("op2", 2000000);
        
        monitor.reset("op1");
        
        assertNull(monitor.getSnapshot("op1"));
        assertNotNull(monitor.getSnapshot("op2"));
    }

    @Test
    void testGetOperationNames() {
        monitor.record("op1", 1000000);
        monitor.record("op2", 2000000);
        monitor.record("op3", 3000000);
        
        List<String> names = monitor.getOperationNames();
        assertNotNull(names);
        assertEquals(3, names.size());
        assertTrue(names.contains("op1"));
        assertTrue(names.contains("op2"));
        assertTrue(names.contains("op3"));
    }

    @Test
    void testGetOperationNamesEmpty() {
        List<String> names = monitor.getOperationNames();
        assertNotNull(names);
        assertTrue(names.isEmpty());
    }

    @Test
    void testGenerateReport() {
        monitor.record("test-op", 1000000);
        
        String report = monitor.generateReport();
        assertNotNull(report);
        assertTrue(report.contains("Performance Report"));
        assertTrue(report.contains("test-op"));
        assertTrue(report.contains("Count: 1"));
    }

    @Test
    void testGenerateReportEmpty() {
        String report = monitor.generateReport();
        assertNotNull(report);
        assertTrue(report.contains("Performance Report"));
    }

    @Test
    void testMetricSnapshotTimeConversions() {
        monitor.record("test-op", 2000000);
        
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(2000000, snapshot.totalTimeNs());
        assertEquals(2.0, snapshot.totalTimeMs(), 0.001);
        assertEquals(2000000, snapshot.minTimeNs());
        assertEquals(2.0, snapshot.minTimeMs(), 0.001);
        assertEquals(2000000, snapshot.maxTimeNs());
        assertEquals(2.0, snapshot.maxTimeMs(), 0.001);
        assertEquals(2000000.0, snapshot.avgTimeNs());
        assertEquals(2.0, snapshot.avgTimeMs(), 0.001);
    }

    @Test
    void testMinTimeCalculation() {
        monitor.record("test-op", 3000000);
        monitor.record("test-op", 1000000);
        monitor.record("test-op", 2000000);
        
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(1000000, snapshot.minTimeNs());
    }

    @Test
    void testMaxTimeCalculation() {
        monitor.record("test-op", 1000000);
        monitor.record("test-op", 3000000);
        monitor.record("test-op", 2000000);
        
        PerformanceMonitor.MetricSnapshot snapshot = monitor.getSnapshot("test-op");
        assertNotNull(snapshot);
        assertEquals(3000000, snapshot.maxTimeNs());
    }

    @Test
    void testZeroMeasurementsSnapshot() {
        monitor.reset("test-op");
        assertNull(monitor.getSnapshot("test-op"));
    }

    @Test
    void testMultipleOperations() {
        monitor.record("op1", 1000000);
        monitor.record("op2", 2000000);
        monitor.record("op1", 1500000);
        monitor.record("op2", 2500000);
        
        PerformanceMonitor.MetricSnapshot snapshot1 = monitor.getSnapshot("op1");
        PerformanceMonitor.MetricSnapshot snapshot2 = monitor.getSnapshot("op2");
        
        assertNotNull(snapshot1);
        assertNotNull(snapshot2);
        assertEquals(2, snapshot1.count());
        assertEquals(2, snapshot2.count());
        assertEquals(2500000, snapshot1.totalTimeNs());
        assertEquals(4500000, snapshot2.totalTimeNs());
    }
}
