package ltd.idcu.est.features.logging.api;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogConfig {
    
    private LogLevel level = LogLevel.INFO;
    private String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private boolean includeCallerInfo = false;
    private boolean colorEnabled = true;
    private List<LogAppender> appenders = new ArrayList<>();
    private LogFormatter formatter;
    
    public LogConfig() {
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public LogConfig setLevel(LogLevel level) {
        this.level = level;
        return this;
    }
    
    public LogConfig setLevel(String level) {
        this.level = LogLevel.fromString(level);
        return this;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public LogConfig setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }
    
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    public LogConfig setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }
    
    public LogConfig setDateTimePattern(String pattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return this;
    }
    
    public boolean isIncludeCallerInfo() {
        return includeCallerInfo;
    }
    
    public LogConfig setIncludeCallerInfo(boolean includeCallerInfo) {
        this.includeCallerInfo = includeCallerInfo;
        return this;
    }
    
    public boolean isColorEnabled() {
        return colorEnabled;
    }
    
    public LogConfig setColorEnabled(boolean colorEnabled) {
        this.colorEnabled = colorEnabled;
        return this;
    }
    
    public List<LogAppender> getAppenders() {
        return new ArrayList<>(appenders);
    }
    
    public LogConfig addAppender(LogAppender appender) {
        this.appenders.add(appender);
        return this;
    }
    
    public LogConfig setAppenders(List<LogAppender> appenders) {
        this.appenders = new ArrayList<>(appenders);
        return this;
    }
    
    public LogFormatter getFormatter() {
        return formatter;
    }
    
    public LogConfig setFormatter(LogFormatter formatter) {
        this.formatter = formatter;
        return this;
    }
    
    public static LogConfig defaultConfig() {
        return new LogConfig();
    }
    
    public static LogConfig debugConfig() {
        return new LogConfig()
                .setLevel(LogLevel.DEBUG)
                .setIncludeCallerInfo(true);
    }
    
    public static LogConfig traceConfig() {
        return new LogConfig()
                .setLevel(LogLevel.TRACE)
                .setIncludeCallerInfo(true);
    }
    
    @Override
    public String toString() {
        return "LogConfig{" +
                "level=" + level +
                ", pattern='" + pattern + '\'' +
                ", includeCallerInfo=" + includeCallerInfo +
                ", colorEnabled=" + colorEnabled +
                ", appenders=" + appenders.size() +
                '}';
    }
}
