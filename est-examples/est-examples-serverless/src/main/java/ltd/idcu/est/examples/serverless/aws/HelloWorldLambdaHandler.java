package ltd.idcu.est.examples.serverless.aws;

import ltd.idcu.est.examples.serverless.HelloWorldFunction;
import ltd.idcu.est.serverless.aws.AwsLambdaHandler;

public class HelloWorldLambdaHandler extends AwsLambdaHandler {
    public HelloWorldLambdaHandler() {
        super(new HelloWorldFunction());
    }
}
