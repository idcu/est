package ltd.idcu.est.logging.console;

import ltd.idcu.est.logging.api.AbstractLogger;
import ltd.idcu.est.logging.api.LogAppender;
import ltd.idcu.est.logging.api.LogConfig;

public class ConsoleLogger extends AbstractLogger {
    
    public ConsoleLogger(String name) {
        this(name, LogConfig.defaultConfig());
    }
    
    public ConsoleLogger(String name, LogConfig config) {
        super(name, config);
        
        if (config.getAppenders().isEmpty()) {
            this.appenders.add(new ConsoleLogAppender());
        } else {
            this.appenders.addAll(config.getAppenders());
        }
        
        initializeAppenders();
    }
}
