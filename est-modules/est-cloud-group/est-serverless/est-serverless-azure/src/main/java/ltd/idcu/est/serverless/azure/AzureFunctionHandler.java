package ltd.idcu.est.serverless.azure;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AzureFunctionHandler {

    private final HttpServerlessFunction function;

    protected AzureFunctionHandler(HttpServerlessFunction function) {
        this.function = function;
    }

    @FunctionName("estHttpTrigger")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE},
                authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {
            ServerlessRequest estRequest = convertToRequest(request);
            Map<String, Object> contextMap = new HashMap<>();
            contextMap.put("invocationId", context.getInvocationId());
            contextMap.put("functionName", context.getFunctionName());
            contextMap.put("logger", context.getLogger());

            ServerlessResponse estResponse = function.handle(estRequest, contextMap);
            return convertFromResponse(request, estResponse);
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private ServerlessRequest convertToRequest(HttpRequestMessage<Optional<String>> request) {
        ServerlessRequest estRequest = new ServerlessRequest();
        estRequest.setMethod(request.getHttpMethod().name());
        estRequest.setPath(request.getUri().getPath());

        Map<String, String> headers = new HashMap<>();
        request.getHeaders().forEach(headers::put);
        estRequest.setHeaders(headers);

        Map<String, String> queryParams = new HashMap<>();
        request.getQueryParameters().forEach(queryParams::put);
        estRequest.setQueryParameters(queryParams);

        request.getBody().ifPresent(estRequest::setBody);

        return estRequest;
    }

    private HttpResponseMessage convertFromResponse(HttpRequestMessage<Optional<String>> request, ServerlessResponse estResponse) {
        HttpResponseMessage.Builder builder = request.createResponseBuilder(
                HttpStatus.valueOf(estResponse.getStatusCode()));

        builder.body(estResponse.getBody());
        estResponse.getHeaders().forEach(builder::header);

        return builder.build();
    }
}
