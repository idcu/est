package ltd.idcu.est.features.logging.file;

import ltd.idcu.est.features.logging.api.AbstractLogger;
import ltd.idcu.est.features.logging.api.LogAppender;
import ltd.idcu.est.features.logging.api.LogConfig;

import java.io.File;

public class FileLogger extends AbstractLogger {
    
    public FileLogger(String name, File file) {
        this(name, file, LogConfig.defaultConfig());
    }
    
    public FileLogger(String name, File file, LogConfig config) {
        super(name, config);
        
        if (config.getAppenders().isEmpty()) {
            this.appenders.add(new FileLogAppender(file));
        } else {
            this.appenders.addAll(config.getAppenders());
        }
        
        initializeAppenders();
    }
    
    public FileLogger(String name, LogConfig config) {
        super(name, config);
        this.appenders.addAll(config.getAppenders());
        initializeAppenders();
    }
    
    @Override
    protected String formatMessage(String format, Object... args) {
        if (args == null || args.length == 0) {
            return format;
        }
        return String.format(format.replace("{}", "%s"), args);
    }
}
