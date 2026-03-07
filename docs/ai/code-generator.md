# 代码生成器指�?
学习如何使用 EST 代码生成器自动生成各种代码�?
---

## 基本用法

### 创建代码生成�?
首先，创建一�?`CodeGenerator` 实例�?
```java
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.impl.DefaultCodeGenerator;

public class CodeGeneratorUsage {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 使用 generator...
    }
}
```

或者从 `AiAssistant` 获取�?
```java
AiAssistant assistant = new DefaultAiAssistant();
CodeGenerator generator = assistant.getCodeGenerator();
```

---

## 生成 Entity

### 基本用法

```java
String entityCode = generator.generateEntity(
    "User",
    "com.example.entity",
    Map.of()
);
```

### 带字段定�?
```java
String entityCode = generator.generateEntity(
    "Product",
    "com.example.entity",
    Map.of(
        "fields", List.of(
            "id:Long",
            "name:String",
            "price:BigDecimal",
            "stock:Integer",
            "createdAt:LocalDateTime"
        ),
        "useValidation", true
    )
);
```

生成�?Entity 示例�?
```java
package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;
    
    public Product() {
    }
    
    public Product(Long id, String name, BigDecimal price, Integer stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
```

---

## 生成 Repository

### 基本用法

```java
String repoCode = generator.generateRepository(
    "User",
    "UserRepository",
    "com.example.repository",
    Map.of()
);
```

生成�?Repository 示例�?
```java
package com.example.repository;

import com.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    List<User> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
    
    long count();
}
```

---

## 生成 Service

### 基本用法

```java
String serviceCode = generator.generateService(
    "User",
    "UserService",
    "com.example.service",
    Map.of()
);
```

### 带选项

```java
String serviceCode = generator.generateService(
    "Product",
    "ProductService",
    "com.example.service",
    Map.of(
        "useDependencyInjection", true,
        "includeValidation", true
    )
);
```

生成�?Service 示例�?
```java
package com.example.service;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import ltd.idcu.est.core.container.api.Inject;
import java.util.List;
import java.util.Optional;

public class ProductService {
    
    private final ProductRepository repository;
    
    @Inject
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    
    public Product create(Product product) {
        return repository.save(product);
    }
    
    public Optional<Product> getById(Long id) {
        return repository.findById(id);
    }
    
    public List<Product> getAll() {
        return repository.findAll();
    }
    
    public Product update(Product product) {
        return repository.save(product);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
    
    public long count() {
        return repository.count();
    }
}
```

---

## 生成 Controller

### 基本用法

```java
String controllerCode = generator.generateController(
    "User",
    "UserController",
    "com.example.controller",
    Map.of()
);
```

### 带路由选项

```java
String controllerCode = generator.generateController(
    "Product",
    "ProductController",
    "com.example.controller",
    Map.of(
        "routes", List.of("GET", "POST", "PUT", "DELETE"),
        "basePath", "/api/products"
    )
);
```

生成�?Controller 示例�?
```java
package com.example.controller;

import com.example.entity.Product;
import com.example.service.ProductService;
import ltd.idcu.est.core.container.api.Inject;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.WebRequest;
import ltd.idcu.est.web.api.WebResponse;
import java.util.List;

public class ProductController {
    
    private final ProductService service;
    
    @Inject
    public ProductController(ProductService service) {
        this.service = service;
    }
    
    public void registerRoutes(WebApplication app) {
        app.get("/api/products", this::getAll);
        app.get("/api/products/{id}", this::getById);
        app.post("/api/products", this::create);
        app.put("/api/products/{id}", this::update);
        app.delete("/api/products/{id}", this::delete);
    }
    
    private void getAll(WebRequest req, WebResponse res) {
        List<Product> products = service.getAll();
        res.send(products);
    }
    
    private void getById(WebRequest req, WebResponse res) {
        Long id = Long.parseLong(req.pathParam("id"));
        service.getById(id)
            .ifPresentOrElse(
                res::send,
                () -> res.status(404).send("Not found")
            );
    }
    
    private void create(WebRequest req, WebResponse res) {
        Product product = req.bodyAs(Product.class);
        Product saved = service.create(product);
        res.status(201).send(saved);
    }
    
    private void update(WebRequest req, WebResponse res) {
        Long id = Long.parseLong(req.pathParam("id"));
        Product product = req.bodyAs(Product.class);
        product.setId(id);
        Product updated = service.update(product);
        res.send(updated);
    }
    
    private void delete(WebRequest req, WebResponse res) {
        Long id = Long.parseLong(req.pathParam("id"));
        service.deleteById(id);
        res.status(204).send();
    }
}
```

