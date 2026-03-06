package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class Collection03_BasicOperations {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 基本操作 ===\n");
        
        Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子", "葡萄", "西瓜");
        
        System.out.println("原始水果列表：" + fruits.toList());
        
        // 获取元素个数
        System.out.println("\n--- 基本信息 ---");
        System.out.println("元素个数：" + fruits.size());
        System.out.println("是否为空：" + fruits.isEmpty());
        System.out.println("是否不为空：" + fruits.isNotEmpty());
        System.out.println("是否包含'苹果'：" + fruits.contains("苹果"));
        System.out.println("是否包含'葡萄'：" + fruits.contains("葡萄"));
        
        // 获取元素
        System.out.println("\n--- 获取元素 ---");
        System.out.println("第一个元素：" + fruits.firstOrNull());
        System.out.println("最后一个元素：" + fruits.lastOrNull());
        System.out.println("索引为1的元素：" + fruits.get(1));
        System.out.println("索引为4的元素：" + fruits.elementAt(4));
        System.out.println("索引为10的元素（安全获取）：" + fruits.elementAtOrNull(10));
    }
}
