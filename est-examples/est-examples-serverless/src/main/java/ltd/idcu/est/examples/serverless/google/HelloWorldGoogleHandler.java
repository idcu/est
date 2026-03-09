package ltd.idcu.est.examples.serverless.google;

import ltd.idcu.est.examples.serverless.HelloWorldFunction;
import ltd.idcu.est.serverless.google.GoogleCloudFunctionHandler;

public class HelloWorldGoogleHandler extends GoogleCloudFunctionHandler {
    public HelloWorldGoogleHandler() {
        super(new HelloWorldFunction());
    }
}
