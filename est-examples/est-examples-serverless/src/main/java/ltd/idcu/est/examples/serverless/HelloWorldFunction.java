package ltd.idcu.est.examples.serverless;

import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.util.Map;

public class HelloWorldFunction implements HttpServerlessFunction {

    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        String name = request.getQueryParameters().getOrDefault("name", "World");
        String message = String.format("{\"message\":\"Hello, %s!\",\"version\":\"1.0.0\"}", name);

        ServerlessResponse response = ServerlessResponse.ok(message);
        response.addHeader("Content-Type", "application/json");
        return response;
    }

    @Override
    public void initialize(Map<String, Object> config) {
        System.out.println("[HelloWorldFunction] Initializing...");
    }

    @Override
    public void destroy() {
        System.out.println("[HelloWorldFunction] Destroying...");
    }
}
