package ltd.idcu.est.features.scheduler.cron;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.BitSet;

public class CronExpression {
    
    private final String expression;
    private final BitSet seconds;
    private final BitSet minutes;
    private final BitSet hours;
    private final BitSet daysOfMonth;
    private final BitSet months;
    private final BitSet daysOfWeek;
    private final ZoneId zoneId;
    
    public CronExpression(String expression) {
        this(expression, ZoneId.systemDefault());
    }
    
    public CronExpression(String expression, ZoneId zoneId) {
        this.expression = expression;
        this.zoneId = zoneId;
        this.seconds = new BitSet(60);
        this.minutes = new BitSet(60);
        this.hours = new BitSet(24);
        this.daysOfMonth = new BitSet(32);
        this.months = new BitSet(13);
        this.daysOfWeek = new BitSet(7);
        parse(expression);
    }
    
    private void parse(String expression) {
        String[] fields = expression.trim().split("\\s+");
        if (fields.length < 5 || fields.length > 6) {
            throw new ltd.idcu.est.features.scheduler.api.SchedulerException(
                    null, 
                    ltd.idcu.est.features.scheduler.api.SchedulerException.ErrorCode.INVALID_CRON_EXPRESSION,
                    "Invalid cron expression: " + expression + ". Expected 5 or 6 fields."
            );
        }
        
        int index = 0;
        if (fields.length == 6) {
            parseField(fields[index++], seconds, 0, 59);
        } else {
            seconds.set(0);
        }
        parseField(fields[index++], minutes, 0, 59);
        parseField(fields[index++], hours, 0, 23);
        parseField(fields[index++], daysOfMonth, 1, 31);
        parseField(fields[index++], months, 1, 12);
        parseField(fields[index], daysOfWeek, 0, 6);
    }
    
    private void parseField(String field, BitSet bits, int min, int max) {
        bits.clear();
        
        if (field.equals("*")) {
            bits.set(min, max + 1);
            return;
        }
        
        String[] parts = field.split(",");
        for (String part : parts) {
            parsePart(part.trim(), bits, min, max);
        }
    }
    
    private void parsePart(String part, BitSet bits, int min, int max) {
        int step = 1;
        String valuePart = part;
        
        int slashIndex = part.indexOf('/');
        if (slashIndex >= 0) {
            step = Integer.parseInt(part.substring(slashIndex + 1));
            valuePart = part.substring(0, slashIndex);
        }
        
        if (valuePart.equals("*")) {
            for (int i = min; i <= max; i += step) {
                bits.set(i);
            }
        } else if (valuePart.contains("-")) {
            String[] range = valuePart.split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            for (int i = start; i <= end; i += step) {
                bits.set(i);
            }
        } else {
            int value = Integer.parseInt(valuePart);
            bits.set(value);
            if (step > 1) {
                for (int i = value + step; i <= max; i += step) {
                    bits.set(i);
                }
            }
        }
    }
    
    public Instant getNextExecutionTime(Instant afterTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(afterTime, zoneId);
        LocalDateTime next = dateTime.plusSeconds(1).truncatedTo(ChronoUnit.SECONDS);
        
        for (int i = 0; i < 366 * 24 * 60 * 60; i++) {
            if (matches(next)) {
                return next.atZone(zoneId).toInstant();
            }
            next = next.plusSeconds(1);
        }
        
        return null;
    }
    
    public boolean matches(LocalDateTime dateTime) {
        if (!seconds.get(dateTime.getSecond())) return false;
        if (!minutes.get(dateTime.getMinute())) return false;
        if (!hours.get(dateTime.getHour())) return false;
        if (!daysOfMonth.get(dateTime.getDayOfMonth())) return false;
        if (!months.get(dateTime.getMonthValue())) return false;
        if (!daysOfWeek.get(dateTime.getDayOfWeek().getValue() % 7)) return false;
        
        return true;
    }
    
    public boolean matches(Instant instant) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);
        return matches(dateTime);
    }
    
    public String getExpression() {
        return expression;
    }
    
    public ZoneId getZoneId() {
        return zoneId;
    }
    
    @Override
    public String toString() {
        return "CronExpression{" + expression + "}";
    }
    
    public static CronExpression from(String expression) {
        return new CronExpression(expression);
    }
    
    public static boolean isValid(String expression) {
        try {
            new CronExpression(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
