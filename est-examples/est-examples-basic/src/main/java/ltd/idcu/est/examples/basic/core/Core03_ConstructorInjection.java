package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core03_ConstructorInjection {
    public static void main(String[] args) {
        System.out.println("=== 进阶篇：构造函数注入 ===\n");
        
        Container container = new DefaultContainer();
        
        container.register(UserService.class, UserServiceImpl.class);
        container.register(ProductService.class, ProductServiceImpl.class);
        container.register(OrderService.class, OrderServiceImpl.class);
        
        OrderService orderService = container.get(OrderService.class);
        System.out.println(orderService.createOrder("iPhone 15"));
    }
}

interface UserService {
    String getUserName();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserName() {
        return "张三";
    }
}

interface ProductService {
    String getProductName();
}

class ProductServiceImpl implements ProductService {
    @Override
    public String getProductName() {
        return "iPhone 15";
    }
}

interface OrderService {
    String createOrder(String product);
}

class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ProductService productService;
    
    public OrderServiceImpl(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    
    @Override
    public String createOrder(String product) {
        return "订单创建成功！用户：" + userService.getUserName() + 
               "，产品：" + productService.getProductName();
    }
}
