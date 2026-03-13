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
        logger.info("=== Annotation-Driven MCP Tools Example");
        logger.info("==================================");
        
        try {
            DefaultMcpServer server = new DefaultMcpServer("Annotation Server", "1.0.0");
            
            AnnotationMcpToolRegistry registry = new AnnotationMcpToolRegistry(server);
            
            logger.info("\n--- Registering annotation-driven tool classes");
            registry.registerTools(
                new WeatherTools(),
                new CalculatorTools(),
                new StringTools()
            );
            
            logger.info("\n--- Testing annotation-driven tools");
            
            testWeatherTools(server);
            testCalculatorTools(server);
            testStringTools(server);
            
        } catch (Exception e) {
            logger.error("Example execution error", e);
        }
        
        logger.info("\n=== Annotation-Driven MCP Example Complete");
    }
    
    @McpTool(name = "WeatherTools", description = "Weather-related tools")
    public static class WeatherTools {
        
        @McpToolMethod(name = "getWeather", description = "Get weather for specified city")
        public String getWeather(
            @McpParam(name = "city", description = "City name", required = true) String city
        ) {
            Map<String, String> weatherData = new HashMap<>();
            weatherData.put("Beijing", "Sunny, 25°C, Humidity 60%");
            weatherData.put("Shanghai", "Cloudy, 22°C, Humidity 70%");
            weatherData.put("Guangzhou", "Light rain, 28°C, Humidity 85%");
            weatherData.put("Shenzhen", "Sunny, 30°C, Humidity 75%");
            
            return city + " weather: " + weatherData.getOrDefault(city, "Unknown city");
        }
        
        @McpToolMethod(name = "getTemperature", description = "Get temperature for specified city")
        public int getTemperature(
            @McpParam(name = "city", description = "City name", required = true) String city
        ) {
            Map<String, Integer> tempData = new HashMap<>();
            tempData.put("Beijing", 25);
            tempData.put("Shanghai", 22);
            tempData.put("Guangzhou", 28);
            tempData.put("Shenzhen", 30);
            
            return tempData.getOrDefault(city, 20);
        }
    }
    
    @McpTool(name = "CalculatorTools", description = "Calculator tools")
    public static class CalculatorTools {
        
        @McpToolMethod(name = "add", description = "Addition operation")
        public double add(
            @McpParam(name = "a", description = "First number", required = true) double a,
            @McpParam(name = "b", description = "Second number", required = true) double b
        ) {
            return a + b;
        }
        
        @McpToolMethod(name = "subtract", description = "Subtraction operation")
        public double subtract(
            @McpParam(name = "a", description = "Minuend", required = true) double a,
            @McpParam(name = "b", description = "Subtrahend", required = true) double b
        ) {
            return a - b;
        }
        
        @McpToolMethod(name = "multiply", description = "Multiplication operation")
        public double multiply(
            @McpParam(name = "a", description = "First number", required = true) double a,
            @McpParam(name = "b", description = "Second number", required = true) double b
        ) {
            return a * b;
        }
        
        @McpToolMethod(name = "divide", description = "Division operation")
        public double divide(
            @McpParam(name = "a", description = "Dividend", required = true) double a,
            @McpParam(name = "b", description = "Divisor", required = true) double b
        ) {
            if (b == 0) {
                throw new IllegalArgumentException("Divisor cannot be zero");
            }
            return a / b;
        }
    }
    
    @McpTool(name = "StringTools", description = "String processing tools")
    public static class StringTools {
        
        @McpToolMethod(name = "toUpperCase", description = "Convert string to uppercase")
        public String toUpperCase(
            @McpParam(name = "text", description = "Input text", required = true) String text
        ) {
            return text.toUpperCase();
        }
        
        @McpToolMethod(name = "toLowerCase", description = "Convert string to lowercase")
        public String toLowerCase(
            @McpParam(name = "text", description = "Input text", required = true) String text
        ) {
            return text.toLowerCase();
        }
        
        @McpToolMethod(name = "reverse", description = "Reverse string")
        public String reverse(
            @McpParam(name = "text", description = "Input text", required = true) String text
        ) {
            return new StringBuilder(text).reverse().toString();
        }
        
        @McpToolMethod(name = "countLength", description = "Calculate string length")
        public int countLength(
            @McpParam(name = "text", description = "Input text", required = true) String text
        ) {
            return text.length();
        }
    }
    
    private static void testWeatherTools(DefaultMcpServer server) {
        logger.info("\nTesting weather tools...");
        
        Map<String, Object> args = new HashMap<>();
        args.put("city", "Beijing");
        McpToolResult result = server.callTool("WeatherTools_getWeather", args);
        logger.info("  WeatherTools_getWeather: {}", result.getContent().get(0).getValue());
        
        Map<String, Object> tempArgs = new HashMap<>();
        tempArgs.put("city", "Shanghai");
        McpToolResult tempResult = server.callTool("WeatherTools_getTemperature", tempArgs);
        logger.info("  WeatherTools_getTemperature: {}", tempResult.getContent().get(0).getValue());
    }
    
    private static void testCalculatorTools(DefaultMcpServer server) {
        logger.info("\nTesting calculator tools...");
        
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
        logger.info("\nTesting string tools...");
        
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
