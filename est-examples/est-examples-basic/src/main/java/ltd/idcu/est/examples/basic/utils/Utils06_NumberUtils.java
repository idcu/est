package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.NumberUtils;

public class Utils06_NumberUtils {
    public static void main(String[] args) {
        System.out.println("=== NumberUtils 示例 ===");
        System.out.println();

        System.out.println("--- 1. 字符串转数字 ---");
        String validStr = "123";
        int num = NumberUtils.toInt(validStr, 0);
        System.out.println("toInt(\"123\", 0): " + num);
        
        String invalidStr = "abc";
        int safeNum = NumberUtils.toInt(invalidStr, 0);
        System.out.println("toInt(\"abc\", 0): " + safeNum);
        
        String longStr = "9999999999";
        long longNum = NumberUtils.toLong(longStr, 0L);
        System.out.println("toLong(\"" + longStr + "\", 0): " + longNum);
        System.out.println();

        System.out.println("--- 2. 验证数字 ---");
        System.out.println("isDigits(\"123\"): " + NumberUtils.isDigits("123"));
        System.out.println("isDigits(\"12.3\"): " + NumberUtils.isDigits("12.3"));
        System.out.println("isNumber(\"12.3\"): " + NumberUtils.isNumber("12.3"));
        System.out.println("isInteger(\"123\"): " + NumberUtils.isInteger("123"));
        System.out.println();

        System.out.println("--- 3. 数学运算 ---");
        int[] scores = {85, 92, 78, 90, 88};
        System.out.println("成绩数组: [85, 92, 78, 90, 88]");
        System.out.println("max: " + NumberUtils.max(scores));
        System.out.println("min: " + NumberUtils.min(scores));
        System.out.println("sum: " + NumberUtils.sum(scores));
        System.out.println();

        System.out.println("--- 4. 四舍五入 ---");
        double pi = 3.1415926;
        System.out.println("pi = " + pi);
        System.out.println("round(pi, 2): " + NumberUtils.round(pi, 2));
        System.out.println("round(pi, 4): " + NumberUtils.round(pi, 4));
        System.out.println();

        System.out.println("--- 5. 范围限制 ---");
        System.out.println("clamp(50, 0, 100): " + NumberUtils.clamp(50, 0, 100));
        System.out.println("clamp(-10, 0, 100): " + NumberUtils.clamp(-10, 0, 100));
        System.out.println("clamp(150, 0, 100): " + NumberUtils.clamp(150, 0, 100));
        System.out.println("isInRange(50, 0, 100): " + NumberUtils.isInRange(50, 0, 100));
    }
}
