package ltd.idcu.est.examples.serverless.azure;

import ltd.idcu.est.examples.serverless.HelloWorldFunction;
import ltd.idcu.est.serverless.azure.AzureFunctionHandler;

public class HelloWorldAzureHandler extends AzureFunctionHandler {
    public HelloWorldAzureHandler() {
        super(new HelloWorldFunction());
    }
}
