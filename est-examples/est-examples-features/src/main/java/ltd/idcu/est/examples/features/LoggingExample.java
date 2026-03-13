package ltd.idcu.est.examples.features;

import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

public class LoggingExample {
    public static void main(String[] args) {
        // Create console logger
        Logger logger = ConsoleLogs.getLogger(LoggingExample.class);
        
        // Different log levels
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");
        
        // Log with exception
        try {
            throw new Exception("Test exception");
        } catch (Exception e) {
            logger.error("Error with exception", e);
        }
    }
}
