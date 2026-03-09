package ltd.idcu.est.serverless.google;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class GoogleCloudFunctionHandler implements HttpFunction {

    private final HttpServerlessFunction function;

    protected GoogleCloudFunctionHandler(HttpServerlessFunction function) {
        this.function = function;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        try {
            ServerlessRequest estRequest = convertToRequest(request);
            Map<String, Object> contextMap = new HashMap<>();

            ServerlessResponse estResponse = function.handle(estRequest, contextMap);
            writeResponse(response, estResponse);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private ServerlessRequest convertToRequest(HttpRequest request) throws IOException {
        ServerlessRequest estRequest = new ServerlessRequest();
        estRequest.setMethod(request.getMethod());
        estRequest.setPath(request.getUri().getPath());

        Map<String, String> headers = new HashMap<>();
        request.getHeaders().forEach((key, values) -> {
            if (!values.isEmpty()) {
                headers.put(key, values.get(0));
            }
        });
        estRequest.setHeaders(headers);

        Map<String, String> queryParams = new HashMap<>();
        request.getQueryParameters().forEach((key, values) -> {
            if (!values.isEmpty()) {
                queryParams.put(key, values.get(0));
            }
        });
        estRequest.setQueryParameters(queryParams);

        request.getReader().ifPresent(reader -> {
            StringBuilder body = new StringBuilder();
            try (BufferedReader br = new BufferedReader(reader)) {
                String line;
                while ((line = br.readLine()) != null) {
                    body.append(line);
                }
            } catch (IOException e) {
                body.append("");
            }
            estRequest.setBody(body.toString());
        });

        return estRequest;
    }

    private void writeResponse(HttpResponse response, ServerlessResponse estResponse) throws IOException {
        response.setStatusCode(estResponse.getStatusCode());
        estResponse.getHeaders().forEach(response::putHeader);
        if (estResponse.getBody() != null) {
            response.getWriter().write(estResponse.getBody());
        }
    }
}
