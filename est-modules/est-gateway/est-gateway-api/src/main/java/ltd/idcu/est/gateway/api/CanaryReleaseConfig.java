package ltd.idcu.est.gateway.api;

import java.util.List;
import java.util.Map;

public class CanaryReleaseConfig {
    private String serviceId;
    private String primaryVersion;
    private String canaryVersion;
    private int canaryPercentage;
    private Map<String, List<String>> headerMatchers;
    private List<String> cookieMatchers;
    private List<String> ipMatchers;

    public CanaryReleaseConfig() {
    }

    public CanaryReleaseConfig(String serviceId, String primaryVersion, String canaryVersion, int canaryPercentage) {
        this.serviceId = serviceId;
        this.primaryVersion = primaryVersion;
        this.canaryVersion = canaryVersion;
        this.canaryPercentage = canaryPercentage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPrimaryVersion() {
        return primaryVersion;
    }

    public void setPrimaryVersion(String primaryVersion) {
        this.primaryVersion = primaryVersion;
    }

    public String getCanaryVersion() {
        return canaryVersion;
    }

    public void setCanaryVersion(String canaryVersion) {
        this.canaryVersion = canaryVersion;
    }

    public int getCanaryPercentage() {
        return canaryPercentage;
    }

    public void setCanaryPercentage(int canaryPercentage) {
        if (canaryPercentage < 0 || canaryPercentage > 100) {
            throw new IllegalArgumentException("Canary percentage must be between 0 and 100");
        }
        this.canaryPercentage = canaryPercentage;
    }

    public Map<String, List<String>> getHeaderMatchers() {
        return headerMatchers;
    }

    public void setHeaderMatchers(Map<String, List<String>> headerMatchers) {
        this.headerMatchers = headerMatchers;
    }

    public List<String> getCookieMatchers() {
        return cookieMatchers;
    }

    public void setCookieMatchers(List<String> cookieMatchers) {
        this.cookieMatchers = cookieMatchers;
    }

    public List<String> getIpMatchers() {
        return ipMatchers;
    }

    public void setIpMatchers(List<String> ipMatchers) {
        this.ipMatchers = ipMatchers;
    }
}
