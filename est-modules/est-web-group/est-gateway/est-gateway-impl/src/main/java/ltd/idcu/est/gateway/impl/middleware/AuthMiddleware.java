package ltd.idcu.est.gateway.impl.middleware;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.GatewayMiddleware;

import java.util.Base64;
import java.util.Set;
import java.util.function.Function;

public class AuthMiddleware implements GatewayMiddleware {
    private static final String NAME = "auth";
    private final AuthValidator validator;
    private final Set<String> excludedPaths;

    public interface AuthValidator {
        boolean validate(String token);
        String getUserId(String token);
    }

    public AuthMiddleware(AuthValidator validator) {
        this(validator, Set.of());
    }

    public AuthMiddleware(AuthValidator validator, Set<String> excludedPaths) {
        this.validator = validator;
        this.excludedPaths = excludedPaths;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            String path = context.getRequestPath();
            
            if (isExcluded(path)) {
                return context;
            }

            String authHeader = context.getRequestHeaders().get("Authorization");
            if (authHeader == null || authHeader.isEmpty()) {
                context.setResponseStatus(401);
                context.setResponseBody("Unauthorized: Missing Authorization header".getBytes());
                context.setAttribute("shouldAbort", true);
                return context;
            }

            String token = extractToken(authHeader);
            if (token == null) {
                context.setResponseStatus(401);
                context.setResponseBody("Unauthorized: Invalid Authorization header format".getBytes());
                context.setAttribute("shouldAbort", true);
                return context;
            }

            if (!validator.validate(token)) {
                context.setResponseStatus(401);
                context.setResponseBody("Unauthorized: Invalid token".getBytes());
                context.setAttribute("shouldAbort", true);
                return context;
            }

            String userId = validator.getUserId(token);
            context.setAttribute("userId", userId);

            return context;
        };
    }

    private boolean isExcluded(String path) {
        for (String excluded : excludedPaths) {
            if (excluded.endsWith("*")) {
                String prefix = excluded.substring(0, excluded.length() - 1);
                if (path.startsWith(prefix)) {
                    return true;
                }
            } else if (path.equals(excluded)) {
                return true;
            }
        }
        return false;
    }

    private String extractToken(String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else if (authHeader.startsWith("Basic ")) {
            return authHeader.substring(6);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -80;
    }
}
