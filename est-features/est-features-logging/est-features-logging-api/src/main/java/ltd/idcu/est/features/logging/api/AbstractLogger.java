package ltd.idcu.est.features.logging.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLogger implements Logger {
    
    protected final String name;
    protected final LogConfig config;
    protected final List<LogAppender> appenders;
    
    protected AbstractLogger(String name, LogConfig config) {
        this.name = name;
        this.config = config;
        this.appenders = new ArrayList<>();
    }
    
    protected void initializeAppenders() {
        for (LogAppender appender : this.appenders) {
            appender.start();
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isTraceEnabled() {
        return config.getLevel().isEnabled(LogLevel.TRACE);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return config.getLevel().isEnabled(LogLevel.DEBUG);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return config.getLevel().isEnabled(LogLevel.INFO);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return config.getLevel().isEnabled(LogLevel.WARN);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return config.getLevel().isEnabled(LogLevel.ERROR);
    }
    
    @Override
    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }
    
    @Override
    public void trace(String format, Object... args) {
        log(LogLevel.TRACE, formatMessage(format, args));
    }
    
    @Override
    public void trace(String message, Throwable throwable) {
        log(LogLevel.TRACE, message, throwable);
    }
    
    @Override
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    @Override
    public void debug(String format, Object... args) {
        log(LogLevel.DEBUG, formatMessage(format, args));
    }
    
    @Override
    public void debug(String message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);
    }
    
    @Override
    public void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    @Override
    public void info(String format, Object... args) {
        log(LogLevel.INFO, formatMessage(format, args));
    }
    
    @Override
    public void info(String message, Throwable throwable) {
        log(LogLevel.INFO, message, throwable);
    }
    
    @Override
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }
    
    @Override
    public void warn(String format, Object... args) {
        log(LogLevel.WARN, formatMessage(format, args));
    }
    
    @Override
    public void warn(String message, Throwable throwable) {
        log(LogLevel.WARN, message, throwable);
    }
    
    @Override
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    @Override
    public void error(String format, Object... args) {
        log(LogLevel.ERROR, formatMessage(format, args));
    }
    
    @Override
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }
    
    @Override
    public void log(LogLevel level, String message) {
        if (!config.getLevel().isEnabled(level)) {
            return;
        }
        
        LogRecord record = LogRecord.of(name, level, message);
        
        for (LogAppender appender : appenders) {
            if (appender.isStarted()) {
                appender.append(record);
            }
        }
    }
    
    @Override
    public void log(LogLevel level, String format, Object... args) {
        log(level, formatMessage(format, args));
    }
    
    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        if (!config.getLevel().isEnabled(level)) {
            return;
        }
        
        LogRecord record = LogRecord.of(name, level, message, throwable);
        
        for (LogAppender appender : appenders) {
            if (appender.isStarted()) {
                appender.append(record);
            }
        }
    }
    
    protected String formatMessage(String format, Object... args) {
        if (args == null || args.length == 0) {
            return format;
        }
        String formatted = format;
        if (formatted.contains("{}")) {
            for (Object arg : args) {
                formatted = formatted.replaceFirst("\\{\\}", arg != null ? arg.toString() : "null");
            }
        } else if (formatted.contains("%")) {
            try {
                formatted = String.format(formatted, args);
            } catch (Exception e) {
                formatted = format + " [Format error: " + e.getMessage() + "]";
            }
        }
        return formatted;
    }
    
    public void close() {
        for (LogAppender appender : appenders) {
            appender.stop();
            appender.close();
        }
    }
}
