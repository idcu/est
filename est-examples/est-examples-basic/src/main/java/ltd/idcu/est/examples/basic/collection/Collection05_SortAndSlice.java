package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class Collection05_SortAndSlice {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 排序与截取 ===\n");
        
        // 排序操作
        System.out.println("--- 排序操作 ---");
        Seq<Integer> numbers = Seqs.of(5, 2, 8, 1, 9);
        System.out.println("原始数字：" + numbers.toList());
        
        Seq<Integer> sorted = numbers.sorted();
        System.out.println("排序后：" + sorted.toList());
        
        Seq<Integer> reversed = numbers.reversed();
        System.out.println("反转后：" + reversed.toList());
        
        Seq<Integer> shuffled = numbers.shuffled();
        System.out.println("随机打乱：" + shuffled.toList());
        
        // 截取操作
        System.out.println("\n--- 截取操作 ---");
        Seq<Integer> moreNumbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("原始数字：" + moreNumbers.toList());
        
        Seq<Integer> first3 = moreNumbers.take(3);
        System.out.println("取前3个：" + first3.toList());
        
        Seq<Integer> lessThan5 = moreNumbers.takeWhile(n -> n < 5);
        System.out.println("取小于5的数：" + lessThan5.toList());
        
        Seq<Integer> last3 = moreNumbers.takeLast(3);
        System.out.println("取后3个：" + last3.toList());
        
        Seq<Integer> dropped3 = moreNumbers.drop(3);
        System.out.println("跳过前3个：" + dropped3.toList());
        
        Seq<Integer> slice = moreNumbers.slice(2, 5);
        System.out.println("切片(2-5)：" + slice.toList());
    }
}
