package ltd.idcu.est.gateway;

public class GatewayExample {
    public static void main(String[] args) {
        Gateway gateway = Gateway.create()
            .withLogging()
            .withCors()
            .route("/api/users/**", "user-service", "/$1")
            .route("/api/orders/**", "order-service", "/$1")
            .route("/api/payments/**", "payment-service", "/$1")
            .registerService("user-service", "http://localhost:8081")
            .registerService("order-service", "http://localhost:8082")
            .registerService("payment-service", "http://localhost:8083");

        System.out.println("Starting API Gateway on port 8080...");
        gateway.start(8080);
        System.out.println("API Gateway started on http://localhost:8080");

        Runtime.getRuntime().addShutdownHook(new Thread(() -&gt; {
            System.out.println("Shutting down API Gateway...");
            gateway.stop();
            System.out.println("API Gateway stopped.");
        }));
    }
}
