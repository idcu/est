package ltd.idcu.est.examples.basic.utils;

import ltd.idcu.est.utils.common.ArrayUtils;
import java.util.Arrays;

public class Utils05_ArrayUtils {
    public static void main(String[] args) {
        System.out.println("=== ArrayUtils 示例 ===");
        System.out.println();

        String[] fruits = {"apple", "banana", "orange"};
        System.out.println("原始数组: " + Arrays.toString(fruits));
        System.out.println();

        System.out.println("--- 1. 基本检查 ---");
        System.out.println("isEmpty: " + ArrayUtils.isEmpty(fruits));
        System.out.println("contains(\"banana\"): " + ArrayUtils.contains(fruits, "banana"));
        System.out.println("indexOf(\"orange\"): " + ArrayUtils.indexOf(fruits, "orange"));
        System.out.println();

        System.out.println("--- 2. 添加元素 ---");
        String[] moreFruits = ArrayUtils.add(fruits, "grape");
        System.out.println("添加后: " + Arrays.toString(moreFruits));
        
        String[] insertFruits = ArrayUtils.add(moreFruits, 1, "mango");
        System.out.println("插入后: " + Arrays.toString(insertFruits));
        System.out.println();

        System.out.println("--- 3. 删除元素 ---");
        String[] lessFruits = ArrayUtils.remove(insertFruits, 2);
        System.out.println("删除索引2后: " + Arrays.toString(lessFruits));
        
        String[] withoutBanana = ArrayUtils.removeElement(lessFruits, "banana");
        System.out.println("删除banana后: " + Arrays.toString(withoutBanana));
        System.out.println();

        System.out.println("--- 4. 数组操作 ---");
        String[] reversed = ArrayUtils.reverse(fruits);
        System.out.println("反转: " + Arrays.toString(reversed));
        
        String[] unique = ArrayUtils.unique(new String[]{"apple", "banana", "apple", "orange"});
        System.out.println("去重: " + Arrays.toString(unique));
        
        System.out.println("first: " + ArrayUtils.first(fruits));
        System.out.println("last: " + ArrayUtils.last(fruits));
    }
}
