package ltd.idcu.est.examples.ai;

import ltd.idcu.est.mcp.api.McpToolResult;
import ltd.idcu.est.mcp.api.annotation.*;
import ltd.idcu.est.mcp.server.AnnotationMcpToolRegistry;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AnnotationDrivenMcpExample {
    
    private static final Logger logger = LoggerFactory.getLogger(AnnotationDrivenMcpExample.class);
    
    public static void main(String[] args) {
        logger.info("=== 注解驱动的 MCP 工具示例");
        logger.info("==================================");
        
        try {
            DefaultMcpServer server = new DefaultMcpServer("Annotation Server", "1.0.0");
            
            AnnotationMcpToolRegistry registry = new AnnotationMcpToolRegistry(server);
            
            logger.info("\n--- 注册注解驱动的工具类");
            registry.registerTools(
                new WeatherTools(),
                new CalculatorTools(),
                new StringTools()
            );
            
            logger.info("\n--- 测试注解驱动的工具");
            
            testWeatherTools(server);
            testCalculatorTools(server);
            testStringTools(server);
            
        } catch (Exception e) {
            logger.error("示例执行出错", e);
        }
        
        logger.info("\n=== 注解驱动 MCP 示例完成");
    }
    
    @McpTool(name = "WeatherTools", description = "天气相关工具")
    public static class WeatherTools {
        
        @McpToolMethod(name = "getWeather", description = "获取指定城市的天气")
        public String getWeather(
            @McpParam(name = "city", description = "城市名称", required = true) String city
        ) {
            Map<String, String> weatherData = new HashMap<>();
            weatherData.put("北京", "晴朗，25°C，湿度 60%");
            weatherData.put("上海", "多云，22°C，湿度 70%");
            weatherData.put("广州", "小雨，28°C，湿度 85%");
            weatherData.put("深圳", "晴朗，30°C，湿度 75%");
            
            return city + " 的天气：" + weatherData.getOrDefault(city, "未知城市");
        }
        
        @McpToolMethod(name = "getTemperature", description = "获取指定城市的温度")
        public int getTemperature(
            @McpParam(name = "city", description = "城市名称", required = true) String city
        ) {
            Map<String, Integer> tempData = new HashMap<>();
            tempData.put("北京", 25);
            tempData.put("上海", 22);
            tempData.put("广州", 28);
            tempData.put("深圳", 30);
            
            return tempData.getOrDefault(city, 20);
        }
    }
    
    @McpTool(name = "CalculatorTools", description = "计算器工具")
    public static class CalculatorTools {
        
        @McpToolMethod(name = "add", description = "加法运算")
        public double add(
            @McpParam(name = "a", description = "第一个数", required = true) double a,
            @McpParam(name = "b", description = "第二个数", required = true) double b
        ) {
            return a + b;
        }
        
        @McpToolMethod(name = "subtract", description = "减法运算")
        public double subtract(
            @McpParam(name = "a", description = "被减数", required = true) double a,
            @McpParam(name = "b", description = "减数", required = true) double b
        ) {
            return a - b;
        }
        
        @McpToolMethod(name = "multiply", description = "乘法运算")
        public double multiply(
            @McpParam(name = "a", description = "第一个数", required = true) double a,
            @McpParam(name = "b", description = "第二个数", required = true) double b
        ) {
            return a * b;
        }
        
        @McpToolMethod(name = "divide", description = "除法运算")
        public double divide(
            @McpParam(name = "a", description = "被除数", required = true) double a,
            @McpParam(name = "b", description = "除数", required = true) double b
        ) {
            if (b == 0) {
                throw new IllegalArgumentException("除数不能为零");
            }
            return a / b;
        }
    }
    
    @McpTool(name = "StringTools", description = "字符串处理工具")
    public static class StringTools {
        
        @McpToolMethod(name = "toUpperCase", description = "字符串转大写")
        public String toUpperCase(
            @McpParam(name = "text", description = "输入文本", required = true) String text
        ) {
            return text.toUpperCase();
        }
        
        @McpToolMethod(name = "toLowerCase", description = "字符串转小写")
        public String toLowerCase(
            @McpParam(name = "text", description = "输入文本", required = true) String text
        ) {
            return text.toLowerCase();
        }
        
        @McpToolMethod(name = "reverse", description = "字符串反转")
        public String reverse(
            @McpParam(name = "text", description = "输入文本", required = true) String text
        ) {
            return new StringBuilder(text).reverse().toString();
        }
        
        @McpToolMethod(name = "countLength", description = "计算字符串长度")
        public int countLength(
            @McpParam(name = "text", description = "输入文本", required = true) String text
        ) {
            return text.length();
        }
    }
    
    private static void testWeatherTools(DefaultMcpServer server) {
        logger.info("\n测试天气工具...");
        
        Map<String, Object> args = new HashMap<>();
        args.put("city", "北京");
        McpToolResult result = server.callTool("WeatherTools_getWeather", args);
        logger.info("  WeatherTools_getWeather: {}", result.getContent().get(0).getValue());
        
        Map<String, Object> tempArgs = new HashMap<>();
        tempArgs.put("city", "上海");
        McpToolResult tempResult = server.callTool("WeatherTools_getTemperature", tempArgs);
        logger.info("  WeatherTools_getTemperature: {}", tempResult.getContent().get(0).getValue());
    }
    
    private static void testCalculatorTools(DefaultMcpServer server) {
        logger.info("\n测试计算器工具...");
        
        Map<String, Object> addArgs = new HashMap<>();
        addArgs.put("a", 10.0);
        addArgs.put("b", 25.0);
        McpToolResult addResult = server.callTool("CalculatorTools_add", addArgs);
        logger.info("  CalculatorTools_add: 10 + 25 = {}", addResult.getContent().get(0).getValue());
        
        Map<String, Object> multiplyArgs = new HashMap<>();
        multiplyArgs.put("a", 5.0);
        multiplyArgs.put("b", 6.0);
        McpToolResult multiplyResult = server.callTool("CalculatorTools_multiply", multiplyArgs);
        logger.info("  CalculatorTools_multiply: 5 * 6 = {}", multiplyResult.getContent().get(0).getValue());
    }
    
    private static void testStringTools(DefaultMcpServer server) {
        logger.info("\n测试字符串工具...");
        
        Map<String, Object> upperArgs = new HashMap<>();
        upperArgs.put("text", "hello world");
        McpToolResult upperResult = server.callTool("StringTools_toUpperCase", upperArgs);
        logger.info("  StringTools_toUpperCase: {}", upperResult.getContent().get(0).getValue());
        
        Map<String, Object> reverseArgs = new HashMap<>();
        reverseArgs.put("text", "EST Framework");
        McpToolResult reverseResult = server.callTool("StringTools_reverse", reverseArgs);
        logger.info("  StringTools_reverse: {}", reverseResult.getContent().get(0).getValue());
        
        Map<String, Object> lengthArgs = new HashMap<>();
        lengthArgs.put("text", "EST AI Suite");
        McpToolResult lengthResult = server.callTool("StringTools_countLength", lengthArgs);
        logger.info("  StringTools_countLength: {}", lengthResult.getContent().get(0).getValue());
    }
}
