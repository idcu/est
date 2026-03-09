package ltd.idcu.est.examples.serverless.alibaba;

import ltd.idcu.est.examples.serverless.HelloWorldFunction;
import ltd.idcu.est.serverless.alibaba.AlibabaFcHandler;

public class HelloWorldFcHandler extends AlibabaFcHandler {
    public HelloWorldFcHandler() {
        super(new HelloWorldFunction());
    }
}
