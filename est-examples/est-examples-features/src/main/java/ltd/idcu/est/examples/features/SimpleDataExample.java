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
        System.out.println("=== EST Data Access Example ===");
        
        basicCRUDExample();
        queryExample();
        cacheIntegrationExample();
        
        System.out.println("\n[X] All examples complete!");
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
            return name + " $" + price + " (Stock:" + stock + ")";
        }
    }
    
    private static void basicCRUDExample() {
        System.out.println("\n--- Basic CRUD Operations ---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        
        Product p1 = new Product("iPhone 15", 5999.0, 100);
        Product p2 = new Product("MacBook Pro", 12999.0, 50);
        
        repository.save(p1);
        repository.save(p2);
        System.out.println("  Saved 2 products");
        
        System.out.println("  Total products: " + repository.count());
        
        Optional<Product> found = repository.findById(p1.getId());
        found.ifPresent(p -> System.out.println("  Found product: " + p));
        
        List<Product> all = repository.findAll();
        System.out.println("  All products:");
        all.forEach(p -> System.out.println("    - " + p));
        
        repository.delete(p2);
        System.out.println("  Total after delete: " + repository.count());
        
        logger.info("CRUD operations example complete");
    }
    
    private static void queryExample() {
        System.out.println("\n--- Query and Filtering ---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        
        repository.save(new Product("iPhone 15", 5999.0, 100));
        repository.save(new Product("iPhone 15 Pro", 7999.0, 80));
        repository.save(new Product("MacBook Air", 8999.0, 60));
        repository.save(new Product("AirPods", 1299.0, 300));
        
        List<Product> all = repository.findAll();
        System.out.println("  All products: " + all.size() + " items");
        
        System.out.println("  Products with price < 8000:");
        List<Product> affordable = new ArrayList<>();
        for (Product p : all) {
            if (p.getPrice() < 8000) {
                affordable.add(p);
                System.out.println("    - " + p);
            }
        }
        
        logger.info("Query example complete");
    }
    
    private static void cacheIntegrationExample() {
        System.out.println("\n--- Data Access + Cache ---");
        
        Repository<Product, Long> repository = MemoryData.newRepository();
        ltd.idcu.est.cache.api.Cache<Long, Product> cache = 
            new ltd.idcu.est.cache.memory.MemoryCache<>();
        
        Product product = new Product("Hot Product", 99.0, 1000);
        repository.save(product);
        
        System.out.println("  First query (from database): " + product);
        
        cache.put(product.getId(), product);
        System.out.println("  Written to cache");
        
        Optional<Product> cached = cache.get(product.getId());
        cached.ifPresent(p -> System.out.println("  Second query (from cache): " + p));
        
        logger.info("Cache integration example complete");
    }
}
