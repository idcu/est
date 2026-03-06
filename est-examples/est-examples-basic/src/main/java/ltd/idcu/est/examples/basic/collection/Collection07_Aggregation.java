package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

import java.util.List;
import java.util.Map;

public class Collection07_Aggregation {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 聚合操作 ===\n");
        
        // reduce/fold 累积操作
        System.out.println("--- reduce/fold 累积操作 ---");
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        System.out.println("原始数字：" + numbers.toList());
        
        Integer sum = numbers.reduce(0, (a, b) -> a + b);
        System.out.println("求和：" + sum);
        
        Integer sum2 = numbers.fold(0, (a, b) -> a + b);
        System.out.println("求和（fold）：" + sum2);
        
        Integer product = numbers.reduce(1, (a, b) -> a * b);
        System.out.println("乘积：" + product);
        
        Integer max = numbers.reduce(Integer::max).orElse(null);
        System.out.println("最大值：" + max);
        
        // groupBy 分组
        System.out.println("\n--- groupBy 分组 ---");
        Seq<User> users = Seqs.of(
            new User("张三", "zhangsan@example.com", 25, "北京", true),
            new User("李四", "lisi@example.com", 18, "上海", true),
            new User("王五", "wangwu@example.com", 30, "北京", false),
            new User("赵六", "zhaoliu@example.com", 22, "上海", true),
            new User("钱七", "qianqi@example.com", 28, "广州", true)
        );
        
        Map<String, Seq<User>> byCity = users.groupBy(User::getCity);
        System.out.println("按城市分组：");
        byCity.forEach((city, cityUsers) -> {
            System.out.println("  " + city + ": " + cityUsers.pluck(User::getName).toList());
        });
        
        // countBy 计数
        System.out.println("\n--- countBy 计数 ---");
        Map<String, Long> countByCity = users.countBy(User::getCity);
        System.out.println("各城市人数：" + countByCity);
        
        // keyBy 转成 Map
        System.out.println("\n--- keyBy 转成 Map ---");
        Map<String, User> byEmail = users.keyBy(User::getEmail);
        System.out.println("按邮箱索引的用户：");
        byEmail.forEach((email, user) -> {
            System.out.println("  " + email + " -> " + user.getName());
        });
        
        // partition 分区
        System.out.println("\n--- partition 分区 ---");
        Pair<Seq<Integer>, Seq<Integer>> partitioned = numbers.partition(n -> n % 2 == 0);
        System.out.println("偶数：" + partitioned.getFirst().toList());
        System.out.println("奇数：" + partitioned.getSecond().toList());
        
        // chunked 分块
        System.out.println("\n--- chunked 分块 ---");
        Seq<Integer> moreNumbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Seq<List<Integer>> chunks = moreNumbers.chunked(3);
        System.out.println("每3个分一块：");
        chunks.forEach(chunk -> System.out.println("  " + chunk));
        
        // windowed 滑动窗口
        System.out.println("\n--- windowed 滑动窗口 ---");
        Seq<List<Integer>> windows = moreNumbers.windowed(3);
        System.out.println("滑动窗口（大小3）：");
        windows.forEach(window -> System.out.println("  " + window));
    }
}
