package ltd.idcu.est.logging.api;

public enum LogLevel {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);
    
    private final int level;
    
    LogLevel(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isEnabled(LogLevel other) {
        return this.level <= other.level;
    }
    
    public static LogLevel fromString(String level) {
        if (level == null || level.isEmpty()) {
            return INFO;
        }
        try {
            return LogLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return INFO;
        }
    }
}
