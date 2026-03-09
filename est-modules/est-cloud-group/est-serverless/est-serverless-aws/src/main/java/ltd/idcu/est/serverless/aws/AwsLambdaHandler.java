package ltd.idcu.est.serverless.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class AwsLambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final HttpServerlessFunction function;

    protected AwsLambdaHandler(HttpServerlessFunction function) {
        this.function = function;
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            ServerlessRequest request = convertToRequest(input);
            Map<String, Object> contextMap = new HashMap<>();
            contextMap.put("awsRequestId", context.getAwsRequestId());
            contextMap.put("functionName", context.getFunctionName());
            contextMap.put("functionVersion", context.getFunctionVersion());
            contextMap.put("memoryLimitInMB", context.getMemoryLimitInMB());
            contextMap.put("remainingTimeInMillis", context.getRemainingTimeInMillis());

            ServerlessResponse response = function.handle(request, contextMap);
            return convertFromResponse(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("body", "{\"error\":\"" + e.getMessage() + "\"}");
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            errorResponse.put("headers", headers);
            return errorResponse;
        }
    }

    @SuppressWarnings("unchecked")
    private ServerlessRequest convertToRequest(Map<String, Object> input) {
        ServerlessRequest request = new ServerlessRequest();
        request.setMethod((String) input.get("httpMethod"));
        request.setPath((String) input.get("path"));

        if (input.containsKey("headers")) {
            request.setHeaders((Map<String, String>) input.get("headers"));
        }
        if (input.containsKey("queryStringParameters")) {
            request.setQueryParameters((Map<String, String>) input.get("queryStringParameters"));
        }
        if (input.containsKey("body")) {
            request.setBody((String) input.get("body"));
        }

        return request;
    }

    private Map<String, Object> convertFromResponse(ServerlessResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("statusCode", response.getStatusCode());
        result.put("headers", response.getHeaders());
        result.put("body", response.getBody());
        result.put("isBase64Encoded", response.isBase64Encoded());
        return result;
    }
}
