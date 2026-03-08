package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.CanaryReleaseConfig;
import ltd.idcu.est.gateway.api.CanaryReleaseManager;
import ltd.idcu.est.gateway.api.GatewayContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultCanaryReleaseManager implements CanaryReleaseManager {
    private final Map<String, CanaryReleaseConfig> configs;
    private final Map<String, Map<String, Long>> stickySessions;

    public DefaultCanaryReleaseManager() {
        this.configs = new ConcurrentHashMap<>();
        this.stickySessions = new ConcurrentHashMap<>();
    }

    @Override
    public void addConfig(CanaryReleaseConfig config) {
        configs.put(config.getServiceId(), config);
    }

    @Override
    public void removeConfig(String serviceId) {
        configs.remove(serviceId);
        stickySessions.remove(serviceId);
    }

    @Override
    public CanaryReleaseConfig getConfig(String serviceId) {
        return configs.get(serviceId);
    }

    @Override
    public Map<String, CanaryReleaseConfig> getAllConfigs() {
        return Collections.unmodifiableMap(configs);
    }

    @Override
    public boolean shouldRouteToCanary(String serviceId, GatewayContext context) {
        CanaryReleaseConfig config = configs.get(serviceId);
        if (config == null) {
            return false;
        }

        if (checkHeaderMatchers(config, context)) {
            return true;
        }

        if (checkCookieMatchers(config, context)) {
            return true;
        }

        if (checkIpMatchers(config, context)) {
            return true;
        }

        String sessionKey = getSessionKey(context);
        if (sessionKey != null) {
            Map<String, Long> serviceStickySessions = stickySessions.computeIfAbsent(
                serviceId, k -> new ConcurrentHashMap<>());
            Long stickyExpiry = serviceStickySessions.get(sessionKey);
            if (stickyExpiry != null && System.currentTimeMillis() < stickyExpiry) {
                return stickyExpiry % 2 == 1;
            }
        }

        int random = ThreadLocalRandom.current().nextInt(100);
        boolean isCanary = random < config.getCanaryPercentage();

        if (sessionKey != null) {
            Map<String, Long> serviceStickySessions = stickySessions.computeIfAbsent(
                serviceId, k -> new ConcurrentHashMap<>());
            long expiry = System.currentTimeMillis() + 3600000;
            if (isCanary) {
                expiry |= 1;
            }
            serviceStickySessions.put(sessionKey, expiry);
        }

        return isCanary;
    }

    @Override
    public String selectTargetVersion(String serviceId, GatewayContext context) {
        CanaryReleaseConfig config = configs.get(serviceId);
        if (config == null) {
            return null;
        }

        return shouldRouteToCanary(serviceId, context) ? 
            config.getCanaryVersion() : config.getPrimaryVersion();
    }

    private boolean checkHeaderMatchers(CanaryReleaseConfig config, GatewayContext context) {
        Map<String, List<String>> headerMatchers = config.getHeaderMatchers();
        if (headerMatchers == null || headerMatchers.isEmpty()) {
            return false;
        }

        Map<String, String> requestHeaders = context.getRequestHeaders();
        for (Map.Entry<String, List<String>> entry : headerMatchers.entrySet()) {
            String headerName = entry.getKey();
            List<String> expectedValues = entry.getValue();
            String actualValue = requestHeaders.get(headerName);
            
            if (actualValue != null) {
                for (String expectedValue : expectedValues) {
                    if (actualValue.equalsIgnoreCase(expectedValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkCookieMatchers(CanaryReleaseConfig config, GatewayContext context) {
        List<String> cookieMatchers = config.getCookieMatchers();
        if (cookieMatchers == null || cookieMatchers.isEmpty()) {
            return false;
        }

        Map<String, String> requestHeaders = context.getRequestHeaders();
        String cookieHeader = requestHeaders.get("Cookie");
        if (cookieHeader == null) {
            return false;
        }

        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            String[] parts = cookie.trim().split("=", 2);
            if (parts.length == 2) {
                String name = parts[0].trim();
                if (cookieMatchers.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIpMatchers(CanaryReleaseConfig config, GatewayContext context) {
        List<String> ipMatchers = config.getIpMatchers();
        if (ipMatchers == null || ipMatchers.isEmpty()) {
            return false;
        }

        Map<String, String> requestHeaders = context.getRequestHeaders();
        String xForwardedFor = requestHeaders.get("X-Forwarded-For");
        String clientIp = xForwardedFor != null ? 
            xForwardedFor.split(",")[0].trim() : null;

        if (clientIp != null) {
            for (String ipMatcher : ipMatchers) {
                if (ipMatches(clientIp, ipMatcher)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ipMatches(String clientIp, String ipPattern) {
        if (ipPattern.contains("*")) {
            String pattern = ipPattern.replace("*", ".*");
            return clientIp.matches(pattern);
        }
        if (ipPattern.contains("/")) {
            String[] parts = ipPattern.split("/");
            String network = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);
            return ipInSubnet(clientIp, network, prefixLength);
        }
        return clientIp.equals(ipPattern);
    }

    private boolean ipInSubnet(String ip, String network, int prefixLength) {
        try {
            int ipInt = ipToInt(ip);
            int networkInt = ipToInt(network);
            int mask = 0xFFFFFFFF << (32 - prefixLength);
            return (ipInt & mask) == (networkInt & mask);
        } catch (Exception e) {
            return false;
        }
    }

    private int ipToInt(String ip) {
        String[] octets = ip.split("\\.");
        return ((Integer.parseInt(octets[0]) & 0xFF) << 24) |
               ((Integer.parseInt(octets[1]) & 0xFF) << 16) |
               ((Integer.parseInt(octets[2]) & 0xFF) << 8) |
               (Integer.parseInt(octets[3]) & 0xFF);
    }

    private String getSessionKey(GatewayContext context) {
        Map<String, String> headers = context.getRequestHeaders();
        String cookieHeader = headers.get("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                String[] parts = cookie.trim().split("=", 2);
                if (parts.length == 2 && 
                    ("JSESSIONID".equals(parts[0]) || "sessionid".equals(parts[0]))) {
                    return parts[1];
                }
            }
        }
        return headers.get("Authorization");
    }
}
