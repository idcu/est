package ltd.idcu.est.utils.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public final class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATETIME_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String COMPACT_DATETIME_WITH_MILLIS_FORMAT = "yyyyMMddHHmmssSSS";

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(ISO_DATETIME_FORMAT);
    private static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATE_FORMAT);
    private static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATETIME_FORMAT);
    private static final DateTimeFormatter COMPACT_DATETIME_WITH_MILLIS_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATETIME_WITH_MILLIS_FORMAT);

    private DateUtils() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalTime timeNow() {
        return LocalTime.now();
    }

    public static long currentMillis() {
        return System.currentTimeMillis();
    }

    public static long currentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static long currentNanos() {
        return System.nanoTime();
    }

    public static String format(LocalDate date) {
        return date == null ? null : date.format(DEFAULT_DATE_FORMATTER);
    }

    public static String format(LocalDate date, String pattern) {
        return date == null ? null : date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalTime time) {
        return time == null ? null : time.format(DEFAULT_TIME_FORMATTER);
    }

    public static String format(LocalTime time, String pattern) {
        return time == null ? null : time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatCompact(LocalDate date) {
        return date == null ? null : date.format(COMPACT_DATE_FORMATTER);
    }

    public static String formatCompact(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(COMPACT_DATETIME_FORMATTER);
    }

    public static String formatCompactWithMillis(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(COMPACT_DATETIME_WITH_MILLIS_FORMATTER);
    }

    public static String formatISO(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(ISO_DATETIME_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dateStr);
        }
    }

    public static LocalDate parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseTime(String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, DEFAULT_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalTime.parse(timeStr);
        }
    }

    public static LocalTime parseTime(String timeStr, String pattern) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DEFAULT_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(dateTimeStr);
        }
    }

    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseCompactDateTime(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        if (dateTimeStr.length() == 8) {
            return LocalDate.parse(dateTimeStr, COMPACT_DATE_FORMATTER).atStartOfDay();
        }
        if (dateTimeStr.length() == 14) {
            return LocalDateTime.parse(dateTimeStr, COMPACT_DATETIME_FORMATTER);
        }
        if (dateTimeStr.length() == 17) {
            return LocalDateTime.parse(dateTimeStr, COMPACT_DATETIME_WITH_MILLIS_FORMATTER);
        }
        throw new DateTimeParseException("Invalid compact datetime format", dateTimeStr, 0);
    }

    public static Date toDate(LocalDate date) {
        return date == null ? null : Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    public static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Instant toInstant(Date date) {
        return date == null ? null : date.toInstant();
    }

    public static Instant toInstant(LocalDate date) {
        return date == null ? null : date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        return dateTime == null ? null : dateTime.plusYears(years);
    }

    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime == null ? null : dateTime.plusMonths(months);
    }

    public static LocalDateTime plusWeeks(LocalDateTime dateTime, long weeks) {
        return dateTime == null ? null : dateTime.plusWeeks(weeks);
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.plusDays(days);
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.plusHours(hours);
    }

    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime == null ? null : dateTime.plusMinutes(minutes);
    }

    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime == null ? null : dateTime.plusSeconds(seconds);
    }

    public static LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        return dateTime == null ? null : dateTime.minusYears(years);
    }

    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        return dateTime == null ? null : dateTime.minusMonths(months);
    }

    public static LocalDateTime minusWeeks(LocalDateTime dateTime, long weeks) {
        return dateTime == null ? null : dateTime.minusWeeks(weeks);
    }

    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.minusDays(days);
    }

    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.minusHours(hours);
    }

    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime == null ? null : dateTime.minusMinutes(minutes);
    }

    public static LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime == null ? null : dateTime.minusSeconds(seconds);
    }

    public static long yearsBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.YEARS.between(start, end);
    }

    public static long monthsBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MONTHS.between(start, end);
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(start, end);
    }

    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(start, end);
    }

    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(start, end);
    }

    public static long millisBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MILLIS.between(start, end);
    }

    public static boolean isBefore(LocalDateTime dateTime, LocalDateTime other) {
        if (dateTime == null || other == null) {
            return false;
        }
        return dateTime.isBefore(other);
    }

    public static boolean isAfter(LocalDateTime dateTime, LocalDateTime other) {
        if (dateTime == null || other == null) {
            return false;
        }
        return dateTime.isAfter(other);
    }

    public static boolean isEqual(LocalDateTime dateTime, LocalDateTime other) {
        if (dateTime == null || other == null) {
            return dateTime == other;
        }
        return dateTime.isEqual(other);
    }

    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }

    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }

    public static boolean isToday(LocalDateTime dateTime) {
        return dateTime != null && dateTime.toLocalDate().isEqual(LocalDate.now());
    }

    public static boolean isYesterday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now().minusDays(1));
    }

    public static boolean isTomorrow(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now().plusDays(1));
    }

    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }

    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate lastDayOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDate firstDayOfNextMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    public static LocalDate firstDayOfNextYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfNextYear());
    }

    public static LocalDate nextOrSame(LocalDate date, DayOfWeek dayOfWeek) {
        return date == null ? null : date.with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    public static LocalDate previousOrSame(LocalDate date, DayOfWeek dayOfWeek) {
        return date == null ? null : date.with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        return date == null ? null : date.atTime(23, 59, 59, 999999999);
    }

    public static LocalDateTime startOfMonth(LocalDate date) {
        return date == null ? null : firstDayOfMonth(date).atStartOfDay();
    }

    public static LocalDateTime endOfMonth(LocalDate date) {
        return date == null ? null : lastDayOfMonth(date).atTime(23, 59, 59, 999999999);
    }

    public static LocalDateTime startOfYear(LocalDate date) {
        return date == null ? null : firstDayOfYear(date).atStartOfDay();
    }

    public static LocalDateTime endOfYear(LocalDate date) {
        return date == null ? null : lastDayOfYear(date).atTime(23, 59, 59, 999999999);
    }

    public static int getYear(LocalDate date) {
        return date == null ? 0 : date.getYear();
    }

    public static int getMonth(LocalDate date) {
        return date == null ? 0 : date.getMonthValue();
    }

    public static int getDayOfMonth(LocalDate date) {
        return date == null ? 0 : date.getDayOfMonth();
    }

    public static int getDayOfWeek(LocalDate date) {
        return date == null ? 0 : date.getDayOfWeek().getValue();
    }

    public static int getDayOfYear(LocalDate date) {
        return date == null ? 0 : date.getDayOfYear();
    }

    public static int getHour(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.getHour();
    }

    public static int getMinute(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.getMinute();
    }

    public static int getSecond(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.getSecond();
    }

    public static int getNano(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.getNano();
    }

    public static int lengthOfMonth(LocalDate date) {
        return date == null ? 0 : date.lengthOfMonth();
    }

    public static int lengthOfYear(LocalDate date) {
        return date == null ? 0 : date.lengthOfYear();
    }

    public static boolean isLeapYear(LocalDate date) {
        return date != null && date.isLeapYear();
    }

    public static ZoneId systemZone() {
        return ZoneId.systemDefault();
    }

    public static ZoneOffset systemOffset() {
        return ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
    }

    public static LocalDateTime toZone(LocalDateTime dateTime, ZoneId fromZone, ZoneId toZone) {
        if (dateTime == null || fromZone == null || toZone == null) {
            return null;
        }
        return dateTime.atZone(fromZone).withZoneSameInstant(toZone).toLocalDateTime();
    }

    public static LocalDateTime toUTC(LocalDateTime dateTime, ZoneId fromZone) {
        return toZone(dateTime, fromZone, ZoneOffset.UTC);
    }

    public static LocalDateTime fromUTC(LocalDateTime dateTime, ZoneId toZone) {
        return toZone(dateTime, ZoneOffset.UTC, toZone);
    }

    public static long toEpochMilli(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    public static long toEpochSecond(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public static LocalDateTime fromEpochSecond(long epochSecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
    }
}
