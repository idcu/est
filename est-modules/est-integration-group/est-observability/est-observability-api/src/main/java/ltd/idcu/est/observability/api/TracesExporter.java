package ltd.idcu.est.observability.api;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Traces分布式追踪导出器接口。
 * 
 * <p>提供Span创建、Tag添加、Event记录等分布式追踪功能，
 * 支持W3C Trace Context标准和跨服务追踪。
 * 
 * <h2>核心概念</h2>
 * <ul>
 *   <li><b>Trace</b>: 一次完整的请求链路，由多个Span组成</li>
 *   <li><b>Span</b>: 链路中的一个操作单元，如方法调用、HTTP请求等</li>
 *   <li><b>Tag</b>: Span的键值对标签，用于标记Span的属性</li>
 *   <li><b>Event</b>: Span的事件记录，用于记录时间点的标记</li>
 * </ul>
 * 
 * <h2>使用示例</h2>
 * <pre>{@code
 * TracesExporter traces = new OpenTelemetryTracesExporter("my-service", "1.0.0");
 * traces.start();
 * 
 * // 方式1：手动管理Span
 * try (TraceScope parentScope = traces.startSpan("parent-operation")) {
 *     traces.addTag(parentScope, "operation", "create");
 *     traces.addEvent(parentScope, "started");
 *     
 *     try (TraceScope childScope = traces.startSpan("child-operation", parentScope.getContext())) {
 *         traces.addTag(childScope, "step", 1);
 *         traces.addEvent(childScope, "processing");
 *     }
 *     
 *     traces.addEvent(parentScope, "completed");
 * }
 * 
 * // 方式2：使用便捷方法
 * String result = traces.trace("lambda-operation", () -> {
 *     // 业务代码
 *     return "success";
 * });
 * 
 * traces.trace("runnable-operation", () -> {
 *     // 业务代码
 * });
 * 
 * traces.stop();
 * }</pre>
 * 
 * @since 2.4.0
 */
public interface TracesExporter {
    
    /**
     * 启动Traces导出器。
     * 
     * <p>启动后会初始化追踪器和导出器。
     */
    void start();
    
    /**
     * 停止Traces导出器。
     * 
     * <p>停止后会关闭导出器并清理资源。
     */
    void stop();
    
    /**
     * 检查Traces导出器是否正在运行。
     * 
     * @return true表示正在运行，false表示已停止
     */
    boolean isRunning();
    
    /**
     * 启动一个新的Span，作为当前Trace的根Span或新Trace的根Span。
     * 
     * @param spanName Span名称
     * @return TraceScope对象，用于管理Span的生命周期
     */
    TraceScope startSpan(String spanName);
    
    /**
     * 启动一个新的Span，作为指定父Span的子Span。
     * 
     * @param spanName Span名称
     * @param parent 父Span的TraceContext
     * @return TraceScope对象，用于管理Span的生命周期
     */
    TraceScope startSpan(String spanName, TraceContext parent);
    
    /**
     * 结束一个Span，默认标记为成功。
     * 
     * @param scope 要结束的TraceScope
     */
    void endSpan(TraceScope scope);
    
    /**
     * 结束一个Span，指定成功或失败状态。
     * 
     * @param scope 要结束的TraceScope
     * @param success true表示成功，false表示失败
     */
    void endSpan(TraceScope scope, boolean success);
    
    /**
     * 为Span添加String类型的Tag。
     * 
     * @param scope TraceScope对象
     * @param key Tag键
     * @param value Tag值
     */
    void addTag(TraceScope scope, String key, String value);
    
    /**
     * 为Span添加long类型的Tag。
     * 
     * @param scope TraceScope对象
     * @param key Tag键
     * @param value Tag值
     */
    void addTag(TraceScope scope, String key, long value);
    
    /**
     * 为Span添加boolean类型的Tag。
     * 
     * @param scope TraceScope对象
     * @param key Tag键
     * @param value Tag值
     */
    void addTag(TraceScope scope, String key, boolean value);
    
    /**
     * 为Span添加double类型的Tag。
     * 
     * @param scope TraceScope对象
     * @param key Tag键
     * @param value Tag值
     */
    void addTag(TraceScope scope, String key, double value);
    
    /**
     * 为Span添加一个Event。
     * 
     * @param scope TraceScope对象
     * @param eventName 事件名称
     */
    void addEvent(TraceScope scope, String eventName);
    
    /**
     * 为Span添加一个带属性的Event。
     * 
     * @param scope TraceScope对象
     * @param eventName 事件名称
     * @param attributes 事件属性键值对
     */
    void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes);
    
    /**
     * 追踪一个Supplier操作，自动创建和结束Span。
     * 
     * @param spanName Span名称
     * @param supplier 要执行的Supplier
     * @param <T> 返回值类型
     * @return Supplier的返回值
     */
    <T> T trace(String spanName, Supplier<T> supplier);
    
    /**
     * 追踪一个Runnable操作，自动创建和结束Span。
     * 
     * @param spanName Span名称
     * @param runnable 要执行的Runnable
     */
    void trace(String spanName, Runnable runnable);
    
    /**
     * 获取当前线程的TraceContext。
     * 
     * @return 当前TraceContext，如果没有则返回null
     */
    TraceContext getCurrentContext();
    
    /**
     * 设置当前线程的TraceContext。
     * 
     * @param context 要设置的TraceContext
     */
    void setCurrentContext(TraceContext context);
    
    /**
     * 清除当前线程的TraceContext。
     */
    void clearCurrentContext();
    
    /**
     * 生成一个新的Trace ID。
     * 
     * @return 32位十六进制Trace ID
     */
    String generateTraceId();
    
    /**
     * 生成一个新的Span ID。
     * 
     * @return 16位十六进制Span ID
     */
    String generateSpanId();
}
