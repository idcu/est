package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LoggingExample {
    public static void main(String[] args) {
        // 创建控制台日志
        Logger logger = ConsoleLogs.getLogger(LoggingExample.class);
        
        // 不同级别的日志
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");
        
        // 带异常的日志
        try {
            throw new Exception("Test exception");
        } catch (Exception e) {
            logger.error("Error with exception", e);
        }
    }
}