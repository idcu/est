package ltd.idcu.est.examples.serverless;

import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.util.Map;

public class CalculatorFunction implements HttpServerlessFunction {

    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        try {
            Map<String, String> queryParams = request.getQueryParameters();
            
            String operation = queryParams.get("op");
            double a = Double.parseDouble(queryParams.getOrDefault("a", "0"));
            double b = Double.parseDouble(queryParams.getOrDefault("b", "0"));

            double result;
            String operationName;

            switch (operation != null ? operation.toLowerCase() : "add") {
                case "subtract":
                case "sub":
                    result = a - b;
                    operationName = "subtraction";
                    break;
                case "multiply":
                case "mul":
                    result = a * b;
                    operationName = "multiplication";
                    break;
                case "divide":
                case "div":
                    if (b == 0) {
                        return ServerlessResponse.badRequest("{\"error\":\"Division by zero\"}");
                    }
                    result = a / b;
                    operationName = "division";
                    break;
                case "add":
                default:
                    result = a + b;
                    operationName = "addition";
                    break;
            }

            String responseBody = String.format(
                "{\"operation\":\"%s\",\"a\":%f,\"b\":%f,\"result\":%f}",
                operationName, a, b, result
            );

            ServerlessResponse response = ServerlessResponse.ok(responseBody);
            response.addHeader("Content-Type", "application/json");
            return response;

        } catch (NumberFormatException e) {
            return ServerlessResponse.badRequest("{\"error\":\"Invalid number format\"}");
        } catch (Exception e) {
            return ServerlessResponse.serverError("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
