package ltd.idcu.est.examples.basic;

import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.utils.common.DateUtils;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.ClassUtils;

public class UtilsExample {
    public static void run() {
        System.out.println("\n=== Utils Example ===");
        
        // 字符串处理示例
        stringUtilsExample();
        
        // 日期处理示例
        dateUtilsExample();
        
        // 断言工具示例
        assertUtilsExample();
        
        // 类处理示例
        classUtilsExample();
    }
    
    private static void stringUtilsExample() {
        System.out.println("\n1. StringUtils Example:");
        
        String text = "Hello EST Framework";
        
        System.out.println("Original text: " + text);
        System.out.println("Uppercase: " + StringUtils.toUpperCase(text));
        System.out.println("Lowercase: " + StringUtils.toLowerCase(text));
        System.out.println("Is empty: " + StringUtils.isEmpty(""));
        System.out.println("Is not empty: " + StringUtils.isNotEmpty(text));
    }
    
    private static void dateUtilsExample() {
        System.out.println("\n2. DateUtils Example:");
        
        long currentTime = System.currentTimeMillis();
        
        System.out.println("Current timestamp: " + currentTime);
        System.out.println("Current date: " + DateUtils.format(currentTime));
        System.out.println("Formatted date: " + DateUtils.format(currentTime, "yyyy-MM-dd HH:mm:ss"));
    }
    
    private static void assertUtilsExample() {
        System.out.println("\n3. AssertUtils Example:");
        
        try {
            AssertUtils.notNull("value", "Value cannot be null");
            System.out.println("Assertion passed: not null");
            
            AssertUtils.isTrue(5 > 3, "5 should be greater than 3");
            System.out.println("Assertion passed: 5 > 3");
        } catch (Exception e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }
    }
    
    private static void classUtilsExample() {
        System.out.println("\n4. ClassUtils Example:");
        
        Class<?> clazz = String.class;
        
        System.out.println("Class name: " + ClassUtils.getClassName(clazz));
        System.out.println("Simple class name: " + ClassUtils.getSimpleClassName(clazz));
        System.out.println("Is primitive: " + ClassUtils.isPrimitive(clazz));
    }
}