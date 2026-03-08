package ltd.idcu.est.scheduler.api;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ScheduleConfig {
    
    private final ScheduleType type;
    private final long initialDelay;
    private final long period;
    private final TimeUnit timeUnit;
    private final String cronExpression;
    private final Instant startTime;
    private final Instant endTime;
    private final int maxExecutions;
    private final boolean fixedRate;
    
    private ScheduleConfig(Builder builder) {
        this.type = builder.type;
        this.initialDelay = builder.initialDelay;
        this.period = builder.period;
        this.timeUnit = builder.timeUnit;
        this.cronExpression = builder.cronExpression;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.maxExecutions = builder.maxExecutions;
        this.fixedRate = builder.fixedRate;
    }
    
    public ScheduleType getType() {
        return type;
    }
    
    public long getInitialDelay() {
        return initialDelay;
    }
    
    public long getPeriod() {
        return period;
    }
    
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public Instant getStartTime() {
        return startTime;
    }
    
    public Instant getEndTime() {
        return endTime;
    }
    
    public int getMaxExecutions() {
        return maxExecutions;
    }
    
    public boolean isFixedRate() {
        return fixedRate;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private ScheduleType type = ScheduleType.ONE_TIME;
        private long initialDelay = 0;
        private long period = 0;
        private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        private String cronExpression;
        private Instant startTime;
        private Instant endTime;
        private int maxExecutions = -1;
        private boolean fixedRate = true;
        
        public Builder type(ScheduleType type) {
            this.type = type;
            return this;
        }
        
        public Builder initialDelay(long initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }
        
        public Builder period(long period) {
            this.period = period;
            return this;
        }
        
        public Builder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }
        
        public Builder cronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            this.type = ScheduleType.CRON;
            return this;
        }
        
        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }
        
        public Builder endTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }
        
        public Builder maxExecutions(int maxExecutions) {
            this.maxExecutions = maxExecutions;
            return this;
        }
        
        public Builder fixedRate(boolean fixedRate) {
            this.fixedRate = fixedRate;
            return this;
        }
        
        public ScheduleConfig build() {
            return new ScheduleConfig(this);
        }
    }
}
