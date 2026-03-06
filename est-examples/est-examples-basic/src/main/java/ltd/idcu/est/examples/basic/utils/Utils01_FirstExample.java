package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.StringUtils;

public class Utils01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Utils Common 5分钟上手 ===");
        System.out.println();

        String name = "  EST Framework  ";
        System.out.println("原始字符串: \"" + name + "\"");
        
        System.out.println("是否为空: " + StringUtils.isEmpty(name));
        
        String trimmed = StringUtils.trim(name);
        System.out.println("去除空格: \"" + trimmed + "\"");
        
        String capitalized = StringUtils.capitalize(trimmed);
        System.out.println("首字母大写: \"" + capitalized + "\"");
        
        System.out.println();
        System.out.println("恭喜！你已经学会使用 EST Utils 了！ 🎉");
    }
}
