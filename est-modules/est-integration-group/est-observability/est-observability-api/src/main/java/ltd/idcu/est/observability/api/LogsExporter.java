package ltd.idcu.est.observability.api;

import java.util.Map;

/**
 * Logs日志导出器接口。
 * 
 * <p>提供五级日志级别（DEBUG、INFO、WARN、ERROR、FATAL）的日志记录功能，
 * 支持异常堆栈跟踪和上下文信息附加。
 * 
 * <h2>日志级别说明</h2>
 * <ul>
 *   <li><b>DEBUG</b>: 调试信息，用于开发调试</li>
 *   <li><b>INFO</b>: 一般信息，记录应用正常运行状态</li>
 *   <li><b>WARN</b>: 警告信息，表示潜在问题</li>
 *   <li><b>ERROR</b>: 错误信息，表示发生错误但不影响应用继续运行</li>
 *   <li><b>FATAL</b>: 致命错误，表示应用无法继续运行</li>
 * </ul>
 * 
 * <h2>使用示例</h2>
 * <pre>{@code
 * LogsExporter logs = new ElkLogsExporter("est-logs", "my-service");
 * logs.start();
 * 
 * logs.debug("Debug message");
 * logs.info("Info message");
 * logs.warn("Warning message");
 * logs.error("Error message");
 * 
 * Map<String, Object> context = Map.of(
 *     "user_id", "12345",
 *     "request_id", "abc-123"
 * );
 * logs.info("User login successful", context);
 * 
 * try {
 *     // ... 业务代码
 * } catch (Exception e) {
 *     logs.error("Operation failed", e, context);
 * }
 * 
 * logs.stop();
 * }</pre>
 * 
 * @since 2.4.0
 */
public interface LogsExporter {
    
    /**
     * 启动Logs导出器。
     * 
     * <p>启动后会初始化日志输出目标（如Elasticsearch连接）。
     */
    void start();
    
    /**
     * 停止Logs导出器。
     * 
     * <p>停止后会关闭日志输出连接并清理资源。
     */
    void stop();
    
    /**
     * 检查Logs导出器是否正在运行。
     * 
     * @return true表示正在运行，false表示已停止
     */
    boolean isRunning();
    
    /**
     * 记录一条日志。
     * 
     * @param level 日志级别
     * @param message 日志消息
     */
    void log(LogLevel level, String message);
    
    /**
     * 记录一条带上下文的日志。
     * 
     * @param level 日志级别
     * @param message 日志消息
     * @param context 上下文信息键值对
     */
    void log(LogLevel level, String message, Map<String, Object> context);
    
    /**
     * 记录一条带异常的日志。
     * 
     * @param level 日志级别
     * @param message 日志消息
     * @param throwable 异常对象
     */
    void log(LogLevel level, String message, Throwable throwable);
    
    /**
     * 记录一条带异常和上下文的日志。
     * 
     * @param level 日志级别
     * @param message 日志消息
     * @param throwable 异常对象
     * @param context 上下文信息键值对
     */
    void log(LogLevel level, String message, Throwable throwable, Map<String, Object> context);
    
    /**
     * 记录DEBUG级别日志。
     * 
     * @param message 日志消息
     */
    void debug(String message);
    
    /**
     * 记录DEBUG级别日志，带上下文。
     * 
     * @param message 日志消息
     * @param context 上下文信息键值对
     */
    void debug(String message, Map<String, Object> context);
    
    /**
     * 记录INFO级别日志。
     * 
     * @param message 日志消息
     */
    void info(String message);
    
    /**
     * 记录INFO级别日志，带上下文。
     * 
     * @param message 日志消息
     * @param context 上下文信息键值对
     */
    void info(String message, Map<String, Object> context);
    
    /**
     * 记录WARN级别日志。
     * 
     * @param message 日志消息
     */
    void warn(String message);
    
    /**
     * 记录WARN级别日志，带上下文。
     * 
     * @param message 日志消息
     * @param context 上下文信息键值对
     */
    void warn(String message, Map<String, Object> context);
    
    /**
     * 记录ERROR级别日志。
     * 
     * @param message 日志消息
     */
    void error(String message);
    
    /**
     * 记录ERROR级别日志，带上下文。
     * 
     * @param message 日志消息
     * @param context 上下文信息键值对
     */
    void error(String message, Map<String, Object> context);
    
    /**
     * 记录ERROR级别日志，带异常。
     * 
     * @param message 日志消息
     * @param throwable 异常对象
     */
    void error(String message, Throwable throwable);
    
    /**
     * 记录ERROR级别日志，带异常和上下文。
     * 
     * @param message 日志消息
     * @param throwable 异常对象
     * @param context 上下文信息键值对
     */
    void error(String message, Throwable throwable, Map<String, Object> context);
    
    /**
     * 日志级别枚举。
     */
    enum LogLevel {
        /** 调试信息 */
        DEBUG,
        /** 一般信息 */
        INFO,
        /** 警告信息 */
        WARN,
        /** 错误信息 */
        ERROR,
        /** 致命错误 */
        FATAL
    }
}
