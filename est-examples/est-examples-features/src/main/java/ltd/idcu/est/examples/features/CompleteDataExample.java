package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.data.api.Entity;
import ltd.idcu.est.features.data.api.Id;
import ltd.idcu.est.features.data.api.Repository;
import ltd.idcu.est.features.data.api.Query;
import ltd.idcu.est.features.data.api.Orm;
import ltd.idcu.est.features.data.memory.MemoryData;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class CompleteDataExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteDataExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST 数据访问模块 - 完整示例");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例将展示 EST 数据访问模块的各种功能：");
        System.out.println("  - 实体定义（@Entity, @Id）");
        System.out.println("  - Repository 模式（CRUD 操作）");
        System.out.println("  - 多种实现方式（内存、JDBC 等）");
        System.out.println("  - 查询和过滤功能");
        System.out.println("  - 与缓存模块联动");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("第一部分：理解数据访问模块的作用");
        System.out.println("=".repeat(70));
        System.out.println("\n【为什么需要数据访问模块？】");
        System.out.println("  - 数据访问模块帮你简化数据库操作");
        System.out.println("  - 不需要写复杂的 SQL 语句");
        System.out.println("  - 可以轻松切换不同的数据库（内存、MySQL、MongoDB 等）");
        System.out.println("  - 代码更简洁、更易维护\n");
        
        System.out.println("=".repeat(70));
        System.out.println("第二部分：定义实体（Entity）");
        System.out.println("=".repeat(70));
        System.out.println("\n【什么是实体？】");
        System.out.println("  - 实体就是你要存储的数据对象，比如用户、订单、商品等");
        System.out.println("  - 使用 @Entity 注解标记这是一个数据库表");
        System.out.println("  - 使用 @Id 注解标记主键字段\n");
        
        memoryRepositoryExample();
        queryExample();
        batchOperationsExample();
        integrationWithCacheExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有数据访问示例完成！");
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
        System.out.println("\n--- 方式一：内存 Repository（最快，适合开发测试）---");
        System.out.println("\n【内存 Repository 的特点】");
        System.out.println("  - 数据存储在内存中，读写速度极快");
        System.out.println("  - 不需要配置数据库");
        System.out.println("  - 程序重启后数据会丢失（适合测试）");
        System.out.println("  - 完美配合单元测试\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("步骤 1: 创建产品实体");
        Product product1 = new Product("iPhone 15", 5999.0, 100, "手机");
        Product product2 = new Product("MacBook Pro", 12999.0, 50, "电脑");
        Product product3 = new Product("AirPods Pro", 1899.0, 200, "配件");
        
        System.out.println("   - " + product1.getName());
        System.out.println("   - " + product2.getName());
        System.out.println("   - " + product3.getName());
        
        System.out.println("\n步骤 2: 保存到 Repository（类似数据库的 INSERT）");
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        logger.info("已保存 3 个产品到 Repository");
        
        System.out.println("\n步骤 3: 查询总数（COUNT）");
        long totalCount = productRepo.count();
        System.out.println("   产品总数: " + totalCount);
        
        System.out.println("\n步骤 4: 根据 ID 查询（SELECT BY ID）");
        Optional<Product> foundProduct = productRepo.findById(product1.getId());
        foundProduct.ifPresent(product -> {
            System.out.println("   找到产品: " + product.getName());
            System.out.println("   价格: " + product.getPrice());
            System.out.println("   库存: " + product.getStock());
        });
        
        System.out.println("\n步骤 5: 查询所有产品（SELECT *）");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   所有产品列表:");
        for (Product p : allProducts) {
            System.out.println("     - " + p);
        }
        
        System.out.println("\n步骤 6: 检查是否存在（EXISTS）");
        boolean exists = productRepo.existsById(product2.getId());
        System.out.println("   产品 " + product2.getName() + " 是否存在: " + exists);
        
        System.out.println("\n步骤 7: 更新产品（UPDATE）");
        Product productToUpdate = foundProduct.get();
        productToUpdate.setPrice(5799.0);
        productToUpdate.setStock(95);
        productRepo.save(productToUpdate);
        System.out.println("   已更新产品价格和库存");
        
        System.out.println("\n步骤 8: 删除单个产品（DELETE）");
        productRepo.delete(product2);
        System.out.println("   已删除产品: " + product2.getName());
        System.out.println("   当前产品总数: " + productRepo.count());
        
        System.out.println("\n步骤 9: 清空所有数据（DELETE ALL）");
        productRepo.deleteAll();
        System.out.println("   已清空所有产品");
        System.out.println("   当前产品总数: " + productRepo.count());
        
        logger.info("内存 Repository 示例完成");
    }
    
    private static void queryExample() {
        System.out.println("\n--- 方式二：查询和过滤 ---");
        System.out.println("\n【查询的作用】");
        System.out.println("  - 从大量数据中找到你需要的记录");
        System.out.println("  - 支持各种条件过滤");
        System.out.println("  - 支持排序和分页\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("步骤 1: 准备测试数据");
        productRepo.save(new Product("iPhone 15", 5999.0, 100, "手机"));
        productRepo.save(new Product("iPhone 15 Pro", 7999.0, 80, "手机"));
        productRepo.save(new Product("MacBook Air", 8999.0, 60, "电脑"));
        productRepo.save(new Product("MacBook Pro", 12999.0, 50, "电脑"));
        productRepo.save(new Product("AirPods", 1299.0, 300, "配件"));
        productRepo.save(new Product("AirPods Pro", 1899.0, 200, "配件"));
        System.out.println("   已插入 6 个产品\n");
        
        System.out.println("步骤 2: 查询所有产品");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   所有产品: " + allProducts.size() + " 个\n");
        
        System.out.println("步骤 3: 手动过滤（简单场景）");
        System.out.println("   查找价格小于 8000 的产品:");
        List<Product> affordableProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getPrice() < 8000) {
                affordableProducts.add(p);
            }
        }
        for (Product p : affordableProducts) {
            System.out.println("     - " + p.getName() + " ¥" + p.getPrice());
        }
        
        System.out.println("\n步骤 4: 按分类过滤");
        System.out.println("   查找所有手机类产品:");
        List<Product> phoneProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if ("手机".equals(p.getCategory())) {
                phoneProducts.add(p);
            }
        }
        for (Product p : phoneProducts) {
            System.out.println("     - " + p.getName());
        }
        
        System.out.println("\n步骤 5: 按库存过滤");
        System.out.println("   查找库存大于 100 的产品:");
        List<Product> highStockProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getStock() > 100) {
                highStockProducts.add(p);
            }
        }
        for (Product p : highStockProducts) {
            System.out.println("     - " + p.getName() + " (库存: " + p.getStock() + ")");
        }
        
        logger.info("查询示例完成");
    }
    
    private static void batchOperationsExample() {
        System.out.println("\n--- 方式三：批量操作 ---");
        System.out.println("\n【批量操作的好处】");
        System.out.println("  - 一次性处理多条数据，效率更高");
        System.out.println("  - 减少数据库交互次数");
        System.out.println("  - 适合导入数据、初始化数据等场景\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        
        System.out.println("步骤 1: 批量保存产品");
        List<Product> productsToSave = new ArrayList<>();
        productsToSave.add(new Product("产品 A", 100.0, 50, "分类1"));
        productsToSave.add(new Product("产品 B", 200.0, 30, "分类1"));
        productsToSave.add(new Product("产品 C", 300.0, 40, "分类2"));
        productsToSave.add(new Product("产品 D", 400.0, 20, "分类2"));
        productsToSave.add(new Product("产品 E", 500.0, 60, "分类3"));
        
        System.out.println("   准备批量保存 " + productsToSave.size() + " 个产品");
        
        for (Product p : productsToSave) {
            productRepo.save(p);
        }
        
        System.out.println("   批量保存完成！");
        System.out.println("   当前产品总数: " + productRepo.count());
        
        System.out.println("\n步骤 2: 批量查询");
        List<Product> allProducts = productRepo.findAll();
        System.out.println("   批量查询到 " + allProducts.size() + " 个产品");
        
        System.out.println("\n步骤 3: 批量删除");
        System.out.println("   清空所有产品...");
        productRepo.deleteAll();
        System.out.println("   批量删除完成！");
        System.out.println("   当前产品总数: " + productRepo.count());
        
        logger.info("批量操作示例完成");
    }
    
    private static void integrationWithCacheExample() {
        System.out.println("\n--- 方式四：数据访问 + 缓存（性能优化）---");
        System.out.println("\n【为什么要结合缓存？】");
        System.out.println("  - 数据库查询相对较慢");
        System.out.println("  - 热点数据可以缓存起来，下次直接从缓存取");
        System.out.println("  - 大幅提升应用性能\n");
        
        Repository<Product, Long> productRepo = MemoryData.newRepository();
        ltd.idcu.est.features.cache.api.Cache<Long, Product> productCache = 
            new ltd.idcu.est.features.cache.memory.MemoryCache<>();
        
        System.out.println("步骤 1: 准备数据");
        Product hotProduct = new Product("热销商品", 99.0, 1000, "热销");
        productRepo.save(hotProduct);
        System.out.println("   已保存热销商品到数据库\n");
        
        System.out.println("步骤 2: 第一次查询（从数据库，较慢）");
        long start1 = System.currentTimeMillis();
        Optional<Product> fromDb = productRepo.findById(hotProduct.getId());
        long end1 = System.currentTimeMillis();
        System.out.println("   从数据库查询耗时: " + (end1 - start1) + "ms");
        
        System.out.println("\n步骤 3: 写入缓存");
        if (fromDb.isPresent()) {
            productCache.put(hotProduct.getId(), fromDb.get());
            System.out.println("   已将商品写入缓存");
        }
        
        System.out.println("\n步骤 4: 第二次查询（从缓存，极快）");
        long start2 = System.currentTimeMillis();
        java.util.Optional<Product> fromCache = productCache.get(hotProduct.getId());
        long end2 = System.currentTimeMillis();
        System.out.println("   从缓存查询耗时: " + (end2 - start2) + "ms");
        
        if (fromCache.isPresent()) {
            System.out.println("   从缓存获取到: " + fromCache.get().getName());
        }
        
        System.out.println("\n   💡 提示：缓存速度通常比数据库快 10-100 倍！");
        
        logger.info("数据访问与缓存联动示例完成");
    }
}
