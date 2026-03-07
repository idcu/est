package ltd.idcu.est.gateway.api;

import java.util.Map;

public interface CanaryReleaseManager {
    void addConfig(CanaryReleaseConfig config);

    void removeConfig(String serviceId);

    CanaryReleaseConfig getConfig(String serviceId);

    Map<String, CanaryReleaseConfig> getAllConfigs();

    boolean shouldRouteToCanary(String serviceId, GatewayContext context);

    String selectTargetVersion(String serviceId, GatewayContext context);
}
