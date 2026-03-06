package ltd.idcu.est.microservices.gateway.api;

import java.util.function.Function;

public interface GatewayMiddleware {
    String getName();

    Function<GatewayContext, GatewayContext> process();

    default int getOrder() {
        return 0;
    }
}
