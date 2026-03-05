package ltd.idcu.est.web;

import ltd.idcu.est.web.api.HttpStatus;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityMiddleware implements Middleware {

    private static final String NAME = "security";
    private static final int PRIORITY = 300;

    private final List<String> excludedPaths;
    private final Map<String, String> userCredentials;
    private String realm = "EST Web Application";

    public SecurityMiddleware() {
        this.excludedPaths = new ArrayList<>();
        this.userCredentials = new ConcurrentHashMap<>();
        excludedPaths.add("/public");
        excludedPaths.add("/static");
        excludedPaths.add("/favicon.ico");
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
    public boolean before(Request request, Response response) {
        String path = request.getPath();
        
        for (String excludedPath : excludedPaths) {
            if (path.startsWith(excludedPath)) {
                return true;
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return false;
        }

        try {
            String encoded = authHeader.substring(6);
            String decoded = new String(Base64.getDecoder().decode(encoded));
            int colonIndex = decoded.indexOf(':');
            if (colonIndex == -1) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                return false;
            }

            String username = decoded.substring(0, colonIndex);
            String password = decoded.substring(colonIndex + 1);

            if (!userCredentials.containsKey(username) || !userCredentials.get(username).equals(password)) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                return false;
            }

            return true;

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void onError(Request request, Response response, Exception e) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
    }

    public void addUser(String username, String password) {
        userCredentials.put(username, password);
    }

    public void addExcludedPath(String path) {
        excludedPaths.add(path);
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
