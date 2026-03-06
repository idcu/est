package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class Collection01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 第一个示例 ===");
        
        // 1. 创建一个序列（序列就是一组数据）
        Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子", "葡萄", "西瓜");
        
        System.out.println("原始水果列表：" + fruits.toList());
        
        // 2. 做一些操作
        Seq<String> result = fruits
            .filter(fruit -> fruit.length() > 2)  // 筛选名字长度大于2的
            .sorted()                               // 排序
            .take(3);                               // 只取前3个
        
        // 3. 输出结果
        System.out.println("筛选结果：" + result.toList());
        
        System.out.println("\n恭喜！你已经会用 EST Collection 了！🎉");
    }
}
