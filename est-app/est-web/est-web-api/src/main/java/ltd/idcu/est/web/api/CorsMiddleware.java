package ltd.idcu.est.web.api;

import java.util.List;

public interface CorsMiddleware extends Middleware {

    List<String> getAllowedOrigins();

    void setAllowedOrigins(List<String> origins);

    default void addAllowedOrigin(String origin) {
        List<String> origins = getAllowedOrigins();
        if (origins != null && !origins.contains(origin)) {
            origins.add(origin);
        }
    }

    default void allowAllOrigins() {
        setAllowedOrigins(List.of("*"));
    }

    List<String> getAllowedMethods();

    void setAllowedMethods(List<String> methods);

    default void addAllowedMethod(String method) {
        List<String> methods = getAllowedMethods();
        if (methods != null && !methods.contains(method)) {
            methods.add(method);
        }
    }

    default void allowAllMethods() {
        setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
    }

    List<String> getAllowedHeaders();

    void setAllowedHeaders(List<String> headers);

    default void addAllowedHeader(String header) {
        List<String> headers = getAllowedHeaders();
        if (headers != null && !headers.contains(header)) {
            headers.add(header);
        }
    }

    List<String> getExposedHeaders();

    void setExposedHeaders(List<String> headers);

    default void addExposedHeader(String header) {
        List<String> headers = getExposedHeaders();
        if (headers != null && !headers.contains(header)) {
            headers.add(header);
        }
    }

    boolean isAllowCredentials();

    void setAllowCredentials(boolean allowCredentials);

    long getMaxAge();

    void setMaxAge(long maxAge);

    default boolean isOriginAllowed(String origin) {
        List<String> origins = getAllowedOrigins();
        if (origins == null || origins.isEmpty()) {
            return false;
        }
        return origins.contains("*") || origins.contains(origin);
    }

    default boolean isMethodAllowed(String method) {
        List<String> methods = getAllowedMethods();
        if (methods == null || methods.isEmpty()) {
            return false;
        }
        return methods.contains("*") || methods.contains(method.toUpperCase());
    }

    default boolean isHeaderAllowed(String header) {
        List<String> headers = getAllowedHeaders();
        if (headers == null || headers.isEmpty()) {
            return false;
        }
        return headers.contains("*") || headers.contains(header.toLowerCase());
    }
}
