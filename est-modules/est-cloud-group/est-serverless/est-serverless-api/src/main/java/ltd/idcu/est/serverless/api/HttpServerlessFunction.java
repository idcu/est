package ltd.idcu.est.serverless.api;

public interface HttpServerlessFunction extends ServerlessFunction<ServerlessRequest, ServerlessResponse> {

    @Override
    ServerlessResponse handle(ServerlessRequest request, java.util.Map<String, Object> context);
}
