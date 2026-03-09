package ltd.idcu.est.codecli.plugin.official;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogAnalysisPlugin extends BaseEstPlugin {

    private static final String PLUGIN_ID = "log-analysis-plugin";
    private static final String PLUGIN_NAME = "Log Analysis Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "日志分析插件，提供日志解析、过滤、统计、搜索等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";

    private List<LogPattern> logPatterns;
    private Map<String, Long> statistics;

    public LogAnalysisPlugin() {
        this.logPatterns = new ArrayList<>();
        this.statistics = new HashMap<>();
        
        addCapability("features", Map.of(
            "parse", true,
            "filter", true,
            "search", true,
            "statistics", true,
            "tail", true
        ));
        addCapability("commands", new String[]{"log_parse", "log_filter", "log_search", "log_stats", "log_tail"});
    }

    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }

    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }

    @Override
    protected void onInitialize() throws PluginException {
        logInfo("日志分析插件初始化完成");
        registerDefaultPatterns();
    }

    private void registerDefaultPatterns() {
        logPatterns.add(new LogPattern("standard", 
            Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(\\w+)\\s+\\[(.*?)\\]\\s+(.*)"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        logPatterns.add(new LogPattern("simple",
            Pattern.compile("(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(\\w+)\\s+(.*)"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
    }

    public void addLogPattern(String name, String pattern, String dateFormat) {
        logPatterns.add(new LogPattern(name, Pattern.compile(pattern), DateTimeFormatter.ofPattern(dateFormat)));
        logInfo("添加日志格式: " + name);
    }

    public List<LogEntry> parseLogFile(Path logFile) throws PluginException {
        return parseLogFile(logFile, null);
    }

    public List<LogEntry> parseLogFile(Path logFile, String patternName) throws PluginException {
        logInfo("开始解析日志文件: " + logFile);
        
        List<LogEntry> entries = new ArrayList<>();
        LogPattern patternToUse = patternName != null ? 
            logPatterns.stream().filter(p -> p.name.equals(patternName)).findFirst().orElse(logPatterns.get(0)) :
            logPatterns.get(0);

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry entry = parseLine(line, patternToUse);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            throw new PluginException("读取日志文件失败: " + e.getMessage(), e);
        }

        logInfo("日志解析完成，共 " + entries.size() + " 条记录");
        return entries;
    }

    private LogEntry parseLine(String line, LogPattern pattern) {
        Matcher matcher = pattern.pattern.matcher(line);
        if (matcher.matches()) {
            try {
                LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), pattern.dateFormatter);
                String level = matcher.group(2);
                String message = matcher.groupCount() > 3 ? matcher.group(4) : matcher.group(3);
                String logger = matcher.groupCount() > 3 ? matcher.group(3) : "";
                
                return new LogEntry(timestamp, level, logger, message, line);
            } catch (DateTimeParseException e) {
                return new LogEntry(null, "UNKNOWN", "", line, line);
            }
        }
        return new LogEntry(null, "UNKNOWN", "", line, line);
    }

    public List<LogEntry> filterByLevel(List<LogEntry> entries, String level) {
        return entries.stream()
            .filter(e -> e.level.equalsIgnoreCase(level))
            .collect(Collectors.toList());
    }

    public List<LogEntry> filterByLevels(List<LogEntry> entries, Set<String> levels) {
        return entries.stream()
            .filter(e -> levels.contains(e.level.toUpperCase()))
            .collect(Collectors.toList());
    }

    public List<LogEntry> filterByTimeRange(List<LogEntry> entries, LocalDateTime start, LocalDateTime end) {
        return entries.stream()
            .filter(e -> e.timestamp != null)
            .filter(e -> !e.timestamp.isBefore(start) && !e.timestamp.isAfter(end))
            .collect(Collectors.toList());
    }

    public List<LogEntry> searchByKeyword(List<LogEntry> entries, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return entries.stream()
            .filter(e -> e.message.toLowerCase().contains(lowerKeyword) || 
                        e.rawLine.toLowerCase().contains(lowerKeyword))
            .collect(Collectors.toList());
    }

    public List<LogEntry> searchByRegex(List<LogEntry> entries, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return entries.stream()
            .filter(e -> pattern.matcher(e.rawLine).find())
            .collect(Collectors.toList());
    }

    public Map<String, Long> getLevelStatistics(List<LogEntry> entries) {
        Map<String, Long> stats = entries.stream()
            .collect(Collectors.groupingBy(e -> e.level, Collectors.counting()));
        
        logInfo("日志级别统计: " + stats);
        return stats;
    }

    public Map<String, Long> getLoggerStatistics(List<LogEntry> entries) {
        Map<String, Long> stats = entries.stream()
            .filter(e -> e.logger != null && !e.logger.isEmpty())
            .collect(Collectors.groupingBy(e -> e.logger, Collectors.counting()));
        
        logInfo("Logger 统计: " + stats.size() + " 个 logger");
        return stats;
    }

    public Map<Integer, Long> getHourlyStatistics(List<LogEntry> entries) {
        Map<Integer, Long> stats = entries.stream()
            .filter(e -> e.timestamp != null)
            .collect(Collectors.groupingBy(e -> e.timestamp.getHour(), Collectors.counting()));
        
        logInfo("小时统计: " + stats);
        return stats;
    }

    public List<String> getTopErrors(List<LogEntry> entries, int limit) {
        return entries.stream()
            .filter(e -> "ERROR".equalsIgnoreCase(e.level))
            .map(e -> e.message)
            .collect(Collectors.groupingBy(m -> m, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .map(e -> e.getKey() + " (" + e.getValue() + "次)")
            .collect(Collectors.toList());
    }

    public List<String> tailLogFile(Path logFile, int lines) throws PluginException {
        List<String> result = new ArrayList<>();
        
        try (Stream<String> stream = Files.lines(logFile)) {
            stream.skip(Math.max(0, Files.lines(logFile).count() - lines))
                  .forEach(result::add);
        } catch (IOException e) {
            throw new PluginException("读取日志文件末尾失败: " + e.getMessage(), e);
        }
        
        logInfo("获取日志文件最后 " + result.size() + " 行");
        return result;
    }

    public String generateReport(List<LogEntry> entries) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== 日志分析报告 ===\n\n");
        
        Map<String, Long> levelStats = getLevelStatistics(entries);
        sb.append("总记录数: ").append(entries.size()).append("\n");
        sb.append("级别统计:\n");
        levelStats.forEach((level, count) -> 
            sb.append("  ").append(level).append(": ").append(count).append("\n"));
        
        sb.append("\nTop 5 错误:\n");
        getTopErrors(entries, 5).forEach(error -> 
            sb.append("  ").append(error).append("\n"));
        
        return sb.toString();
    }

    public static class LogEntry {
        public final LocalDateTime timestamp;
        public final String level;
        public final String logger;
        public final String message;
        public final String rawLine;

        public LogEntry(LocalDateTime timestamp, String level, String logger, String message, String rawLine) {
            this.timestamp = timestamp;
            this.level = level;
            this.logger = logger;
            this.message = message;
            this.rawLine = rawLine;
        }
    }

    private static class LogPattern {
        final String name;
        final Pattern pattern;
        final DateTimeFormatter dateFormatter;

        LogPattern(String name, Pattern pattern, DateTimeFormatter dateFormatter) {
            this.name = name;
            this.pattern = pattern;
            this.dateFormatter = dateFormatter;
        }
    }
}
