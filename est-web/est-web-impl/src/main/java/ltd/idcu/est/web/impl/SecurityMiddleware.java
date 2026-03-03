package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.HttpStatus;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityMiddleware implements Middleware {

    private static final String NAME = "security";
    private static final int PRIORITY = 300;

    private List<String> excludedPaths = new ArrayList<>();
    private Map<String, String> userCredentials = new ConcurrentHashMap<>();
    private String realm = "EST Web Application";

    public SecurityMiddleware() {
        // 添加默认排除路径
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
        
        // 检查是否为排除路径
        for (String excludedPath : excludedPaths) {
            if (path.startsWith(excludedPath)) {
                return true;
            }
        }

        // 检查认证头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return false;
        }

        // 解析认证信息
        try {
            String encoded = authHeader.substring(6);
            String decoded = new String(java.util.Base64.getDecoder().decode(encoded));
            int colonIndex = decoded.indexOf(':');
            if (colonIndex == -1) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                return false;
            }

            String username = decoded.substring(0, colonIndex);
            String password = decoded.substring(colonIndex + 1);

            // 验证用户凭证
            if (!userCredentials.containsKey(username) || !userCredentials.get(username).equals(password)) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // 用户验证成功
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

    // 添加用户凭证
    public void addUser(String username, String password) {
        userCredentials.put(username, password);
    }

    // 添加排除路径
    public void addExcludedPath(String path) {
        excludedPaths.add(path);
    }

    // 设置领域
    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
