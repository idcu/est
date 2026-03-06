package ltd.idcu.est.examples.basic.patterns;

import ltd.idcu.est.patterns.api.creational.Factory;
import ltd.idcu.est.patterns.impl.creational.DefaultFactory;

public class Patterns03_Factory {
    public static void main(String[] args) {
        System.out.println("=== 工厂模式示例 ===");
        System.out.println();

        Factory<Food> noodlesFactory = DefaultFactory.of("noodles", Noodles::new);
        Factory<Food> riceFactory = DefaultFactory.of("rice", Rice::new);
        
        Food noodles = noodlesFactory.create();
        Food rice = riceFactory.create();
        
        noodles.eat();
        rice.eat();
    }
}

interface Food {
    void eat();
}

class Noodles implements Food {
    @Override
    public void eat() {
        System.out.println("吃面条");
    }
}

class Rice implements Food {
    @Override
    public void eat() {
        System.out.println("吃米饭");
    }
}
