package ltd.idcu.est.logging.file;

import ltd.idcu.est.logging.api.LogAppender;
import ltd.idcu.est.logging.api.LogConfig;
import ltd.idcu.est.logging.api.LogFormatter;
import ltd.idcu.est.logging.api.LogLevel;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.api.LoggerFactory;

import java.io.File;
import java.time.format.DateTimeFormatter;

public final class FileLogs {
    
    private FileLogs() {
    }
    
    public static Logger getLogger(String name, File file) {
        return new FileLogger(name, file);
    }
    
    public static Logger getLogger(Class<?> clazz, File file) {
        return getLogger(clazz.getName(), file);
    }
    
    public static Logger getLogger(String name, File file, LogConfig config) {
        return new FileLogger(name, file, config);
    }
    
    public static Logger getLogger(String name, File file, LogLevel level) {
        LogConfig config = LogConfig.defaultConfig().setLevel(level);
        return new FileLogger(name, file, config);
    }
    
    public static Logger debugLogger(String name, File file) {
        return getLogger(name, file, LogLevel.DEBUG);
    }
    
    public static Logger traceLogger(String name, File file) {
        return getLogger(name, file, LogLevel.TRACE);
    }
    
    public static FileLogAppender appender(File file) {
        return FileLogAppender.create(file);
    }
    
    public static FileLogAppender appender(File file, LogFormatter formatter) {
        return FileLogAppender.create(file, formatter);
    }
    
    public static FileLogAppender rollingAppender(File file, long maxFileSize, int maxBackupIndex) {
        return FileLogAppender.createWithRolling(file, maxFileSize, maxBackupIndex);
    }
    
    public static FileLogAppender dateRollingAppender(File file) {
        return FileLogAppender.createWithDateRolling(file);
    }
    
    public static FileLogFormatter formatter() {
        return FileLogFormatter.defaultFormatter();
    }
    
    public static FileLogFormatter formatter(String pattern) {
        return FileLogFormatter.withPattern(pattern);
    }
    
    public static FileLogFormatter formatter(DateTimeFormatter dateTimeFormatter) {
        return new FileLogFormatter(dateTimeFormatter);
    }
    
    public static void setAsDefault(File file) {
        LoggerFactory.setLoggerFactory(name -> new FileLogger(name, file));
    }
    
    public static void setAsDefault(File file, LogConfig config) {
        LoggerFactory.setLoggerFactory(name -> new FileLogger(name, file, config));
    }
    
    public static FileLoggerBuilder builder() {
        return new FileLoggerBuilder();
    }
    
    public static class FileLoggerBuilder {
        private String name;
        private File file;
        private LogLevel level = LogLevel.INFO;
        private LogFormatter formatter = new FileLogFormatter();
        private long maxFileSize = FileLogAppender.DEFAULT_MAX_FILE_SIZE;
        private int maxBackupIndex = FileLogAppender.DEFAULT_MAX_BACKUP_INDEX;
        private boolean useDateRolling = false;
        private boolean append = true;
        
        public FileLoggerBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public FileLoggerBuilder file(File file) {
            this.file = file;
            return this;
        }
        
        public FileLoggerBuilder file(String path) {
            this.file = new File(path);
            return this;
        }
        
        public FileLoggerBuilder level(LogLevel level) {
            this.level = level;
            return this;
        }
        
        public FileLoggerBuilder level(String level) {
            this.level = LogLevel.fromString(level);
            return this;
        }
        
        public FileLoggerBuilder formatter(LogFormatter formatter) {
            this.formatter = formatter;
            return this;
        }
        
        public FileLoggerBuilder maxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
            return this;
        }
        
        public FileLoggerBuilder maxBackupIndex(int maxBackupIndex) {
            this.maxBackupIndex = maxBackupIndex;
            return this;
        }
        
        public FileLoggerBuilder useDateRolling(boolean useDateRolling) {
            this.useDateRolling = useDateRolling;
            return this;
        }
        
        public FileLoggerBuilder append(boolean append) {
            this.append = append;
            return this;
        }
        
        public Logger build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Logger name cannot be null or empty");
            }
            if (file == null) {
                throw new IllegalArgumentException("File cannot be null");
            }
            
            LogAppender appender = new FileLogAppender("FILE", file, formatter, 
                    maxFileSize, maxBackupIndex, append, true, useDateRolling);
            LogConfig config = LogConfig.defaultConfig()
                    .setLevel(level)
                    .addAppender(appender);
            
            return new FileLogger(name, config);
        }
    }
}