---

## 生成完整�?Web 应用

### 基本用法

```java
String webAppCode = generator.generateWebApp(
    "my-webapp",
    "com.example",
    Map.of(
        "entities", List.of("User", "Product", "Order")
    )
);
```

### 带完整选项

```java
String webAppCode = generator.generateWebApp(
    "ecommerce-app",
    "com.ecommerce",
    Map.of(
        "entities", List.of("User", "Product", "Order", "OrderItem"),
        "includeAuth", true,
        "includeSwagger", true,
        "includeTests", true,
        "database", "h2"
    )
);
```

---

## 生成测试代码

### 基本用法

```java
String testCode = generator.generateTest(
    "UserService",
    "UserServiceTest",
    "com.example.service",
    Map.of()
);
```

生成的测试示例：

```java
package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreate() {
        User user = new User();
        user.setName("Test User");
        
        when(repository.save(user)).thenReturn(user);
        
        User result = service.create(user);
        
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(repository, times(1)).save(user);
    }
    
    @Test
    public void testGetById() {
        User user = new User();
        user.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        
        Optional<User> result = service.getById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get().getId());
    }
    
    @Test
    public void testGetAll() {
        User user1 = new User();
        User user2 = new User();
        
        when(repository.findAll()).thenReturn(List.of(user1, user2));
        
        List<User> result = service.getAll();
        
        assertEquals(2, result.size());
    }
}
```

---

## 生成 pom.xml

### 基本用法

```java
String pomXml = generator.generatePomXml(
    "my-project",
    "com.example",
    "my-app",
    "1.0.0"
);
```

生成�?pom.xml 示例�?
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <name>my-project</name>
    <description>My EST application</description>
    
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <est.version>2.0.0</est.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>ltd.idcu.est</groupId>
            <artifactId>est-core-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu.est</groupId>
            <artifactId>est-core-impl</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu.est</groupId>
            <artifactId>est-web-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu.est</groupId>
            <artifactId>est-web-impl</artifactId>
            <version>${est.version}</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 完整示例

```java
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.impl.DefaultCodeGenerator;
import java.util.List;
import java.util.Map;

public class CompleteCodeGeneratorExample {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        System.out.println("=== EST 代码生成器完整示�?===\n");
        
        // 1. 生成 Entity
        System.out.println("--- 1. 生成 Entity ---");
        String entity = generator.generateEntity(
            "Product",
            "com.example.entity",
            Map.of(
                "fields", List.of("id:Long", "name:String", "price:BigDecimal")
            )
        );
        System.out.println(entity);
        
        // 2. 生成 Repository
        System.out.println("\n--- 2. 生成 Repository ---");
        String repo = generator.generateRepository(
            "Product",
            "ProductRepository",
            "com.example.repository",
            Map.of()
        );
        System.out.println(repo);
        
        // 3. 生成 Service
        System.out.println("\n--- 3. 生成 Service ---");
        String service = generator.generateService(
            "Product",
            "ProductService",
            "com.example.service",
            Map.of()
        );
        System.out.println(service);
        
        // 4. 生成 Controller
        System.out.println("\n--- 4. 生成 Controller ---");
        String controller = generator.generateController(
            "Product",
            "ProductController",
            "com.example.controller",
            Map.of()
        );
        System.out.println(controller);
        
        // 5. 生成 pom.xml
        System.out.println("\n--- 5. 生成 pom.xml ---");
        String pom = generator.generatePomXml(
            "my-project",
            "com.example",
            "my-app",
            "1.0.0"
        );
        System.out.println(pom);
        
        System.out.println("\n�?完成�?);
    }
}
```

---

**下一�?*: 学习 [提示词工程](prompt-engineering.md)
