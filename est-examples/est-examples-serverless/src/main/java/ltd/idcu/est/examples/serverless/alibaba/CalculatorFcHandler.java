package ltd.idcu.est.examples.serverless.alibaba;

import ltd.idcu.est.examples.serverless.CalculatorFunction;
import ltd.idcu.est.serverless.alibaba.AlibabaFcHandler;

public class CalculatorFcHandler extends AlibabaFcHandler {
    public CalculatorFcHandler() {
        super(new CalculatorFunction());
    }
}
