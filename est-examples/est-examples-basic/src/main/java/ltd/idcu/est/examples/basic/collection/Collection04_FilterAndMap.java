package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class Collection04_FilterAndMap {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 筛选与映射 ===\n");
        
        // 筛选操作
        System.out.println("--- 筛选操作 ---");
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("原始数字：" + numbers.toList());
        
        Seq<Integer> evenNumbers = numbers.where(n -> n % 2 == 0);
        System.out.println("偶数：" + evenNumbers.toList());
        
        Seq<Integer> oddNumbers = numbers.whereNot(n -> n % 2 == 0);
        System.out.println("奇数：" + oddNumbers.toList());
        
        Seq<Integer> greaterThan5 = numbers.where(n -> n > 5);
        System.out.println("大于5的数：" + greaterThan5.toList());
        
        // 去重
        System.out.println("\n--- 去重 ---");
        Seq<String> words = Seqs.of("a", "b", "a", "c", "b", "d");
        System.out.println("原始：" + words.toList());
        Seq<String> uniqueWords = words.distinct();
        System.out.println("去重后：" + uniqueWords.toList());
        
        // 映射操作
        System.out.println("\n--- 映射操作 ---");
        Seq<Integer> doubled = numbers.map(n -> n * 2);
        System.out.println("每个数乘以2：" + doubled.toList());
        
        Seq<String> asStrings = numbers.map(n -> "数字：" + n);
        System.out.println("转成字符串：" + asStrings.toList());
        
        Seq<String> indexed = words.mapIndexed((index, word) -> index + ": " + word);
        System.out.println("带索引：" + indexed.toList());
    }
}
