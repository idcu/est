package ltd.idcu.est.serverless.alibaba;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.HttpRequestHandler;
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class AlibabaFcHandler implements HttpRequestHandler {

    private final HttpServerlessFunction function;

    protected AlibabaFcHandler(HttpServerlessFunction function) {
        this.function = function;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) 
            throws IOException {
        try {
            ServerlessRequest estRequest = convertToRequest(request);
            Map<String, Object> contextMap = new HashMap<>();
            contextMap.put("requestId", context.getRequestId());
            contextMap.put("functionName", context.getFunctionName());
            contextMap.put("functionHandler", context.getFunctionHandler());
            contextMap.put("memoryLimitInMB", context.getMemoryLimitInMB());
            contextMap.put("credentials", context.getExecutionCredentials());
            contextMap.put("logger", context.getLogger());

            ServerlessResponse estResponse = function.handle(estRequest, contextMap);
            writeResponse(response, estResponse);
        } catch (Exception e) {
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private ServerlessRequest convertToRequest(HttpServletRequest request) throws IOException {
        ServerlessRequest estRequest = new ServerlessRequest();
        estRequest.setMethod(request.getMethod());
        estRequest.setPath(request.getRequestURI());

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        estRequest.setHeaders(headers);

        Map<String, String> queryParams = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            queryParams.put(name, request.getParameter(name));
        }
        estRequest.setQueryParameters(queryParams);

        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            estRequest.setBody(body.toString());
        }

        return estRequest;
    }

    private void writeResponse(HttpServletResponse response, ServerlessResponse estResponse) throws IOException {
        response.setStatus(estResponse.getStatusCode());
        estResponse.getHeaders().forEach(response::setHeader);
        if (estResponse.getBody() != null) {
            response.getWriter().write(estResponse.getBody());
        }
    }
}
