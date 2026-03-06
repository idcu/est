package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Collection02_CreateSeq {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 创建序列的 7 种方法 ===\n");
        
        // 方法1：直接创建（最常用）
        System.out.println("--- 方法1：直接创建 ---");
        Seq<String> seq1 = Seqs.of("a", "b", "c");
        Seq<Integer> seq2 = Seqs.of(1, 2, 3, 4, 5);
        System.out.println("seq1: " + seq1.toList());
        System.out.println("seq2: " + seq2.toList());
        
        // 方法2：从 List 创建
        System.out.println("\n--- 方法2：从 List 创建 ---");
        List<String> list = Arrays.asList("x", "y", "z");
        Seq<String> seq3 = Seqs.from(list);
        System.out.println("seq3: " + seq3.toList());
        
        // 方法3：创建空序列
        System.out.println("\n--- 方法3：创建空序列 ---");
        Seq<String> emptySeq = Seqs.empty();
        System.out.println("emptySeq 为空吗？" + emptySeq.isEmpty());
        
        // 方法4：创建数字范围
        System.out.println("\n--- 方法4：创建数字范围 ---");
        Seq<Integer> range1 = Seqs.range(1, 10);
        Seq<Integer> range2 = Seqs.range(1, 10, 2);
        System.out.println("range1 (1-10): " + range1.toList());
        System.out.println("range2 (1-10, 步长2): " + range2.toList());
        
        // 方法5：生成序列
        System.out.println("\n--- 方法5：生成序列 ---");
        Seq<Double> randoms = Seqs.generate(5, Math::random);
        System.out.println("5个随机数: " + randoms.toList());
        
        Random random = new Random();
        Seq<Integer> randomInts = Seqs.generate(10, () -> random.nextInt(100));
        System.out.println("10个0-100的随机整数: " + randomInts.toList());
        
        // 方法6：重复元素
        System.out.println("\n--- 方法6：重复元素 ---");
        Seq<String> repeated = Seqs.repeat("hello", 5);
        System.out.println("重复5次'hello': " + repeated.toList());
        
        // 方法7：从 Stream 创建
        System.out.println("\n--- 方法7：从 Stream 创建 ---");
        Stream<String> stream = Stream.of("a", "b", "c");
        Seq<String> seq4 = Seqs.from(stream);
        System.out.println("从Stream创建: " + seq4.toList());
    }
}
