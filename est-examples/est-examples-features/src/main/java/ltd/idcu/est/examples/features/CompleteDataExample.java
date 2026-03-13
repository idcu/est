package ltd.idcu.est.examples.features;

import ltd.idcu.est.data.api.Entity;
import ltd.idcu.est.data.api.Id;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.api.Query;
import ltd.idcu.est.data.api.Orm;
import ltd.idcu.est.data.memory.MemoryData;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class CompleteDataExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteDataExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST Data Access Module - Complete Example");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates various features of the EST Data Access module:");
        System.out.println("  - Entity Definition (@Entity, @Id)");
        System.out.println("  - Repository Pattern (CRUD Operations)");
        System.out.println("  - Multiple Implementations (Memory, JDBC, etc.)");
        System.out.println("  - Query and Filter Functions");
        System.out.println("  - Integration with Cache Module");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Part 1: Understanding the Role of Data Access");
        System.out.println("=".repeat(70));
        System.out.println("\n[Why Do We Need a Data Access Module?]");
        System.out.println("  - Data access module helps simplify database operations");
        System.out.println("  - No need to write complex SQL statements");
        System.out.println("  - Can easily switch between different databases (Memory, MySQL, MongoDB, etc.)");
        System.out.println("  - Code is cleaner and easier to maintain\n");
        
        System.out.println("=".repeat(70));
        System.out.println("Part 2: Defining Entities");
        System.out.println("=".repeat(70));
        System.out.println("\n[What is an Entity?]");
        System.out.println("  - Entity is the data object you want to store, like User, Order, Product, etc.");
        System.out.println("  - Use @Entity annotation to mark this as a database table");
        System.out.println("  - Use @Id annotation to mark primary key field\n");
        
        memoryRepositoryExample();
        queryExample();
        batchOperationsExample();
        integrationWithCacheExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[X] All data access examples completed!");
        System.out.println("=".repeat(70));
    }
    
    @Entity(tableName = "products")
    public static class Product {
        @Id
        private Long id;
        private String name;
        private double price;
        private int stock;
        private String category;
        
        public Product() {}
        
        public Product(String name, double price, int stock, String category) {
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.category = category;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        @Override
        public String toString() {
            return "Product{id=" + id + ", name='" + name + "', price=" + price + 
                   ", stock=" + stock + ", category='" + category + "'}";
        }
    }
    
    private static void memoryRepositoryExample() {
        System.out.println("\n--- Approach 1: Memory Repository (Fastest, Suitable for Dev/Test) ---");
        System.out.println("\n[Memory Repository Features]");
        System.out.println("  - Data stored in memory, extremely fast read/write");
        System.out.println("  - No need to configure database");
        System.out.println("  - Data lost after program restart (suitable for testing)");
        System.out.println("  - Perfect for unit testing\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("Step 1: Create product entities");
        Product product1 = new Product("iPhone 15", 5999.0, 100, "Phones");
        Product product2 = new Product("MacBook Pro", 12999.0, 50, "Computers");
        Product product3 = new Product("AirPods Pro", 1899.0, 200, "Accessories");
        
        System.out.println("   - " + product1.getName());
        System.out.println("   - " + product2.getName());
        System.out.println("   - " + product3.getName());
        
        System.out.println("\nStep 2: Save to Repository (like database INSERT)");
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        logger.info("Saved 3 products to Repository");
        
        System.out.println("\nStep 3: Query total count (COUNT)");
        long totalCount = productRepo.count();
        System.out.println("   Total products: " + totalCount);
        
        System.out.println("\nStep 4: Query by ID (SELECT BY ID)");
        Optional<Product> foundProduct = productRepo.findById(product1.getId());
        foundProduct.ifPresent(product -> {
            System.out.println("   Found product: " + product.getName());
            System.out.println("   Price: " + product.getPrice());
            System.out.println("   Stock: " + product.getStock());
        });
        
        System.out.println("\nStep 5: Query all products (SELECT *)");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   All products list:");
        for (Product p : allProducts) {
            System.out.println("     - " + p);
        }
        
        System.out.println("\nStep 6: Check if exists (EXISTS)");
        boolean exists = productRepo.existsById(product2.getId());
        System.out.println("   Product " + product2.getName() + " exists: " + exists);
        
        System.out.println("\nStep 7: Update product (UPDATE)");
        Product productToUpdate = foundProduct.get();
        productToUpdate.setPrice(5799.0);
        productToUpdate.setStock(95);
        productRepo.save(productToUpdate);
        System.out.println("   Updated product price and stock");
        
        System.out.println("\nStep 8: Delete single product (DELETE)");
        productRepo.delete(product2);
        System.out.println("   Deleted product: " + product2.getName());
        System.out.println("   Current total products: " + productRepo.count());
        
        System.out.println("\nStep 9: Clear all data (DELETE ALL)");
        productRepo.deleteAll();
        System.out.println("   Cleared all products");
        System.out.println("   Current total products: " + productRepo.count());
        
        logger.info("Memory Repository example completed");
    }
    
    private static void queryExample() {
        System.out.println("\n--- Approach 2: Query and Filter ---");
        System.out.println("\n[Role of Queries]");
        System.out.println("  - Find records you need from large amounts of data");
        System.out.println("  - Supports various condition filtering");
        System.out.println("  - Supports sorting and pagination\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("Step 1: Prepare test data");
        productRepo.save(new Product("iPhone 15", 5999.0, 100, "Phones"));
        productRepo.save(new Product("iPhone 15 Pro", 7999.0, 80, "Phones"));
        productRepo.save(new Product("MacBook Air", 8999.0, 60, "Computers"));
        productRepo.save(new Product("MacBook Pro", 12999.0, 50, "Computers"));
        productRepo.save(new Product("AirPods", 1299.0, 300, "Accessories"));
        productRepo.save(new Product("AirPods Pro", 1899.0, 200, "Accessories"));
        System.out.println("   Inserted 6 products\n");
        
        System.out.println("Step 2: Query all products");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   All products: " + allProducts.size() + " items\n");
        
        System.out.println("Step 3: Manual filtering (simple scenarios)");
        System.out.println("   Find products with price less than 8000:");
        List<Product> affordableProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getPrice() < 8000) {
                affordableProducts.add(p);
            }
        }
        for (Product p : affordableProducts) {
            System.out.println("     - " + p.getName() + " $" + p.getPrice());
        }
        
        System.out.println("\nStep 4: Filter by category");
        System.out.println("   Find all phone category products:");
        List<Product> phoneProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if ("Phones".equals(p.getCategory())) {
                phoneProducts.add(p);
            }
        }
        for (Product p : phoneProducts) {
            System.out.println("     - " + p.getName());
        }
        
        System.out.println("\nStep 5: Filter by stock");
        System.out.println("   Find products with stock greater than 100:");
        List<Product> highStockProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getStock() > 100) {
                highStockProducts.add(p);
            }
        }
        for (Product p : highStockProducts) {
            System.out.println("     - " + p.getName() + " (Stock: " + p.getStock() + ")");
        }
        
        logger.info("Query example completed");
    }
    
    private static void batchOperationsExample() {
        System.out.println("\n--- Approach 3: Batch Operations ---");
        System.out.println("\n[Benefits of Batch Operations]");
        System.out.println("  - Process multiple records at once, more efficient");
        System.out.println("  - Reduce database interaction times");
        System.out.println("  - Suitable for data import, initialization, etc.\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("Step 1: Batch save products");
        List<Product> productsToSave = new ArrayList<>();
        productsToSave.add(new Product("Product A", 100.0, 50, "Category1"));
        productsToSave.add(new Product("Product B", 200.0, 30, "Category1"));
        productsToSave.add(new Product("Product C", 300.0, 40, "Category2"));
        productsToSave.add(new Product("Product D", 400.0, 20, "Category2"));
        productsToSave.add(new Product("Product E", 500.0, 60, "Category3"));
        
        System.out.println("   Preparing to batch save " + productsToSave.size() + " products");
        
        for (Product p : productsToSave) {
            productRepo.save(p);
        }
        
        System.out.println("   Batch save completed");
        System.out.println("   Current total products: " + productRepo.count());
        
        System.out.println("\nStep 2: Batch query");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   Batch queried: " + allProducts.size() + " products");
        
        System.out.println("\nStep 3: Batch delete");
        System.out.println("   Clearing all products...");
        productRepo.deleteAll();
        System.out.println("   Batch delete completed");
        System.out.println("   Current total products: " + productRepo.count());
        
        logger.info("Batch operations example completed");
    }
    
    private static void integrationWithCacheExample() {
        System.out.println("\n--- Approach 4: Data Access + Cache (Performance Optimization) ---");
        System.out.println("\n[Why Combine with Cache?]");
        System.out.println("  - Database queries are relatively slow");
        System.out.println("  - Hot data can be cached, get directly from cache next time");
        System.out.println("  - Significantly improve application performance\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        ltd.idcu.est.cache.api.Cache<Long, Product> productCache = 
            new ltd.idcu.est.cache.memory.MemoryCache<>();
        
        System.out.println("Step 1: Prepare data");
        Product hotProduct = new Product("Hot Product", 99.0, 1000, "Hot");
        productRepo.save(hotProduct);
        System.out.println("   Saved hot product to database\n");
        
        System.out.println("Step 2: First query (from database, slower)");
        long start1 = System.currentTimeMillis();
        Optional<Product> fromDb = productRepo.findById(hotProduct.getId());
        long end1 = System.currentTimeMillis();
        System.out.println("   Query from database took: " + (end1 - start1) + "ms");
        
        System.out.println("\nStep 3: Write to cache");
        if (fromDb.isPresent()) {
            productCache.put(hotProduct.getId(), fromDb.get());
            System.out.println("   Written product to cache");
        }
        
        System.out.println("\nStep 4: Second query (from cache, extremely fast)");
        long start2 = System.currentTimeMillis();
        java.util.Optional<Product> fromCache = productCache.get(hotProduct.getId());
        long end2 = System.currentTimeMillis();
        System.out.println("   Query from cache took: " + (end2 - start2) + "ms");
        
        if (fromCache.isPresent()) {
            System.out.println("   Got from cache: " + fromCache.get().getName());
        }
        
        System.out.println("\n   [Tip] Cache speed is usually 10-100x faster than database!");
        
        logger.info("Data access and cache integration example completed");
    }
}
