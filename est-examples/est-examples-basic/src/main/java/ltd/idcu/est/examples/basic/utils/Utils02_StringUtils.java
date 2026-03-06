package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.StringUtils;
import java.util.Arrays;

public class Utils02_StringUtils {
    public static void main(String[] args) {
        System.out.println("=== StringUtils 示例 ===");
        System.out.println();

        System.out.println("--- 1. 空值检查 ---");
        System.out.println("isEmpty(null): " + StringUtils.isEmpty(null));
        System.out.println("isEmpty(\"\"): " + StringUtils.isEmpty(""));
        System.out.println("isEmpty(\"  \"): " + StringUtils.isEmpty("  "));
        System.out.println("isBlank(null): " + StringUtils.isBlank(null));
        System.out.println("isBlank(\"\"): " + StringUtils.isBlank(""));
        System.out.println("isBlank(\"  \"): " + StringUtils.isBlank("  "));
        System.out.println();

        System.out.println("--- 2. 去除空格 ---");
        String str = "   Hello EST   ";
        System.out.println("原始: \"" + str + "\"");
        System.out.println("trim: \"" + StringUtils.trim(str) + "\"");
        System.out.println("trimToNull(\"  \"): " + StringUtils.trimToNull("  "));
        System.out.println("trimToEmpty(null): \"" + StringUtils.trimToEmpty(null) + "\"");
        System.out.println();

        System.out.println("--- 3. 默认值处理 ---");
        System.out.println("defaultIfBlank(null, \"访客\"): \"" + StringUtils.defaultIfBlank(null, "访客") + "\"");
        System.out.println("defaultIfBlank(\"\", \"匿名\"): \"" + StringUtils.defaultIfBlank("", "匿名") + "\"");
        System.out.println();

        System.out.println("--- 4. 字符串比较 ---");
        System.out.println("equals(\"EST\", \"EST\"): " + StringUtils.equals("EST", "EST"));
        System.out.println("equals(null, null): " + StringUtils.equals(null, null));
        System.out.println("equalsIgnoreCase(\"est\", \"EST\"): " + StringUtils.equalsIgnoreCase("est", "EST"));
        System.out.println();

        System.out.println("--- 5. 截取字符串 ---");
        String framework = "ESTFramework";
        System.out.println("left(\"" + framework + "\", 3): \"" + StringUtils.left(framework, 3) + "\"");
        System.out.println("right(\"" + framework + "\", 9): \"" + StringUtils.right(framework, 9) + "\"");
        System.out.println("mid(\"" + framework + "\", 3, 5): \"" + StringUtils.mid(framework, 3, 5) + "\"");
        System.out.println();

        System.out.println("--- 6. 分割和连接 ---");
        String csv = "apple,banana,orange";
        String[] fruits = StringUtils.split(csv, ",");
        System.out.println("split(\"" + csv + "\", \",\"): " + Arrays.toString(fruits));
        
        String[] colors = {"red", "green", "blue"};
        System.out.println("join: \"" + StringUtils.join(colors, " | ") + "\"");
    }
}
