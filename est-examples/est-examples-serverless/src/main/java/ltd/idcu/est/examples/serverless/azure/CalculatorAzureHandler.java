package ltd.idcu.est.examples.serverless.azure;

import ltd.idcu.est.examples.serverless.CalculatorFunction;
import ltd.idcu.est.serverless.azure.AzureFunctionHandler;

public class CalculatorAzureHandler extends AzureFunctionHandler {
    public CalculatorAzureHandler() {
        super(new CalculatorFunction());
    }
}
