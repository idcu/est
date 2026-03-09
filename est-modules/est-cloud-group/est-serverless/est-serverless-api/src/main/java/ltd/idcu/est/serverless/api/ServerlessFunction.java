package ltd.idcu.est.serverless.api;

import java.util.Map;

public interface ServerlessFunction<I, O> {

    O handle(I input, Map<String, Object> context);

    default void initialize(Map<String, Object> config) {
    }

    default void destroy() {
    }
}
