package ltd.idcu.est.observability.api;

import java.util.Map;

/**
 * Metrics指标导出器接口。
 * 
 * <p>提供Counter、Gauge、Histogram、Summary四种标准指标类型的注册和操作。
 * 
 * <h2>指标类型说明</h2>
 * <ul>
 *   <li><b>Counter</b>: 计数器，只增不减，适用于请求计数、错误计数等</li>
 *   <li><b>Gauge</b>: 仪表盘，可增可减，适用于连接数、队列长度等</li>
 *   <li><b>Histogram</b>: 直方图，记录分布，适用于请求延迟、响应大小等</li>
 *   <li><b>Timer</b>: 计时器，记录时间分布，适用于方法执行时间等</li>
 * </ul>
 * 
 * <h2>使用示例</h2>
 * <pre>{@code
 * MetricsExporter metrics = new PrometheusMetricsExporter(9090);
 * metrics.start();
 * 
 * // 注册指标
 * metrics.registerCounter("http_requests_total", "Total HTTP requests", "method", "status");
 * metrics.registerGauge("active_connections", "Number of active connections");
 * metrics.registerHistogram("request_duration_seconds", "Request duration", 0.1, 0.5, 1.0);
 * metrics.registerTimer("database_query_time", "Database query time");
 * 
 * // 使用指标
 * Map<String, String> labels = Map.of("method", "GET", "status", "200");
 * metrics.incrementCounter("http_requests_total", labels);
 * metrics.setGauge("active_connections", 150);
 * metrics.recordHistogram("request_duration_seconds", 0.234);
 * metrics.recordTimer("database_query_time", 45);
 * 
 * metrics.stop();
 * }</pre>
 * 
 * @since 2.4.0
 */
public interface MetricsExporter {
    
    /**
     * 启动Metrics导出器。
     * 
     * <p>启动后会初始化指标注册中心并启动HTTP服务器（如适用）。
     */
    void start();
    
    /**
     * 停止Metrics导出器。
     * 
     * <p>停止后会关闭HTTP服务器并清理资源。
     */
    void stop();
    
    /**
     * 检查Metrics导出器是否正在运行。
     * 
     * @return true表示正在运行，false表示已停止
     */
    boolean isRunning();
    
    /**
     * 注册一个无标签的Counter指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     */
    void registerCounter(String name, String help);
    
    /**
     * 注册一个带标签的Counter指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     * @param labelNames 标签名称数组
     */
    void registerCounter(String name, String help, String... labelNames);
    
    /**
     * 无标签Counter递增1。
     * 
     * @param name 指标名称
     */
    void incrementCounter(String name);
    
    /**
     * 无标签Counter递增指定数量。
     * 
     * @param name 指标名称
     * @param amount 递增数量
     */
    void incrementCounter(String name, long amount);
    
    /**
     * 带标签Counter递增1。
     * 
     * @param name 指标名称
     * @param labels 标签键值对
     */
    void incrementCounter(String name, Map<String, String> labels);
    
    /**
     * 注册一个无标签的Gauge指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     */
    void registerGauge(String name, String help);
    
    /**
     * 注册一个带标签的Gauge指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     * @param labelNames 标签名称数组
     */
    void registerGauge(String name, String help, String... labelNames);
    
    /**
     * 设置无标签Gauge的值。
     * 
     * @param name 指标名称
     * @param value 指标值
     */
    void setGauge(String name, double value);
    
    /**
     * 设置带标签Gauge的值。
     * 
     * @param name 指标名称
     * @param value 指标值
     * @param labels 标签键值对
     */
    void setGauge(String name, double value, Map<String, String> labels);
    
    /**
     * 注册一个Histogram指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     * @param buckets 直方图桶边界数组
     */
    void registerHistogram(String name, String help, double... buckets);
    
    /**
     * 记录无标签Histogram的值。
     * 
     * @param name 指标名称
     * @param value 观测值
     */
    void recordHistogram(String name, double value);
    
    /**
     * 记录带标签Histogram的值。
     * 
     * @param name 指标名称
     * @param value 观测值
     * @param labels 标签键值对
     */
    void recordHistogram(String name, double value, Map<String, String> labels);
    
    /**
     * 注册一个Timer指标。
     * 
     * @param name 指标名称
     * @param help 指标帮助说明
     */
    void registerTimer(String name, String help);
    
    /**
     * 记录无标签Timer的时间。
     * 
     * @param name 指标名称
     * @param milliseconds 时间（毫秒）
     */
    void recordTimer(String name, long milliseconds);
    
    /**
     * 记录带标签Timer的时间。
     * 
     * @param name 指标名称
     * @param milliseconds 时间（毫秒）
     * @param labels 标签键值对
     */
    void recordTimer(String name, long milliseconds, Map<String, String> labels);
    
    /**
     * 抓取当前所有指标数据。
     * 
     * @return 格式化为文本的指标数据
     */
    String scrape();
    
    /**
     * 获取当前注册的所有指标信息。
     * 
     * @return 包含指标信息的Map
     */
    Map<String, Object> getMetrics();
}
