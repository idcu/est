package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core01_FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Core 第一个示例 ===");
        System.out.println("这个示例将带你5分钟上手 EST Core！\n");
        
        Container container = new DefaultContainer();
        
        container.register(MyService.class, MyServiceImpl.class);
        
        MyService service = container.get(MyService.class);
        
        System.out.println(service.hello("小白"));
    }
}

interface MyService {
    String hello(String name);
}

class MyServiceImpl implements MyService {
    @Override
    public String hello(String name) {
        return "你好，" + name + "！欢迎使用 EST Core！";
    }
}
