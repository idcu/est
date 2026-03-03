package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.CorsMiddleware;
import ltd.idcu.est.web.api.HttpStatus;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.List;

public class DefaultCorsMiddleware implements CorsMiddleware {

    private static final String NAME = "cors";
    private static final int PRIORITY = 100;

    private List<String> allowedOrigins = new ArrayList<>();
    private List<String> allowedMethods = new ArrayList<>();
    private List<String> allowedHeaders = new ArrayList<>();
    private List<String> exposedHeaders = new ArrayList<>();
    private boolean allowCredentials = false;
    private long maxAge = 86400;

    public DefaultCorsMiddleware() {
        allowAllOrigins();
        allowAllMethods();
        allowedHeaders.add("*");
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
        this.allowedOrigins = origins;
    }

    @Override
    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    @Override
    public void setAllowedMethods(List<String> methods) {
        this.allowedMethods = methods;
    }

    @Override
    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    @Override
    public void setAllowedHeaders(List<String> headers) {
        this.allowedHeaders = headers;
    }

    @Override
    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    @Override
    public void setExposedHeaders(List<String> headers) {
        this.exposedHeaders = headers;
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
    public boolean before(Request request, Response response) {
        String origin = request.getHeader("Origin");
        
        if (origin != null && isOriginAllowed(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            
            if (allowCredentials) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            
            if (!exposedHeaders.isEmpty()) {
                response.setHeader("Access-Control-Expose-Headers", String.join(", ", exposedHeaders));
            }
            
            if ("OPTIONS".equals(request.getMethod())) {
                response.setHeader("Access-Control-Allow-Methods", String.join(", ", allowedMethods));
                response.setHeader("Access-Control-Allow-Headers", String.join(", ", allowedHeaders));
                response.setHeader("Access-Control-Max-Age", String.valueOf(maxAge));
                response.setStatus(HttpStatus.NO_CONTENT);
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
