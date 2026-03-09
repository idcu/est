package ltd.idcu.est.observability.api;

/**
 * EST Framework可观测性统一接口。
 * 
 * <p>提供Metrics、Logs、Traces三支柱可观测能力的统一管理入口。
 * 
 * <h2>使用示例</h2>
 * <pre>{@code
 * MetricsExporter metrics = new PrometheusMetricsExporter(9090);
 * LogsExporter logs = new ElkLogsExporter("est-logs", "my-service");
 * TracesExporter traces = new OpenTelemetryTracesExporter("my-service", "1.0.0");
 * 
 * Observability observability = new DefaultObservability(
 *         "my-service",
 *         "1.0.0",
 *         metrics,
 *         logs,
 *         traces
 * );
 * 
 * observability.start();
 * // ... 使用可观测性功能
 * observability.stop();
 * }</pre>
 * 
 * @since 2.4.0
 */
public interface Observability {
    
    /**
     * 获取Metrics导出器。
     * 
     * @return Metrics导出器实例，用于指标采集和导出
     */
    MetricsExporter getMetricsExporter();
    
    /**
     * 获取Logs导出器。
     * 
     * @return Logs导出器实例，用于日志收集和导出
     */
    LogsExporter getLogsExporter();
    
    /**
     * 获取Traces导出器。
     * 
     * @return Traces导出器实例，用于分布式追踪
     */
    TracesExporter getTracesExporter();
    
    /**
     * 启动可观测性服务。
     * 
     * <p>启动后会依次启动Metrics、Logs、Traces导出器。
     * 如果已经在运行状态，则此方法不会执行任何操作。
     */
    void start();
    
    /**
     * 停止可观测性服务。
     * 
     * <p>停止后会依次停止Traces、Logs、Metrics导出器。
     * 如果已经在停止状态，则此方法不会执行任何操作。
     */
    void stop();
    
    /**
     * 检查可观测性服务是否正在运行。
     * 
     * @return true表示正在运行，false表示已停止
     */
    boolean isRunning();
    
    /**
     * 获取服务名称。
     * 
     * @return 服务名称，用于标识当前应用
     */
    String getServiceName();
    
    /**
     * 获取服务版本。
     * 
     * @return 服务版本号，用于标识当前应用版本
     */
    String getServiceVersion();
}
