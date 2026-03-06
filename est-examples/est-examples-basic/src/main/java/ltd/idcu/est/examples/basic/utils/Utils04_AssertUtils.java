package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.AssertUtils;

public class Utils04_AssertUtils {
    public static void main(String[] args) {
        System.out.println("=== AssertUtils 示例 ===");
        System.out.println();

        System.out.println("--- 1. 空值检查 ---");
        String name = "EST";
        AssertUtils.notNull(name, "名称不能为空");
        System.out.println("✓ notNull 检查通过");
        
        String nickname = "  EST  ";
        AssertUtils.hasText(nickname, "昵称不能为空");
        System.out.println("✓ hasText 检查通过");
        System.out.println();

        System.out.println("--- 2. 布尔检查 ---");
        int age = 18;
        AssertUtils.isTrue(age >= 18, "必须年满18岁");
        System.out.println("✓ isTrue 检查通过");
        
        AssertUtils.isFalse(age < 0, "年龄不能为负数");
        System.out.println("✓ isFalse 检查通过");
        System.out.println();

        System.out.println("--- 3. 数值检查 ---");
        int count = 10;
        AssertUtils.isPositive(count, "数量必须大于0");
        System.out.println("✓ isPositive 检查通过");
        
        int score = 85;
        AssertUtils.inRange(score, 0, 100, "分数必须在0-100之间");
        System.out.println("✓ inRange 检查通过");
        
        System.out.println();
        System.out.println("所有断言检查通过！");
    }
}
