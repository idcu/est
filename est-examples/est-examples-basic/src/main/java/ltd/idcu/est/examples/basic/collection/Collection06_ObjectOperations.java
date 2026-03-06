package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

class User {
    private String name;
    private String email;
    private int age;
    private String city;
    private boolean active;
    
    public User(String name, String email, int age, String city, boolean active) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.city = city;
        this.active = active;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    public boolean isActive() { return active; }
    
    @Override
    public String toString() {
        return name + "(" + age + "岁, " + city + ")";
    }
}

public class Collection06_ObjectOperations {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 对象操作 ===\n");
        
        Seq<User> users = Seqs.of(
            new User("张三", "zhangsan@example.com", 25, "北京", true),
            new User("李四", "lisi@example.com", 18, "上海", true),
            new User("王五", "wangwu@example.com", 30, "北京", false),
            new User("赵六", "zhaoliu@example.com", 22, "上海", true),
            new User("钱七", "qianqi@example.com", 28, "广州", true)
        );
        
        System.out.println("所有用户：" + users.toList());
        
        // 筛选活跃用户
        System.out.println("\n--- 筛选活跃用户 ---");
        Seq<User> activeUsers = users.where(User::isActive);
        System.out.println("活跃用户：" + activeUsers.toList());
        
        // 筛选成年人
        System.out.println("\n--- 筛选成年人（年龄>=18） ---");
        Seq<User> adults = users.where(u -> u.getAge() >= 18);
        System.out.println("成年人：" + adults.toList());
        
        // 按年龄排序
        System.out.println("\n--- 按年龄从小到大排序 ---");
        Seq<User> sortedByAge = users.sortBy(User::getAge);
        System.out.println("排序后：" + sortedByAge.toList());
        
        // 按年龄降序
        System.out.println("\n--- 按年龄从大到小排序 ---");
        Seq<User> sortedByAgeDesc = users.sortByDesc(User::getAge);
        System.out.println("排序后：" + sortedByAgeDesc.toList());
        
        // 提取姓名
        System.out.println("\n--- 提取所有用户的姓名 ---");
        Seq<String> names = users.pluck(User::getName);
        System.out.println("姓名列表：" + names.toList());
        
        // 提取邮箱
        System.out.println("\n--- 提取所有用户的邮箱 ---");
        Seq<String> emails = users.pluck(User::getEmail);
        System.out.println("邮箱列表：" + emails.toList());
        
        // 找年龄最小的用户
        System.out.println("\n--- 找年龄最小的用户 ---");
        User youngest = users.minBy(User::getAge).orElse(null);
        System.out.println("年龄最小的用户：" + youngest);
        
        // 找年龄最大的用户
        System.out.println("\n--- 找年龄最大的用户 ---");
        User oldest = users.maxBy(User::getAge).orElse(null);
        System.out.println("年龄最大的用户：" + oldest);
        
        // 统计活跃用户数量
        System.out.println("\n--- 统计活跃用户数量 ---");
        int activeCount = users.count(User::isActive);
        System.out.println("活跃用户数量：" + activeCount);
        
        // 判断是否所有用户都是活跃的
        System.out.println("\n--- 判断是否所有用户都是活跃的 ---");
        boolean allActive = users.all(User::isActive);
        System.out.println("所有用户都是活跃的吗？" + allActive);
        
        // 判断是否有北京的用户
        System.out.println("\n--- 判断是否有北京的用户 ---");
        boolean hasBeijingUser = users.any(u -> "北京".equals(u.getCity()));
        System.out.println("有北京的用户吗？" + hasBeijingUser);
    }
}
