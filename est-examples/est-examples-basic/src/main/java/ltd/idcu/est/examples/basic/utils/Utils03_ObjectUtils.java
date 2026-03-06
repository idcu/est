package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.ObjectUtils;
import java.util.ArrayList;
import java.util.List;

public class Utils03_ObjectUtils {
    public static void main(String[] args) {
        System.out.println("=== ObjectUtils 示例 ===");
        System.out.println();

        System.out.println("--- 1. 空值检查 ---");
        System.out.println("isEmpty(null): " + ObjectUtils.isEmpty(null));
        System.out.println("isEmpty(\"\"): " + ObjectUtils.isEmpty(""));
        System.out.println("isEmpty(new Object[0]): " + ObjectUtils.isEmpty(new Object[0]));
        System.out.println("isEmpty(new ArrayList<>()): " + ObjectUtils.isEmpty(new ArrayList<>()));
        System.out.println();

        System.out.println("--- 2. 默认值处理 ---");
        String name = null;
        System.out.println("defaultIfNull(null, \"未知\"): \"" + ObjectUtils.defaultIfNull(name, "未知") + "\"");
        
        String a = null, b = null, c = "EST";
        System.out.println("firstNonNull: \"" + ObjectUtils.firstNonNull(a, b, c) + "\"");
        System.out.println();

        System.out.println("--- 3. 对象比较 ---");
        System.out.println("equals(1, 1): " + ObjectUtils.equals(1, 1));
        System.out.println("equals(new int[]{1,2}, new int[]{1,2}): " + ObjectUtils.equals(new int[]{1,2}, new int[]{1,2}));
        System.out.println("compare(5, 10): " + ObjectUtils.compare(5, 10));
        System.out.println("compare(10, 5): " + ObjectUtils.compare(10, 5));
    }
}
