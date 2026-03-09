package ltd.idcu.est.examples.serverless.google;

import ltd.idcu.est.examples.serverless.CalculatorFunction;
import ltd.idcu.est.serverless.google.GoogleCloudFunctionHandler;

public class CalculatorGoogleHandler extends GoogleCloudFunctionHandler {
    public CalculatorGoogleHandler() {
        super(new CalculatorFunction());
    }
}
