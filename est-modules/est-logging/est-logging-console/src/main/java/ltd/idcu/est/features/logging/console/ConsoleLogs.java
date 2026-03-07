package ltd.idcu.est.features.logging.console;

import ltd.idcu.est.features.logging.api.LogAppender;
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LoggerFactory;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

public final class ConsoleLogs {
    
    private ConsoleLogs() {
    }
    
    public static Logger getLogger(String name) {
        return new ConsoleLogger(name);
    }
    
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
    
    public static Logger getLogger(String name, LogConfig config) {
        return new ConsoleLogger(name, config);
    }
    
    public static Logger getLogger(String name, LogLevel level) {
        LogConfig config = LogConfig.defaultConfig().setLevel(level);
        return new ConsoleLogger(name, config);
    }
    
    public static Logger debugLogger(String name) {
        return getLogger(name, LogLevel.DEBUG);
    }
    
    public static Logger traceLogger(String name) {
        return getLogger(name, LogLevel.TRACE);
    }
    
    public static ConsoleLogAppender appender() {
        return new ConsoleLogAppender();
    }
    
    public static ConsoleLogAppender appender(LogFormatter formatter) {
        return ConsoleLogAppender.withFormatter(formatter);
    }
    
    public static ConsoleLogAppender stdoutAppender() {
        return ConsoleLogAppender.stdout();
    }
    
    public static ConsoleLogAppender stderrAppender() {
        return ConsoleLogAppender.stderr();
    }
    
    public static ConsoleLogFormatter formatter() {
        return new ConsoleLogFormatter();
    }
    
    public static ConsoleLogFormatter coloredFormatter() {
        return ConsoleLogFormatter.colored();
    }
    
    public static ConsoleLogFormatter plainFormatter() {
        return ConsoleLogFormatter.plain();
    }
    
    public static ConsoleLogFormatter formatter(DateTimeFormatter dateTimeFormatter, boolean colorEnabled) {
        return new ConsoleLogFormatter(dateTimeFormatter, colorEnabled);
    }
    
    public static void setAsDefault() {
        LoggerFactory.setLoggerFactory(ConsoleLogger::new);
    }
    
    public static void setAsDefault(LogConfig config) {
        LoggerFactory.setLoggerFactory(name -> new ConsoleLogger(name, config));
    }
    
    public static ConsoleLoggerBuilder builder() {
        return new ConsoleLoggerBuilder();
    }
    
    public static class ConsoleLoggerBuilder {
        private String name;
        private LogLevel level = LogLevel.INFO;
        private LogFormatter formatter = new ConsoleLogFormatter();
        private PrintStream output = System.out;
        
        public ConsoleLoggerBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public ConsoleLoggerBuilder level(LogLevel level) {
            this.level = level;
            return this;
        }
        
        public ConsoleLoggerBuilder level(String level) {
            this.level = LogLevel.fromString(level);
            return this;
        }
        
        public ConsoleLoggerBuilder formatter(LogFormatter formatter) {
            this.formatter = formatter;
            return this;
        }
        
        public ConsoleLoggerBuilder output(PrintStream output) {
            this.output = output;
            return this;
        }
        
        public Logger build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Logger name cannot be null or empty");
            }
            
            LogAppender appender = new ConsoleLogAppender("CONSOLE", formatter, output);
            LogConfig config = LogConfig.defaultConfig()
                    .setLevel(level)
                    .addAppender(appender);
            
            return new ConsoleLogger(name, config);
        }
    }
}
