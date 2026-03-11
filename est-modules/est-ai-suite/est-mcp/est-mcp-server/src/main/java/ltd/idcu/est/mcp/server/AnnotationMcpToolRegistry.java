package ltd.idcu.est.mcp.server;

import ltd.idcu.est.mcp.api.McpTool;
import ltd.idcu.est.mcp.api.McpToolResult;
import ltd.idcu.est.mcp.api.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class AnnotationMcpToolRegistry {
    
    private static final Logger logger = LoggerFactory.getLogger(AnnotationMcpToolRegistry.class);
    
    private final DefaultMcpServer server;
    
    public AnnotationMcpToolRegistry(DefaultMcpServer server) {
        this.server = server;
    }
    
    public void registerTools(Object... toolInstances) {
        for (Object instance : toolInstances) {
            registerTool(instance);
        }
    }
    
    public void registerTool(Object toolInstance) {
        Class<?> toolClass = toolInstance.getClass();
        
        if (!toolClass.isAnnotationPresent(McpTool.class)) {
            logger.warn("类 {} 没有 @McpTool 注解，跳过注册", toolClass.getName());
            return;
        }
        
        McpTool classAnnotation = toolClass.getAnnotation(McpTool.class);
        String toolName = classAnnotation.name().isEmpty() ? 
            toolClass.getSimpleName() : classAnnotation.name();
        String toolDescription = classAnnotation.description();
        
        logger.info("注册 MCP 工具类: {} (名称: {})", toolClass.getName(), toolName);
        
        Method[] methods = toolClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(McpToolMethod.class)) {
                registerMethodAsTool(toolInstance, method, toolName);
            }
        }
    }
    
    private void registerMethodAsTool(Object instance, Method method, String classToolName) {
        McpToolMethod methodAnnotation = method.getAnnotation(McpToolMethod.class);
        
        String methodName = methodAnnotation.name().isEmpty() ? 
            method.getName() : methodAnnotation.name();
        String fullToolName = classToolName + "_" + methodName;
        String description = methodAnnotation.description();
        
        logger.info("  注册方法: {} -> 工具: {}", method.getName(), fullToolName);
        
        McpTool tool = new McpTool(fullToolName, description);
        
        Parameter[] parameters = method.getParameters();
        for (Parameter param : parameters) {
            if (param.isAnnotationPresent(McpParam.class)) {
                McpParam paramAnnotation = param.getAnnotation(McpParam.class);
                String paramName = paramAnnotation.name().isEmpty() ? 
                    param.getName() : paramAnnotation.name();
                String paramDesc = paramAnnotation.description();
                boolean required = paramAnnotation.required();
                
                logger.info("    参数: {} (描述: {}, 必需: {})", paramName, paramDesc, required);
            }
        }
        
        Function<Map<String, Object>, McpToolResult> handler = arguments -> {
            try {
                Object[] args = prepareArguments(method, arguments);
                Object result = method.invoke(instance, args);
                
                McpToolResult toolResult = new McpToolResult(true);
                if (result != null) {
                    toolResult.setContent(Arrays.asList(
                        new McpToolResult.Content("text", String.valueOf(result))
                    ));
                }
                return toolResult;
            } catch (Exception e) {
                logger.error("执行工具 {} 时出错", fullToolName, e);
                McpToolResult toolResult = new McpToolResult(false);
                toolResult.setContent(Arrays.asList(
                    new McpToolResult.Content("text", "执行错误: " + e.getMessage())
                ));
                return toolResult;
            }
        };
        
        server.registerTool(tool, handler);
    }
    
    private Object[] prepareArguments(Method method, Map<String, Object> arguments) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName;
            
            if (param.isAnnotationPresent(McpParam.class)) {
                McpParam paramAnnotation = param.getAnnotation(McpParam.class);
                paramName = paramAnnotation.name().isEmpty() ? param.getName() : paramAnnotation.name();
            } else {
                paramName = param.getName();
            }
            
            Object value = arguments.get(paramName);
            args[i] = convertValue(value, param.getType());
        }
        
        return args;
    }
    
    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            if (targetType.isPrimitive()) {
                if (targetType == boolean.class) return false;
                if (targetType == char.class) return '\0';
                if (targetType == byte.class) return (byte) 0;
                if (targetType == short.class) return (short) 0;
                if (targetType == int.class) return 0;
                if (targetType == long.class) return 0L;
                if (targetType == float.class) return 0.0f;
                if (targetType == double.class) return 0.0;
            }
            return null;
        }
        
        if (targetType.isInstance(value)) {
            return value;
        }
        
        if (targetType == String.class) {
            return String.valueOf(value);
        }
        
        if (targetType == int.class || targetType == Integer.class) {
            return ((Number) value).intValue();
        }
        
        if (targetType == long.class || targetType == Long.class) {
            return ((Number) value).longValue();
        }
        
        if (targetType == double.class || targetType == Double.class) {
            return ((Number) value).doubleValue();
        }
        
        if (targetType == float.class || targetType == Float.class) {
            return ((Number) value).floatValue();
        }
        
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Boolean) return value;
            return Boolean.parseBoolean(String.valueOf(value));
        }
        
        return value;
    }
}
