package ltd.idcu.est.features.monitor.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DiskSpaceHealthCheck implements HealthCheck {
    private final double minFreeSpacePercent;
    private final String path;

    public DiskSpaceHealthCheck() {
        this("/", 10.0);
    }

    public DiskSpaceHealthCheck(String path, double minFreeSpacePercent) {
        this.path = path;
        this.minFreeSpacePercent = minFreeSpacePercent;
    }

    @Override
    public HealthStatus check() {
        File file = new File(path);
        if (!file.exists()) {
            return HealthStatus.unhealthy("Path does not exist: " + path);
        }

        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usableSpace = file.getUsableSpace();

        double freeSpacePercent = (double) freeSpace / totalSpace * 100;

        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("totalSpace", totalSpace);
        details.put("freeSpace", freeSpace);
        details.put("usableSpace", usableSpace);
        details.put("freeSpacePercent", freeSpacePercent);

        if (freeSpacePercent < minFreeSpacePercent) {
            return HealthStatus.unhealthy("Disk free space below " + minFreeSpacePercent + "%: " + String.format("%.1f", freeSpacePercent) + "%", details);
        } else if (freeSpacePercent < minFreeSpacePercent * 1.5) {
            return HealthStatus.degraded("Disk free space is low: " + String.format("%.1f", freeSpacePercent) + "%", details);
        } else {
            return HealthStatus.healthy("Disk free space is normal: " + String.format("%.1f", freeSpacePercent) + "%", details);
        }
    }

    @Override
    public HealthStatus getStatus() {
        return check();
    }

    @Override
    public String getName() {
        return "disk";
    }

    @Override
    public String getDescription() {
        return "Disk space health check for path: " + path;
    }
}
