package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core05_ScopeExample {
    public static void main(String[] args) {
        System.out.println("=== 高级篇：作用域（Scope）===\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("--- 单例模式（Singleton）---");
        container.register(CounterService.class, CounterService.class, Scope.SINGLETON, "singleton");
        CounterService s1 = container.get(CounterService.class, "singleton");
        CounterService s2 = container.get(CounterService.class, "singleton");
        s1.increment();
        s2.increment();
        System.out.println("s1 计数：" + s1.getCount());
        System.out.println("s2 计数：" + s2.getCount());
        System.out.println("是否是同一个对象：" + (s1 == s2));
        
        System.out.println("\n--- 原型模式（Prototype）---");
        container.register(CounterService.class, CounterService.class, Scope.PROTOTYPE, "prototype");
        CounterService p1 = container.get(CounterService.class, "prototype");
        CounterService p2 = container.get(CounterService.class, "prototype");
        p1.increment();
        p2.increment();
        System.out.println("p1 计数：" + p1.getCount());
        System.out.println("p2 计数：" + p2.getCount());
        System.out.println("是否是同一个对象：" + (p1 == p2));
    }
}

class CounterService {
    private int count = 0;
    
    public void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}
