package ltd.idcu.est.examples.microservices.order.controller;

import ltd.idcu.est.examples.microservices.order.model.Order;
import ltd.idcu.est.web.api.Get;
import ltd.idcu.est.web.api.Post;
import ltd.idcu.est.web.api.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class OrderController {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public OrderController() {
        orders.put("1", new Order("1", "1", "iPhone 15", 1, new BigDecimal("7999.00")));
        orders.put("2", new Order("2", "2", "MacBook Pro", 1, new BigDecimal("14999.00")));
        orders.put("3", new Order("3", "1", "AirPods Pro", 2, new BigDecimal("1899.00")));
    }

    @Get("/orders")
    public List<Order> getAllOrders() {
        System.out.println("[OrderService] GET /orders - Return all orders");
        return new ArrayList<>(orders.values());
    }

    @Get("/orders/{id}")
    public Order getOrderById(String id) {
        System.out.println("[OrderService] GET /orders/" + id + " - Get order");
        Order order = orders.get(id);
        if (order == null) {
            throw new RuntimeException("Order not found: " + id);
        }
        return order;
    }

    @Get("/orders/user/{userId}")
    public List<Order> getOrdersByUserId(String userId) {
        System.out.println("[OrderService] GET /orders/user/" + userId + " - Get user orders");
        return orders.values().stream()
                .filter(order -&gt; order.getUserId().equals(userId))
                .toList();
    }

    @Post("/orders")
    public Order createOrder(Order order) {
        String id = String.valueOf(orders.size() + 1);
        order.setId(id);
        order.setTotalAmount(order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
        order.setStatus("CREATED");
        order.setCreatedAt(java.time.LocalDateTime.now());
        orders.put(id, order);
        System.out.println("[OrderService] POST /orders - Create order: " + order);
        return order;
    }
}
