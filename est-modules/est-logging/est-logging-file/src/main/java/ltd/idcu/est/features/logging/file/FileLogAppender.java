package ltd.idcu.est.features.logging.file;

import ltd.idcu.est.features.logging.api.LogAppender;
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class FileLogAppender implements LogAppender {
    
    private final String name;
    private final LogFormatter formatter;
    private final File baseFile;
    private final long maxFileSize;
    private final int maxBackupIndex;
    private final boolean append;
    private final boolean immediateFlush;
    private final boolean useDateRolling;
    private final DateTimeFormatter dateFormatter;
    
    private PrintWriter writer;
    private final AtomicBoolean started;
    private final ReentrantLock lock;
    private long currentSize;
    private String currentDate;
    
    public static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024;
    public static final int DEFAULT_MAX_BACKUP_INDEX = 5;
    
    public FileLogAppender(File file) {
        this(file, new FileLogFormatter());
    }
    
    public FileLogAppender(File file, LogFormatter formatter) {
        this("FILE", file, formatter, DEFAULT_MAX_FILE_SIZE, DEFAULT_MAX_BACKUP_INDEX, true, true, false);
    }
    
    public FileLogAppender(String name, File file, LogFormatter formatter, 
                           long maxFileSize, int maxBackupIndex, 
                           boolean append, boolean immediateFlush, boolean useDateRolling) {
        this.name = name;
        this.baseFile = file;
        this.formatter = formatter;
        this.maxFileSize = maxFileSize;
        this.maxBackupIndex = maxBackupIndex;
        this.append = append;
        this.immediateFlush = immediateFlush;
        this.useDateRolling = useDateRolling;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.started = new AtomicBoolean(false);
        this.lock = new ReentrantLock();
        this.currentSize = 0;
        this.currentDate = LocalDate.now().format(dateFormatter);
    }
    
    @Override
    public void append(LogRecord record) {
        if (!started.get()) {
            return;
        }
        
        String formatted = formatter.format(record);
        String line = formatted + System.lineSeparator();
        
        lock.lock();
        try {
            checkRolling();
            
            if (writer == null) {
                return;
            }
            
            writer.write(line);
            currentSize += line.getBytes(StandardCharsets.UTF_8).length;
            
            if (immediateFlush) {
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
    
    private void checkRolling() throws IOException {
        if (useDateRolling) {
            String today = LocalDate.now().format(dateFormatter);
            if (!today.equals(currentDate)) {
                rollDate(today);
                currentDate = today;
                return;
            }
        }
        
        if (currentSize >= maxFileSize) {
            rollSize();
        }
    }
    
    private void rollDate(String newDate) throws IOException {
        closeWriter();
        
        File rolledFile = new File(baseFile.getParent(), 
                baseFile.getName() + "." + currentDate);
        if (baseFile.exists()) {
            baseFile.renameTo(rolledFile);
        }
        
        openWriter();
        currentSize = 0;
    }
    
    private void rollSize() throws IOException {
        closeWriter();
        
        for (int i = maxBackupIndex - 1; i >= 1; i--) {
            File backup = new File(baseFile.getParent(), 
                    baseFile.getName() + "." + i);
            File nextBackup = new File(baseFile.getParent(), 
                    baseFile.getName() + "." + (i + 1));
            
            if (backup.exists()) {
                if (nextBackup.exists()) {
                    nextBackup.delete();
                }
                backup.renameTo(nextBackup);
            }
        }
        
        File firstBackup = new File(baseFile.getParent(), 
                baseFile.getName() + ".1");
        if (baseFile.exists()) {
            if (firstBackup.exists()) {
                firstBackup.delete();
            }
            baseFile.renameTo(firstBackup);
        }
        
        openWriter();
        currentSize = 0;
    }
    
    private void openWriter() throws IOException {
        File parent = baseFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        
        FileWriter fileWriter = new FileWriter(baseFile, append);
        this.writer = new PrintWriter(fileWriter, true);
        
        if (baseFile.exists()) {
            currentSize = baseFile.length();
        }
    }
    
    private void closeWriter() {
        if (writer != null) {
            writer.flush();
            writer.close();
            writer = null;
        }
    }
    
    @Override
    public void flush() {
        lock.lock();
        try {
            if (writer != null) {
                writer.flush();
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void close() {
        stop();
        lock.lock();
        try {
            closeWriter();
        } finally {
            lock.unlock();
        }
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
        if (started.compareAndSet(false, true)) {
            lock.lock();
            try {
                openWriter();
            } catch (IOException e) {
                System.err.println("Failed to open log file: " + e.getMessage());
                started.set(false);
            } finally {
                lock.unlock();
            }
        }
    }
    
    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            flush();
        }
    }
    
    public File getFile() {
        return baseFile;
    }
    
    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }
    
    public static FileLogAppender create(File file) {
        return new FileLogAppender(file);
    }
    
    public static FileLogAppender create(File file, LogFormatter formatter) {
        return new FileLogAppender(file, formatter);
    }
    
    public static FileLogAppender createWithRolling(File file, long maxFileSize, int maxBackupIndex) {
        return new FileLogAppender("FILE", file, new FileLogFormatter(), 
                maxFileSize, maxBackupIndex, true, true, false);
    }
    
    public static FileLogAppender createWithDateRolling(File file) {
        return new FileLogAppender("FILE", file, new FileLogFormatter(), 
                Long.MAX_VALUE, 0, true, true, true);
    }
}
