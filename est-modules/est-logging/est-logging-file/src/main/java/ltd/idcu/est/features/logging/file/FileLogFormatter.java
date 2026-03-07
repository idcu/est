package ltd.idcu.est.features.logging.file;

import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogRecord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileLogFormatter implements LogFormatter {
    
    private final DateTimeFormatter dateTimeFormatter;
    
    public FileLogFormatter() {
        this(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
    
    public FileLogFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }
    
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        
        String timestamp = record.getTimestamp()
                .atZone(ZoneId.systemDefault())
                .format(dateTimeFormatter);
        
        sb.append(timestamp);
        sb.append(" [");
        sb.append(record.getThreadName());
        sb.append("] ");
        sb.append(String.format("%-5s", record.getLevel().name()));
        sb.append(" ");
        sb.append(record.getLoggerName());
        sb.append(" - ");
        sb.append(record.getMessage());
        
        if (record.getThrowable() != null) {
            sb.append("\n");
            formatThrowable(sb, record.getThrowable());
        }
        
        return sb.toString();
    }
    
    private void formatThrowable(StringBuilder sb, Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        sb.append(sw.toString());
    }
    
    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    public static FileLogFormatter defaultFormatter() {
        return new FileLogFormatter();
    }
    
    public static FileLogFormatter withPattern(String pattern) {
        return new FileLogFormatter(DateTimeFormatter.ofPattern(pattern));
    }
}
