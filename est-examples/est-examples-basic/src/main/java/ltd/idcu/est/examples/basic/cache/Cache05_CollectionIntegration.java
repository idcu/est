package ltd.idcu.est.examples.basic.cache;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;
import ltd.idcu.est.cache.memory.MemoryCache;

import java.util.ArrayList;
import java.util.List;

public class Cache05_CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== 集成篇：EST Cache + EST Collection ===\n");
        
        MemoryCache<String, Product> cache = (MemoryCache<String, Product>) Caches.newMemoryCache();
        
        Product p1 = new Product(1L, "iPhone 15", 5999.0, 100);
        Product p2 = new Product(2L, "iPhone 15 Pro", 8999.0, 50);
        Product p3 = new Product(3L, "MacBook Pro", 14999.0, 30);
        Product p4 = new Product(4L, "iPad", 3999.0, 200);
        Product p5 = new Product(5L, "AirPods Pro", 1999.0, 150);
        
        cache.put("product:1", p1);
        cache.put("product:2", p2);
        cache.put("product:3", p3);
        cache.put("product:4", p4);
        cache.put("product:5", p5);
        
        System.out.println("--- 1. 从缓存获取所有产品，转换�?Seq ---");
        List<Product> productList = new ArrayList<>(cache.values());
        Seq<Product> products = Seqs.from(productList);
        
        System.out.println("\n--- 2. 筛选价格小�?10000 的产�?---");
        products
            .where(p -> p.getPrice() < 10000)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 3. 计算所有产品的总库存价�?---");
        double totalValue = products
            .mapToDouble(p -> p.getPrice() * p.getStock())
            .sum();
        System.out.println("库存总价值：" + totalValue + " �?);
        
        System.out.println("\n--- 4. 找出库存最多的 3 个产�?---");
        products
            .sortBy(Product::getStock, true)
            .take(3)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 5. 按价格区间分组统�?---");
        long cheapCount = products.where(p -> p.getPrice() < 5000).count();
        long midCount = products.where(p -> p.getPrice() >= 5000 && p.getPrice() < 10000).count();
        long expensiveCount = products.where(p -> p.getPrice() >= 10000).count();
        
        System.out.println("价格 < 5000�? + cheapCount + " �?);
        System.out.println("5000-10000�? + midCount + " �?);
        System.out.println("价格 >= 10000�? + expensiveCount + " �?);
    }
}

class Product {
    private Long id;
    private String name;
    private double price;
    private int stock;
    
    public Product(Long id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}
