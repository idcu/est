package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.collection.impl.Seqs;

public class Utils07_CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== Utils 与 Collection 集成示例 ===");
        System.out.println();

        System.out.println("--- 1. 结合 StringUtils 使用 ---");
        Seqs.of("  apple  ", "  banana  ", "", "  cherry  ", null)
            .filter(StringUtils::isNotBlank)
            .map(StringUtils::trim)
            .map(StringUtils::capitalize)
            .forEach(fruit -> System.out.println("  - " + fruit));
        System.out.println();

        System.out.println("--- 2. 数据清理流水线 ---");
        String rawData = "  EST,Framework,,Utils,,Collection  ";
        Seqs.of(StringUtils.split(rawData, ","))
            .map(StringUtils::trim)
            .filter(StringUtils::isNotBlank)
            .forEach(item -> System.out.println("  - " + item));
        System.out.println();

        System.out.println("--- 3. 实际应用场景 ---");
        Seqs.of("user@example.com", "  invalid-email  ", "", "admin@est.com", null)
            .filter(StringUtils::isNotBlank)
            .map(StringUtils::trim)
            .filter(email -> StringUtils.contains(email, "@"))
            .forEach(validEmail -> System.out.println("  ✓ " + validEmail));
    }
}
