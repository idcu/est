package ltd.idcu.est.examples.basic;

import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.utils.common.DateUtils;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.ClassUtils;

import java.time.LocalDateTime;

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
        System.out.println("Uppercase: " + StringUtils.upperCase(text));
        System.out.println("Lowercase: " + StringUtils.lowerCase(text));
        System.out.println("Is empty: " + StringUtils.isEmpty(""));
        System.out.println("Is not empty: " + StringUtils.isNotEmpty(text));
        System.out.println("Is blank: " + StringUtils.isBlank("   "));
        System.out.println("Capitalize: " + StringUtils.capitalize("hello"));
    }
    
    private static void dateUtilsExample() {
        System.out.println("\n2. DateUtils Example:");
        
        LocalDateTime now = DateUtils.now();
        long currentTime = DateUtils.currentMillis();
        
        System.out.println("Current datetime: " + DateUtils.format(now));
        System.out.println("Current timestamp: " + currentTime);
        System.out.println("Formatted date: " + DateUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("Compact format: " + DateUtils.formatCompact(now));
        System.out.println("Is today: " + DateUtils.isToday(now));
    }
    
    private static void assertUtilsExample() {
        System.out.println("\n3. AssertUtils Example:");
        
        try {
            AssertUtils.notNull("value", "Value cannot be null");
            System.out.println("Assertion passed: not null");
            
            AssertUtils.isTrue(5 > 3, "5 should be greater than 3");
            System.out.println("Assertion passed: 5 > 3");
            
            AssertUtils.hasText("hello", "Text should not be blank");
            System.out.println("Assertion passed: has text");
        } catch (Exception e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }
    }
    
    private static void classUtilsExample() {
        System.out.println("\n4. ClassUtils Example:");
        
        Class<?> clazz = String.class;
        
        System.out.println("Class name: " + ClassUtils.getQualifiedName(clazz));
        System.out.println("Simple class name: " + ClassUtils.getShortName(clazz));
        System.out.println("Is primitive or wrapper: " + ClassUtils.isPrimitiveOrWrapper(clazz));
        System.out.println("Package name: " + ClassUtils.getPackageName(clazz));
    }
}
