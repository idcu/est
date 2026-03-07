package ltd.idcu.est.features.logging.console;

import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogRecord;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ConsoleLogFormatter implements LogFormatter {
    
    private final DateTimeFormatter dateTimeFormatter;
    private final boolean colorEnabled;
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    
    public ConsoleLogFormatter() {
        this(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"), true);
    }
    
    public ConsoleLogFormatter(DateTimeFormatter dateTimeFormatter, boolean colorEnabled) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.colorEnabled = colorEnabled;
    }
    
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        
        String timestamp = record.getTimestamp()
                .atZone(ZoneId.systemDefault())
                .format(dateTimeFormatter);
        
        sb.append(colorize(timestamp, CYAN));
        sb.append(" [");
        sb.append(colorize(record.getThreadName(), MAGENTA));
        sb.append("] ");
        sb.append(colorizeLevel(record.getLevel()));
        sb.append(" ");
        sb.append(colorize(abbreviateLoggerName(record.getLoggerName(), 36), WHITE));
        sb.append(" - ");
        sb.append(record.getMessage());
        
        if (record.getThrowable() != null) {
            sb.append("\n");
            formatThrowable(sb, record.getThrowable());
        }
        
        return sb.toString();
    }
    
    private void formatThrowable(StringBuilder sb, Throwable throwable) {
        sb.append(colorize(throwable.toString(), RED));
        
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int maxFrames = Math.min(stackTrace.length, 10);
        
        for (int i = 0; i < maxFrames; i++) {
            sb.append("\n\tat ");
            sb.append(colorize(stackTrace[i].toString(), YELLOW));
        }
        
        if (stackTrace.length > maxFrames) {
            sb.append("\n\t... ");
            sb.append(stackTrace.length - maxFrames);
            sb.append(" more");
        }
        
        Throwable cause = throwable.getCause();
        if (cause != null) {
            sb.append("\nCaused by: ");
            formatThrowable(sb, cause);
        }
    }
    
    private String colorizeLevel(ltd.idcu.est.features.logging.api.LogLevel level) {
        String levelStr = String.format("%-5s", level.name());
        if (!colorEnabled) {
            return levelStr;
        }
        
        return switch (level) {
            case TRACE -> colorize(levelStr, BLUE);
            case DEBUG -> colorize(levelStr, GREEN);
            case INFO -> colorize(levelStr, WHITE);
            case WARN -> colorize(levelStr, YELLOW);
            case ERROR -> colorize(levelStr, RED);
        };
    }
    
    private String colorize(String text, String color) {
        if (!colorEnabled) {
            return text;
        }
        return color + text + RESET;
    }
    
    private String abbreviateLoggerName(String loggerName, int maxLength) {
        if (loggerName.length() <= maxLength) {
            return loggerName;
        }
        
        String[] parts = loggerName.split("\\.");
        if (parts.length <= 1) {
            return loggerName.substring(0, maxLength - 3) + "...";
        }
        
        StringBuilder abbreviated = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1) {
                abbreviated.append(parts[i]);
            } else {
                abbreviated.append(parts[i].charAt(0)).append(".");
            }
        }
        
        if (abbreviated.length() > maxLength) {
            return abbreviated.substring(abbreviated.length() - maxLength);
        }
        
        return abbreviated.toString();
    }
    
    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    public static ConsoleLogFormatter colored() {
        return new ConsoleLogFormatter();
    }
    
    public static ConsoleLogFormatter plain() {
        return new ConsoleLogFormatter(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"), 
                false
        );
    }
}
