package ltd.idcu.est.examples.basic.data;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.data.memory.MemoryData;
import ltd.idcu.est.features.data.memory.MemoryRepository;

import java.util.List;

public class Data04_CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== 集成篇：EST Data + EST Collection ===\n");
        
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
        
        Product p5 = new Product();
        p5.setName("AirPods Pro");
        p5.setPrice(1999.0);
        p5.setStock(200);
        repo.save(p5);
        
        List<Product> productList = repo.findAll();
        Seq<Product> products = Seqs.from(productList);
        
        System.out.println("--- 1. 筛选有库存且价格 < 10000 的产品 ---");
        products
            .where(p -> p.getStock() > 0)
            .where(p -> p.getPrice() < 10000)
            .forEach(System.out::println);
        
        System.out.println("\n--- 2. 按价格从低到高排序 ---");
        products
            .sortBy(Product::getPrice)
            .forEach(System.out::println);
        
        System.out.println("\n--- 3. 计算所有有库存产品的总价值 ---");
        double totalValue = products
            .where(p -> p.getStock() > 0)
            .mapToDouble(p -> p.getPrice() * p.getStock())
            .sum();
        System.out.println("库存总价值：" + totalValue + " 元");
        
        System.out.println("\n--- 4. 找出最贵的 3 个产品 ---");
        products
            .sortBy(Product::getPrice, true)
            .take(3)
            .forEach(System.out::println);
        
        System.out.println("\n--- 5. 统计各价格区间的产品数量 ---");
        System.out.println("价格 < 5000：" + products.where(p -> p.getPrice() < 5000).count());
        System.out.println("5000-10000：" + products.where(p -> p.getPrice() >= 5000 && p.getPrice() < 10000).count());
        System.out.println("价格 >= 10000：" + products.where(p -> p.getPrice() >= 10000).count());
    }
}
