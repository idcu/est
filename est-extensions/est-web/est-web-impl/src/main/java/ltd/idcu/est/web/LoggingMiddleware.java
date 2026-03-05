package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingMiddleware implements Middleware {

    private static final String NAME = "logging";
    private static final int PRIORITY = 200;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
        String timestamp = LocalDateTime.now().format(formatter);
        String method = request.getMethod().toString();
        String path = request.getPath();
        String ip = request.getRemoteAddress();
        
        System.out.printf("[%s] %s %s from %s%n", timestamp, method, path, ip);
        return true;
    }

    @Override
    public void after(Request request, Response response) {
        String timestamp = LocalDateTime.now().format(formatter);
        String method = request.getMethod().toString();
        String path = request.getPath();
        int status = response.getStatus().getCode();
        long contentLength = response.getContentLength();
        
        System.out.printf("[%s] %s %s %d %d bytes%n", timestamp, method, path, status, contentLength);
    }

    @Override
    public void onError(Request request, Response response, Exception e) {
        String timestamp = LocalDateTime.now().format(formatter);
        String method = request.getMethod().toString();
        String path = request.getPath();
        
        System.err.printf("[%s] ERROR %s %s: %s%n", timestamp, method, path, e.getMessage());
        e.printStackTrace();
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
