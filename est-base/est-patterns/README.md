# EST Patterns - 璁捐妯″紡妯″潡

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Patterns �?EST 妗嗘灦鐨勮璁℃ā寮忔ā鍧楋紝鎻愪緵浜嗗父鐢ㄨ璁℃ā寮忕殑寮€绠卞嵆鐢ㄥ疄鐜帮紝甯姪浣犵紪鍐欐洿浼橀泤銆佹洿鍙淮鎶ょ殑浠ｇ爜銆?

---

## 馃摎 鐩�?

- [蹇€熷叆闂�?#蹇€熷叆闂?
- [鍩虹绡囷細鍒涘缓鍨嬫ā寮�?#鍩虹绡囧垱寤哄瀷妯″紡)
- [鍩虹绡囷細缁撴瀯鍨嬫ā寮�?#鍩虹绡囩粨鏋勫瀷妯″紡)
- [杩涢樁绡囷細琛屼负鍨嬫ā寮�?#杩涢樁绡囪涓哄瀷妯″紡)
- [鏈€浣冲疄璺�?#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸璁捐妯″紡�?

鎯宠薄浣犲湪鍋氳彍锛屾瘡娆″仛绾㈢儳鑲夐兘鐢ㄥ悓鏍风殑姝ラ锛氬垏鑲夈€佺劘姘淬€佺倰绯栬壊銆佺倴鐓?.. 杩欎釜鍥哄畾�?鑿滆�?灏辨槸涓€绉嶆ā寮忋€?

**璁捐妯″紡**灏辨槸缂栫▼涓�?鑿滆�?锛屾槸鍓嶄汉鎬荤粨鍑烘潵鐨勮В鍐崇壒瀹氶棶棰樼殑鏈€浣冲疄璺点€?

### 5鍒嗛挓涓婃墜

璁╂垜浠粠鏈€甯哥敤鐨?*鍗曚緥妯″紡**寮€濮嬶�?

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class FirstExample {
    public static void main(String[] args) {
        // 鍒涘缓鍗曚緥 - 纭繚鍙湁涓€涓疄渚?
        Singleton<DatabaseConnection> singleton = 
            DefaultSingleton.of(DatabaseConnection::new);
        
        // 鑾峰彇瀹炰緥锛堝娆¤幏鍙栭兘鏄悓涓€涓�?
        DatabaseConnection conn1 = singleton.getInstance();
        DatabaseConnection conn2 = singleton.getInstance();
        
        System.out.println("鏄惁鏄悓涓€涓疄渚? " + (conn1 == conn2));
    }
}

class DatabaseConnection {
    public DatabaseConnection() {
        System.out.println("鏁版嵁搴撹繛鎺ュ凡鍒涘缓");
    }
}
```

杩愯缁撴灉�?
```
鏁版嵁搴撹繛鎺ュ凡鍒涘缓
鏄惁鏄悓涓€涓疄渚? true
```

鎭枩锛佷綘宸茬粡瀛︿細浣跨敤绗竴涓璁℃ā寮忎簡锛?馃帀

---

## 馃敯 鍩虹绡囷細鍒涘缓鍨嬫ā寮?

### 鐢熸椿绫绘瘮

鍒涘缓鍨嬫ā寮忓氨�?鐢熶骇杞﹂棿"锛屼笓闂ㄨ礋�?鐢熶�?瀵硅薄銆?

### 1. 鍗曚緥妯″紡 (Singleton)

**鍦烘�?*锛氱彮绾ч噷鍙湁涓€涓彮闀匡紝澶у閮芥壘浠栨眹鎶ュ伐浣溿€?

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class SingletonExample {
    public static void main(String[] args) {
        // 鍒涘缓鐝暱鍗曚�?
        Singleton<ClassMonitor> monitorSingleton = 
            DefaultSingleton.of(ClassMonitor::new);
        
        // 澶氫釜鍚屽閮芥壘鍚屼竴涓彮闀?
        ClassMonitor monitor1 = monitorSingleton.getInstance();
        ClassMonitor monitor2 = monitorSingleton.getInstance();
        
        monitor1.report("灏忔�?);
        monitor2.report("灏忕�?);
        
        System.out.println("鏄悓涓€涓彮闀? " + (monitor1 == monitor2));
    }
}

class ClassMonitor {
    public void report(String studentName) {
        System.out.println(studentName + " 鍚戠彮闀挎眹鎶ュ伐浣?);
    }
}
```

**鐗圭�?*�?
- 鍙湁涓€涓疄�?
- 鍏ㄥ眬璁块棶�?
- 绾跨▼瀹夊�?

---

### 2. 宸ュ巶妯″紡 (Factory)

**鍦烘�?*锛氬幓椁愬巺鍚冮キ锛屼綘鍛婅瘔鏈嶅姟�?鏉ヤ竴浠藉淇濋浮涓?锛屽悗鍘ㄥ氨缁欎綘鍋氬ソ浜嗭紝浣犱笉鐢ㄥ叧蹇冩€庝箞鍋氱殑銆?

```java
import ltd.idcu.est.patterns.api.creational.Factory;
import ltd.idcu.est.patterns.impl.creational.DefaultFactory;

public class FactoryExample {
    public static void main(String[] args) {
        // 鍒涘缓鑿滃搧宸ュ�?
        Factory<Food> noodlesFactory = DefaultFactory.of("noodles", Noodles::new);
        Factory<Food> riceFactory = DefaultFactory.of("rice", Rice::new);
        
        // 鐐归�?- 宸ュ巶甯綘鍒涘�?
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
        System.out.println("鍚冮潰鏉?);
    }
}

class Rice implements Food {
    @Override
    public void eat() {
        System.out.println("鍚冪背楗?);
    }
}
```

**鐗圭�?*�?
- 涓嶇敤鑷繁 new 瀵硅�?
- 瀹规槗鎵╁睍鏂颁骇鍝?
- 灏佽鍒涘缓閫昏�?

---

### 3. 寤洪€犺€呮ā�?(Builder)

**鍦烘�?*锛氱粍瑁呯數鑴戯紝浣犲彲浠ラ€夋嫨涓嶅悓�?CPU銆佸唴瀛樸€佺‖鐩?.. 鑷敱缁勫悎�?

```java
import ltd.idcu.est.patterns.api.creational.Builder;
import ltd.idcu.est.patterns.impl.creational.AbstractBuilder;

public class BuilderExample {
    public static void main(String[] args) {
        // 鑷敱缁勮鐢佃�?
        Computer computer = new Computer.Builder()
            .cpu("Intel i7")
            .memory("16GB")
            .storage("512GB SSD")
            .build();
        
        System.out.println(computer);
    }
}

class Computer {
    private final String cpu;
    private final String memory;
    private final String storage;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.memory = builder.memory;
        this.storage = builder.storage;
    }
    
    @Override
    public String toString() {
        return "鐢佃剳閰嶇疆: CPU=" + cpu + ", 鍐呭�?" + memory + ", 纭�?" + storage;
    }
    
    public static class Builder extends AbstractBuilder<Computer> {
        private String cpu;
        private String memory;
        private String storage;
        
        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public Builder memory(String memory) {
            this.memory = memory;
            return this;
        }
        
        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }
        
        @Override
        protected Computer doBuild() {
            return new Computer(this);
        }
    }
}
```

**鐗圭�?*�?
- 閾惧紡璋冪敤
- 鍙傛暟鍙€?
-  immutable 瀵硅�?

---

## 馃敯 鍩虹绡囷細缁撴瀯鍨嬫ā寮?

### 鐢熸椿绫绘瘮

缁撴瀯鍨嬫ā寮忓氨�?瑁呬慨甯堝�?锛屽府浣犳妸涓滆タ缁勫悎鍦ㄤ竴璧枫€?

### 1. 瑁呴グ鍣ㄦā寮?(Decorator)

**鍦烘�?*锛氫拱鍜栧暋锛屽彲浠ュ姞绯栥€佸姞濂躲€佸姞鐝嶇�?.. 鎯冲姞浠€涔堝氨鍔犱粈涔堛€?

```java
import ltd.idcu.est.patterns.api.structural.Decorator;
import ltd.idcu.est.patterns.impl.structural.AbstractDecorator;

public class DecoratorExample {
    public static void main(String[] args) {
        // 鍩虹鍜栧暋
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " 浠锋�? " + coffee.getCost());
        
        // 鍔犵�?
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " 浠锋�? " + coffee.getCost());
        
        // 鍐嶅姞濂?
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " 浠锋�? " + coffee.getCost());
    }
}

interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "绠€鍗曞挅鍟?;
    }
    
    @Override
    public double getCost() {
        return 10.0;
    }
}

class MilkDecorator extends AbstractDecorator<Coffee> implements Coffee {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + 鐗涘�?;
    }
    
    @Override
    public double getCost() {
        return decorated.getCost() + 2.0;
    }
}

class SugarDecorator extends AbstractDecorator<Coffee> implements Coffee {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + �?;
    }
    
    @Override
    public double getCost() {
        return decorated.getCost() + 1.0;
    }
}
```

**鐗圭�?*�?
- 鍔ㄦ€佹坊鍔犲姛�?
- 涓嶄慨鏀瑰師浠ｇ�?
- 鍙粍鍚堝涓楗?

---

## 馃搱 杩涢樁绡囷細琛屼负鍨嬫ā寮?

### 鐢熸椿绫绘瘮

琛屼负鍨嬫ā寮忓氨�?娌熼€氬崗璋冨憳"锛屽府瀵硅薄涔嬮棿鏇村ソ鍦版矡閫氥�?

### 1. 瑙傚療鑰呮ā寮?(Observer)

**鍦烘�?*锛氬井淇＄兢鑱婏紝缇や富鍙戞秷鎭紝鎵€鏈変汉閮借兘鏀跺埌�?

```java
import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.patterns.api.behavioral.Subject;
import ltd.idcu.est.patterns.impl.behavioral.DefaultSubject;

public class ObserverExample {
    public static void main(String[] args) {
        // 鍒涘缓缇よ亰
        WeChatGroup group = new WeChatGroup();
        
        // 鍔犲叆鎴愬憳
        group.addMember(new Member("灏忔�?));
        group.addMember(new Member("灏忕�?));
        group.addMember(new Member("灏忓�?));
        
        // 缇や富鍙戞秷�?
        group.sendMessage("浠婃�?鐐瑰紑浼氾紒");
    }
}

class WeChatGroup {
    private final Subject<String> subject = new DefaultSubject<>();
    
    public void addMember(Observer<String> member) {
        subject.attach(member);
    }
    
    public void sendMessage(String message) {
        subject.notifyObservers(message);
    }
}

class Member implements Observer<String> {
    private final String name;
    
    public Member(String name) {
        this.name = name;
    }
    
    @Override
    public String getId() {
        return name;
    }
    
    @Override
    public void update(String message) {
        System.out.println(name + " 鏀跺埌娑堟�? " + message);
    }
}
```

**鐗圭�?*�?
- 涓€瀵瑰渚濊禆
- 鏉捐€﹀�?
- 鑷姩閫氱煡

---

### 2. 绛栫暐妯″紡 (Strategy)

**鍦烘�?*锛氬嚭琛屾柟寮忥紝鍙互閫夋嫨姝ヨ銆侀獞杞︺€佸紑�?.. 鎯虫€庝箞鍘诲氨鎬庝箞鍘汇€?

```java
import ltd.idcu.est.patterns.api.behavioral.Strategy;
import ltd.idcu.est.patterns.api.behavioral.StrategyContext;
import ltd.idcu.est.patterns.impl.behavioral.DefaultStrategy;
import ltd.idcu.est.patterns.impl.behavioral.DefaultStrategyContext;

public class StrategyExample {
    public static void main(String[] args) {
        // 瀹氫箟鍑鸿绛栫�?
        Strategy<String, String> walk = DefaultStrategy.of("walk", 
            place -> "姝ヨ鍘? + place + "锛岄渶瑕?0鍒嗛�?);
        Strategy<String, String> bike = DefaultStrategy.of("bike", 
            place -> "楠戣溅鍘? + place + "锛岄渶瑕?5鍒嗛�?);
        Strategy<String, String> car = DefaultStrategy.of("car", 
            place -> "寮€杞﹀�? + place + "锛岄渶瑕?鍒嗛�?);
        
        // 浣跨敤绛栫暐涓婁笅鏂?
        StrategyContext<String, String> context = new DefaultStrategyContext<>();
        context.registerStrategy("walk", walk);
        context.registerStrategy("bike", bike);
        context.registerStrategy("car", car);
        
        // 鏍规嵁鎯呭喌閫夋嫨绛栫暐
        System.out.println(context.execute("walk", "鍏�?));
        System.out.println(context.execute("bike", "瓒呭�?));
        System.out.println(context.execute("car", "鏈哄満"));
    }
}
```

**鐗圭�?*�?
- 绠楁硶鍙簰�?
- 鏄撲簬鎵╁睍
- 閬垮厤澶氶噸鍒ゆ�?

---

## �?鏈€浣冲疄璺?

### 1. 涓嶈涓轰簡鐢ㄦā寮忚€岀敤妯″紡

```java
// �?杩囧害璁捐 - 绠€鍗曞姛鑳界敤浜嗗鏉傛ā寮?
// 绠€鍗曠殑浜嬫儏绠€鍗曞�?

// �?绠€鍗曠洿鎺?
if (type.equals("A")) {
    doA();
} else {
    doB();
}
```

### 2. 缁勫悎浣跨敤澶氫釜妯″紡

```java
// 鍗曚�?+ 宸ュ�?+ 瑙傚療鑰?
// 寰堝鍦烘櫙闇€瑕佺粍鍚堜娇鐢ㄥ涓ā寮?
```

### 3. 鐞嗚В妯″紡鐨勬剰鍥?

姣忎釜妯″紡閮芥湁鐗瑰畾鐨勮В鍐抽棶棰橈紝涓嶈鍙褰㈠紡锛?
- Singleton锛氱‘淇濆敮涓€瀹炰�?
- Factory锛氬皝瑁呭璞″垱�?
- Observer锛氫竴瀵瑰閫氱煡

---

## 馃摝 妯″潡闆嗘�?

### �?est-collection 闆嗘�?

```java
import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        // �?Collection 绠＄悊瑙傚療�?
        Seqs.of(new Member("灏忔�?), new Member("灏忕�?))
            .forEach(member -> System.out.println("鍔犲�? " + member.getId()));
    }
}
```

---

## 馃摎 鏇村鍐呭

- [EST 椤圭洰涓婚〉](https://github.com/idcu/est)
- [EST Core](../est-core/README.md)
- [EST Collection](../est-collection/README.md)

---

**绁濅綘浣跨敤鎰夊揩锛?* 馃帀
