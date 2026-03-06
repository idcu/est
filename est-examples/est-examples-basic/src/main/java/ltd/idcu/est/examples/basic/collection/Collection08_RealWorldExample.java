package ltd.idcu.est.examples.basic.collection;

import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

class Order {
    private String orderId;
    private double amount;
    private boolean paid;
    private String userId;
    private String product;
    
    public Order(String orderId, double amount, boolean paid, String userId, String product) {
        this.orderId = orderId;
        this.amount = amount;
        this.paid = paid;
        this.userId = userId;
        this.product = product;
    }
    
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }
    public String getUserId() { return userId; }
    public String getProduct() { return product; }
}

public class Collection08_RealWorldExample {
    public static void main(String[] args) {
        System.out.println("=== EST Collection 实战案例：电商数据处理 ===\n");
        
        Seq<Order> orders = Seqs.of(
            new Order("ORDER001", 150.0, true, "USER001", "iPhone"),
            new Order("ORDER002", 80.0, true, "USER002", "耳机"),
            new Order("ORDER003", 200.0, false, "USER003", "iPad"),
            new Order("ORDER004", 300.0, true, "USER004", "MacBook"),
            new Order("ORDER005", 50.0, true, "USER005", "充电器"),
            new Order("ORDER006", 180.0, true, "USER006", "Apple Watch"),
            new Order("ORDER007", 250.0, true, "USER001", "AirPods"),
            new Order("ORDER008", 120.0, false, "USER007", "键盘"),
            new Order("ORDER009", 350.0, true, "USER008", "iPhone Pro"),
            new Order("ORDER010", 90.0, true, "USER009", "鼠标")
        );
        
        System.out.println("所有订单：");
        orders.forEach(order -> {
            System.out.printf("  %s: %s - ¥%.2f (%s)%n", 
                order.getOrderId(), 
                order.getProduct(), 
                order.getAmount(), 
                order.isPaid() ? "已支付" : "未支付");
        });
        
        // 1. 筛选出已支付且金额大于100元的订单
        System.out.println("\n--- 1. 已支付且金额>100元的订单 ---");
        Seq<Order> filteredOrders = orders
            .where(Order::isPaid)
            .where(order -> order.getAmount() > 100);
        
        filteredOrders.forEach(order -> {
            System.out.printf("  %s: %s - ¥%.2f%n", 
                order.getOrderId(), order.getProduct(), order.getAmount());
        });
        
        // 2. 按金额从大到小排序，取前5个
        System.out.println("\n--- 2. 金额最高的前5个订单 ---");
        Seq<Order> top5Orders = filteredOrders
            .sortByDesc(Order::getAmount)
            .take(5);
        
        top5Orders.forEach(order -> {
            System.out.printf("  %s: %s - ¥%.2f%n", 
                order.getOrderId(), order.getProduct(), order.getAmount());
        });
        
        // 3. 计算这些订单的总金额
        System.out.println("\n--- 3. 符合条件的订单总金额 ---");
        double totalAmount = filteredOrders
            .fold(0.0, (sum, order) -> sum + order.getAmount());
        System.out.printf("  总金额：¥%.2f%n", totalAmount);
        
        // 4. 提取订单号和金额
        System.out.println("\n--- 4. 订单号和金额列表 ---");
        Seq<Pair<String, Double>> orderAmounts = filteredOrders
            .map(order -> new Pair<>(order.getOrderId(), order.getAmount()));
        
        orderAmounts.forEach(pair -> {
            System.out.printf("  %s: ¥%.2f%n", pair.getFirst(), pair.getSecond());
        });
        
        // 5. 统计每个用户的订单数和消费总额
        System.out.println("\n--- 5. 每个用户的订单统计 ---");
        var userStats = orders
            .where(Order::isPaid)
            .groupBy(Order::getUserId);
        
        userStats.forEach((userId, userOrders) -> {
            int orderCount = userOrders.count();
            double userTotal = userOrders.fold(0.0, (sum, order) -> sum + order.getAmount());
            System.out.printf("  用户 %s: %d 个订单，总消费 ¥%.2f%n", 
                userId, orderCount, userTotal);
        });
        
        // 6. 找出消费最高的用户
        System.out.println("\n--- 6. 消费最高的用户 ---");
        Pair<String, Double> topUser = userStats.entrySet().stream()
            .map(entry -> {
                double total = entry.getValue().fold(0.0, (sum, order) -> sum + order.getAmount());
                return new Pair<>(entry.getKey(), total);
            })
            .max((p1, p2) -> Double.compare(p1.getSecond(), p2.getSecond()))
            .orElse(null);
        
        if (topUser != null) {
            System.out.printf("  消费最高的用户：%s，总消费 ¥%.2f%n", 
                topUser.getFirst(), topUser.getSecond());
        }
        
        System.out.println("\n恭喜！你已经完成了一个完整的实战案例！🎉");
    }
}
