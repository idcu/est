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

public class LogAnalyzerPlugin extends BaseEstPlugin {

    private static final String PLUGIN_ID = "log-analyzer-plugin";
    private static final String PLUGIN_NAME = "Log Analyzer Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "日志分析插件，提供日志解析、统计、搜索、可视化等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";

    private List<LogPattern> logPatterns;
    private Path workDir;

    public LogAnalyzerPlugin() {
        this.logPatterns = new ArrayList<>();
        
        addCapability("features", Map.of(
            "parse", true,
            "search", true,
            "stats", true,
            "filter", true,
            "export", true
        ));
        addCapability("commands", new String[]{"log_parse", "log_search", "log_stats", "log_filter", "log_export"});
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
        this.workDir = context.getWorkDir();
        logInfo("日志分析插件初始化完成");
        initDefaultPatterns();
    }

    private void initDefaultPatterns() {
        logPatterns.add(new LogPattern(
            "standard",
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s+(\\w+)\\s+\\[(.*?)\\]\\s+(.*)$",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        ));
        logPatterns.add(new LogPattern(
            "simple",
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(\\w+)\\s+(.*)$",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));
        logPatterns.add(new LogPattern(
            "json",
            "\\{.*\"timestamp\":\"([^\"]+)\".*\"level\":\"([^\"]+)\".*\"message\":\"([^\"]+)\".*\\}",
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ));
    }

    public void addLogPattern(String name, String pattern, String dateFormat) {
        logPatterns.add(new LogPattern(name, pattern, DateTimeFormatter.ofPattern(dateFormat)));
        logInfo("添加日志格式: " + name);
    }

    public List<LogEntry> parseLogFile(Path logFile) throws PluginException {
        return parseLogFile(logFile, null);
    }

    public List<LogEntry> parseLogFile(Path logFile, String patternName) throws PluginException {
        List<LogEntry> entries = new ArrayList<>();
        
        if (!Files.exists(logFile)) {
            throw new PluginException("日志文件不存在: " + logFile);
        }
        
        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                LogEntry entry = parseLogLine(line, lineNumber, patternName);
                if (entry != null) {
                    entries.add(entry);
                }
            }
            
            logInfo("解析日志文件完成，共 " + entries.size() + " 条记录");
            return entries;
        } catch (IOException e) {
            throw new PluginException("读取日志文件失败: " + e.getMessage(), e);
        }
    }

    private LogEntry parseLogLine(String line, int lineNumber, String patternName) {
        List<LogPattern> patterns = patternName != null 
            ? logPatterns.stream().filter(p -> p.name.equals(patternName)).collect(Collectors.toList())
            : logPatterns;
        
        for (LogPattern pattern : patterns) {
            Matcher matcher = pattern.pattern.matcher(line);
            if (matcher.matches()) {
                try {
                    LocalDateTime timestamp = null;
                    if (matcher.groupCount() >= 1) {
                        try {
                            timestamp = LocalDateTime.parse(matcher.group(1), pattern.dateFormatter);
                        } catch (DateTimeParseException e) {
                        }
                    }
                    
                    String level = matcher.groupCount() >= 2 ? matcher.group(2) : "UNKNOWN";
                    String message = matcher.groupCount() >= 3 ? matcher.group(matcher.groupCount()) : line;
                    
                    return new LogEntry(timestamp, level, message, lineNumber, line);
                } catch (Exception e) {
                }
            }
        }
        
        return new LogEntry(null, "UNKNOWN", line, lineNumber, line);
    }

    public LogStatistics analyzeLogs(List<LogEntry> entries) {
        LogStatistics stats = new LogStatistics();
        
        for (LogEntry entry : entries) {
            stats.totalEntries++;
            
            stats.levelCount.merge(entry.level, 1, Integer::sum);
            
            if (entry.timestamp != null) {
                if (stats.firstTimestamp == null || entry.timestamp.isBefore(stats.firstTimestamp)) {
                    stats.firstTimestamp = entry.timestamp;
                }
                if (stats.lastTimestamp == null || entry.timestamp.isAfter(stats.lastTimestamp)) {
                    stats.lastTimestamp = entry.timestamp;
                }
            }
            
            for (String keyword : Arrays.asList("error", "exception", "fail", "warning", "critical")) {
                if (entry.message.toLowerCase().contains(keyword)) {
                    stats.keywordCount.merge(keyword, 1, Integer::sum);
                }
            }
        }
        
        return stats;
    }

    public List<LogEntry> searchLogs(List<LogEntry> entries, String keyword) {
        return entries.stream()
            .filter(e -> e.message.toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<LogEntry> filterByLevel(List<LogEntry> entries, String... levels) {
        Set<String> levelSet = new HashSet<>(Arrays.asList(levels));
        return entries.stream()
            .filter(e -> levelSet.contains(e.level.toUpperCase()))
            .collect(Collectors.toList());
    }

    public List<LogEntry> filterByTimeRange(List<LogEntry> entries, LocalDateTime start, LocalDateTime end) {
        return entries.stream()
            .filter(e -> e.timestamp != null)
            .filter(e -> !e.timestamp.isBefore(start) && !e.timestamp.isAfter(end))
            .collect(Collectors.toList());
    }

    public Map<String, List<LogEntry>> groupByLevel(List<LogEntry> entries) {
        return entries.stream()
            .collect(Collectors.groupingBy(e -> e.level));
    }

    public Map<String, List<LogEntry>> groupByHour(List<LogEntry> entries) {
        return entries.stream()
            .filter(e -> e.timestamp != null)
            .collect(Collectors.groupingBy(e -> e.timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"))));
    }

    public String exportToText(List<LogEntry> entries) {
        StringBuilder sb = new StringBuilder();
        for (LogEntry entry : entries) {
            sb.append("[").append(entry.lineNumber).append("] ");
            if (entry.timestamp != null) {
                sb.append(entry.timestamp).append(" ");
            }
            sb.append("[").append(entry.level).append("] ");
            sb.append(entry.message).append("\n");
        }
        return sb.toString();
    }

    public String exportToCsv(List<LogEntry> entries) {
        StringBuilder sb = new StringBuilder();
        sb.append("Line,Timestamp,Level,Message\n");
        
        for (LogEntry entry : entries) {
            sb.append(entry.lineNumber).append(",");
            sb.append(entry.timestamp != null ? entry.timestamp : "").append(",");
            sb.append("\"").append(entry.level).append("\",");
            sb.append("\"").append(entry.message.replace("\"", "\"\"")).append("\"\n");
        }
        
        return sb.toString();
    }

    public String generateSummaryReport(List<LogEntry> entries) {
        LogStatistics stats = analyzeLogs(entries);
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== 日志分析报告 ===\n\n");
        sb.append("总记录数: ").append(stats.totalEntries).append("\n");
        sb.append("时间范围: ").append(stats.firstTimestamp).append(" - ").append(stats.lastTimestamp).append("\n\n");
        
        sb.append("日志级别统计:\n");
        for (Map.Entry<String, Integer> entry : stats.levelCount.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        sb.append("\n关键字统计:\n");
        for (Map.Entry<String, Integer> entry : stats.keywordCount.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return sb.toString();
    }

    public List<Path> findLogFiles() throws PluginException {
        return findLogFiles(workDir);
    }

    public List<Path> findLogFiles(Path directory) throws PluginException {
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(p -> {
                    String name = p.getFileName().toString().toLowerCase();
                    return name.endsWith(".log") || name.endsWith(".txt") && name.contains("log");
                })
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PluginException("查找日志文件失败: " + e.getMessage(), e);
        }
    }

    public static class LogEntry {
        public final LocalDateTime timestamp;
        public final String level;
        public final String message;
        public final int lineNumber;
        public final String rawLine;

        public LogEntry(LocalDateTime timestamp, String level, String message, int lineNumber, String rawLine) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
            this.lineNumber = lineNumber;
            this.rawLine = rawLine;
        }
    }

    public static class LogStatistics {
        public int totalEntries = 0;
        public LocalDateTime firstTimestamp;
        public LocalDateTime lastTimestamp;
        public Map<String, Integer> levelCount = new HashMap<>();
        public Map<String, Integer> keywordCount = new HashMap<>();
    }

    private static class LogPattern {
        final String name;
        final Pattern pattern;
        final DateTimeFormatter dateFormatter;

        LogPattern(String name, String pattern, DateTimeFormatter dateFormatter) {
            this.name = name;
            this.pattern = Pattern.compile(pattern);
            this.dateFormatter = dateFormatter;
        }
    }
}
