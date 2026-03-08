package ltd.idcu.est.logging.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class LoggerFactory {
    
    private static final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    private static volatile LogConfig globalConfig = LogConfig.defaultConfig();
    private static volatile Function<String, Logger> loggerFactory = null;
    
    protected LoggerFactory() {
    }
    
    public static Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, n -> {
            if (loggerFactory != null) {
                return loggerFactory.apply(n);
            }
            return createDefaultLogger(n);
        });
    }
    
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
    
    public static void setLoggerFactory(Function<String, Logger> factory) {
        loggerFactory = factory;
    }
    
    public static void setGlobalConfig(LogConfig config) {
        globalConfig = config;
    }
    
    public static LogConfig getGlobalConfig() {
        return globalConfig;
    }
    
    public static void reset() {
        loggers.clear();
        globalConfig = LogConfig.defaultConfig();
        loggerFactory = null;
    }
    
    protected abstract Logger createLogger(String name);
    
    private static Logger createDefaultLogger(String name) {
        return new DefaultLogger(name, globalConfig);
    }
    
    private static class DefaultLogger implements Logger {
        private final String name;
        private final LogConfig config;
        
        DefaultLogger(String name, LogConfig config) {
            this.name = name;
            this.config = config;
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
            appendLog(record);
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
            appendLog(record);
        }
        
        private void appendLog(LogRecord record) {
            for (LogAppender appender : config.getAppenders()) {
                if (appender.isStarted()) {
                    appender.append(record);
                }
            }
        }
        
        private String formatMessage(String format, Object... args) {
            if (args == null || args.length == 0) {
                return format;
            }
            return String.format(format.replace("{}", "%s"), args);
        }
    }
}
