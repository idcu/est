package ltd.idcu.est.examples.basic.data;

import ltd.idcu.est.features.data.memory.MemoryData;
import ltd.idcu.est.features.data.memory.MemoryRepository;

import java.util.List;
import java.util.Optional;

public class Data03_AdvancedQuery {
    public static void main(String[] args) {
        System.out.println("=== 进阶篇：高级查询 ===\n");
        
        MemoryRepository<Product, Long> repo = MemoryData.newRepository();
        
        Product p1 = new Product();
        p1.setName("iPhone 15");
        p1.setPrice(5999.0);
        p1.setStock(100);
        repo.save(p1);
        
        Product p2 = new Product();
        p2.setName("iPhone 15 Pro");
        p2.setPrice(8999.0);
        p2.setStock(50);
        repo.save(p2);
        
        Product p3 = new Product();
        p3.setName("MacBook Pro");
        p3.setPrice(14999.0);
        p3.setStock(30);
        repo.save(p3);
        
        Product p4 = new Product();
        p4.setName("iPad");
        p4.setPrice(3999.0);
        p4.setStock(0);
        repo.save(p4);
        
        System.out.println("--- 查找价格小于 10000 的产品 ---");
        List<Product> affordable = repo.findByPredicate(p -> p.getPrice() < 10000);
        affordable.forEach(System.out::println);
        
        System.out.println("\n--- 查找有库存的产品 ---");
        List<Product> inStock = repo.findByPredicate(p -> p.getStock() > 0);
        inStock.forEach(System.out::println);
        
        System.out.println("\n--- 查找名称包含 'iPhone' 的产品 ---");
        List<Product> iphones = repo.findByPredicate(p -> p.getName().contains("iPhone"));
        iphones.forEach(System.out::println);
        
        System.out.println("\n--- 查找第一个价格大于 5000 的产品 ---");
        Optional<Product> expensive = repo.findFirstByPredicate(p -> p.getPrice() > 5000);
        expensive.ifPresent(p -> System.out.println("找到：" + p));
        
        System.out.println("\n--- 统计价格大于 5000 的产品数量 ---");
        long count = repo.countByPredicate(p -> p.getPrice() > 5000);
        System.out.println("数量：" + count);
        
        System.out.println("\n--- 删除所有缺货的产品 ---");
        repo.deleteByPredicate(p -> p.getStock() == 0);
        System.out.println("删除后剩余：" + repo.count() + " 个产品");
    }
}
