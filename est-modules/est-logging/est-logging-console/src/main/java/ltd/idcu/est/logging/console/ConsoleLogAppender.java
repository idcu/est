package ltd.idcu.est.logging.console;

import ltd.idcu.est.logging.api.LogAppender;
import ltd.idcu.est.logging.api.LogFormatter;
import ltd.idcu.est.logging.api.LogRecord;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleLogAppender implements LogAppender {
    
    private final String name;
    private final LogFormatter formatter;
    private final PrintStream output;
    private final AtomicBoolean started;
    
    public ConsoleLogAppender() {
        this("CONSOLE", new ConsoleLogFormatter(), System.out);
    }
    
    public ConsoleLogAppender(String name, LogFormatter formatter, PrintStream output) {
        this.name = name;
        this.formatter = formatter;
        this.output = output;
        this.started = new AtomicBoolean(true);
    }
    
    @Override
    public void append(LogRecord record) {
        if (!started.get()) {
            return;
        }
        
        String formatted = formatter.format(record);
        PrintStream target = getTargetStream(record);
        target.println(formatted);
    }
    
    private PrintStream getTargetStream(LogRecord record) {
        if (record.getLevel() == ltd.idcu.est.logging.api.LogLevel.ERROR ||
            record.getLevel() == ltd.idcu.est.logging.api.LogLevel.WARN) {
            return System.err;
        }
        return output;
    }
    
    @Override
    public void flush() {
        output.flush();
        System.err.flush();
    }
    
    @Override
    public void close() {
        stop();
        flush();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isStarted() {
        return started.get();
    }
    
    @Override
    public void start() {
        started.set(true);
    }
    
    @Override
    public void stop() {
        started.set(false);
    }
    
    public static ConsoleLogAppender stdout() {
        return new ConsoleLogAppender("STDOUT", new ConsoleLogFormatter(), System.out);
    }
    
    public static ConsoleLogAppender stderr() {
        return new ConsoleLogAppender("STDERR", new ConsoleLogFormatter(), System.err);
    }
    
    public static ConsoleLogAppender withFormatter(LogFormatter formatter) {
        return new ConsoleLogAppender("CONSOLE", formatter, System.out);
    }
}
