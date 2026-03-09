package ltd.idcu.est.examples.serverless.aws;

import ltd.idcu.est.examples.serverless.CalculatorFunction;
import ltd.idcu.est.serverless.aws.AwsLambdaHandler;

public class CalculatorLambdaHandler extends AwsLambdaHandler {
    public CalculatorLambdaHandler() {
        super(new CalculatorFunction());
    }
}
