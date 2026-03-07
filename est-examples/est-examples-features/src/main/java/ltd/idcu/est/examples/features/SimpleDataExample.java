package ltd.idcu.est.examples.features;

import ltd.idcu.est.data.api.Entity;
import ltd.idcu.est.data.api.Id;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryData;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class SimpleDataExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleDataExample.class);
    
    public static void main(String[] args) {
        System.out.println("=== EST 数据访问示例 ===");
        
        basicCRUDExample();
        queryExample();
        cacheIntegrationExample();
        
        System.out.println("\n�?所有示例完成！");
    }
    
    @Entity(tableName = "products")
    public static class Product {
        @Id
        private Long id;
        private String name;
        private double price;
        private int stock;
        
        public Product() {}
        
        public Product(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        
        @Override
        public String toString() {
            return name + " ¥" + price + " (库存:" + stock + ")";
        }
    }
    
    private static void basicCRUDExample() {
        System.out.println("\n--- 基础 CRUD 操作 ---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        
        Product p1 = new Product("iPhone 15", 5999.0, 100);
        Product p2 = new Product("MacBook Pro", 12999.0, 50);
        
        repository.save(p1);
        repository.save(p2);
        System.out.println("  已保�?2 个产�?);
        
        System.out.println("  产品总数: " + repository.count());
        
        Optional<Product> found = repository.findById(p1.getId());
        found.ifPresent(p -> System.out.println("  找到产品: " + p));
        
        List<Product> all = repository.findAll();
        System.out.println("  所有产�?");
        all.forEach(p -> System.out.println("    - " + p));
        
        repository.delete(p2);
        System.out.println("  删除后总数: " + repository.count());
        
        logger.info("CRUD 操作示例完成");
    }
    
    private static void queryExample() {
        System.out.println("\n--- 查询和过�?---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        
        repository.save(new Product("iPhone 15", 5999.0, 100));
        repository.save(new Product("iPhone 15 Pro", 7999.0, 80));
        repository.save(new Product("MacBook Air", 8999.0, 60));
        repository.save(new Product("AirPods", 1299.0, 300));
        
        List<Product> all = repository.findAll();
        System.out.println("  所有产�? " + all.size() + " �?);
        
        System.out.println("  价格 < 8000 的产�?");
        List<Product> affordable = new ArrayList<>();
        for (Product p : all) {
            if (p.getPrice() < 8000) {
                affordable.add(p);
                System.out.println("    - " + p);
            }
        }
        
        logger.info("查询示例完成");
    }
    
    private static void cacheIntegrationExample() {
        System.out.println("\n--- 数据访问 + 缓存 ---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        ltd.idcu.est.cache.api.Cache<Long, Product> cache = 
            new ltd.idcu.est.cache.memory.MemoryCache<>();
        
        Product product = new Product("热销商品", 99.0, 1000);
        repository.save(product);
        
        System.out.println("  第一次查询（从数据库�? " + product);
        
        cache.put(product.getId(), product);
        System.out.println("  已写入缓�?);
        
        Optional<Product> cached = cache.get(product.getId());
        cached.ifPresent(p -> System.out.println("  第二次查询（从缓存）: " + p));
        
        logger.info("缓存联动示例完成");
    }
}
