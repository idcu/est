package ltd.idcu.est.web;

import ltd.idcu.est.web.api.CorsMiddleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.List;

public class DefaultCorsMiddleware implements CorsMiddleware {

    private static final String NAME = "cors";
    private static final int PRIORITY = 50;

    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private boolean allowCredentials;
    private long maxAge;

    public DefaultCorsMiddleware() {
        this.allowedOrigins = new ArrayList<>();
        this.allowedOrigins.add("*");
        this.allowedMethods = new ArrayList<>();
        this.allowedMethods.add("GET");
        this.allowedMethods.add("POST");
        this.allowedMethods.add("PUT");
        this.allowedMethods.add("DELETE");
        this.allowedMethods.add("PATCH");
        this.allowedMethods.add("OPTIONS");
        this.allowedHeaders = new ArrayList<>();
        this.exposedHeaders = new ArrayList<>();
        this.allowCredentials = false;
        this.maxAge = 3600;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    @Override
    public void setAllowedOrigins(List<String> origins) {
        this.allowedOrigins = origins != null ? new ArrayList<>(origins) : new ArrayList<>();
    }

    @Override
    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    @Override
    public void setAllowedMethods(List<String> methods) {
        this.allowedMethods = methods != null ? new ArrayList<>(methods) : new ArrayList<>();
    }

    @Override
    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    @Override
    public void setAllowedHeaders(List<String> headers) {
        this.allowedHeaders = headers != null ? new ArrayList<>(headers) : new ArrayList<>();
    }

    @Override
    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    @Override
    public void setExposedHeaders(List<String> headers) {
        this.exposedHeaders = headers != null ? new ArrayList<>(headers) : new ArrayList<>();
    }

    @Override
    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    @Override
    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    @Override
    public long getMaxAge() {
        return maxAge;
    }

    @Override
    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public boolean isOriginAllowed(String origin) {
        return allowedOrigins.contains("*") || allowedOrigins.contains(origin);
    }

    @Override
    public boolean before(Request request, Response response) {
        String origin = request.getHeader("Origin");
        
        if (origin != null && !isOriginAllowed(origin)) {
            response.setStatus(403);
            response.json("{\"error\":\"Origin not allowed\"}");
            return false;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod().getMethod())) {
            response.setStatus(200);
            response.setHeader("Access-Control-Allow-Origin", getAllowedOrigins().contains("*") ? "*" : (getAllowedOrigins().isEmpty() ? "*" : getAllowedOrigins().get(0)));
            response.setHeader("Access-Control-Allow-Methods", String.join(",", getAllowedMethods()));
            if (allowCredentials) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            if (maxAge > 0) {
                response.setHeader("Access-Control-Max-Age", String.valueOf(maxAge));
            }
            if (!exposedHeaders.isEmpty()) {
                response.setHeader("Access-Control-Expose-Headers", String.join(",", exposedHeaders));
            }
            return true;
        }

        return true;
    }

    @Override
    public void after(Request request, Response response) {
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
