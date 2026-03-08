# EST Base 鍩虹妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?

## 鐩綍
1. [浠€涔堟槸 EST Base锛焆(#浠€涔堟槸-est-base)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Base锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Base 灏卞儚鏄竴涓?宸ュ叿绠?銆傛兂璞′竴涓嬩綘鍦ㄥ仛瀹跺姟锛岄渶瑕佸悇绉嶅伐鍏凤細铻轰笣鍒€銆佹壋鎵嬨€佸嵎灏恒€佸壀鍒€...

**浼犵粺鏂瑰紡**锛氭瘡娆￠渶瑕佷粈涔堝伐鍏烽兘鑷繁鍘讳拱锛屽緢楹荤儲銆?

**EST Base 鏂瑰紡**锛氱粰浣犱竴涓婊″父鐢ㄥ伐鍏风殑宸ュ叿绠憋紝閲岄潰鏈夛細
- 馃敡 **宸ュ叿闆?* - JSON銆乆ML銆乊AML銆両O 绛夊父鐢ㄥ伐鍏?
- 馃搻 **璁捐妯″紡** - 鍗曚緥銆佸伐鍘傘€佽瀵熻€呯瓑甯哥敤璁捐妯″紡
- 馃摝 **闆嗗悎妗嗘灦** - 鏇村己澶х殑闆嗗悎鎿嶄綔
- 馃И **娴嬭瘯鏀寔** - 鍗曞厓娴嬭瘯鍜屽熀鍑嗘祴璇?

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 寮€绠卞嵆鐢紝闆朵緷璧?
- 鈿?**楂樻€ц兘** - 绾?Java 瀹炵幇锛屾€ц兘浼樼
- 馃敀 **闆朵緷璧?* - 涓嶄緷璧栦换浣曠涓夋柟搴?
- 馃帹 **涓板瘜鍔熻兘** - 娑电洊甯哥敤宸ュ叿鍜岃璁℃ā寮?

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-util-common</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-collection</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓▼搴?

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.util.common.StringUtils;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Base 绗竴涓ず渚?===\n");
        
        System.out.println("--- 1. 浣跨敤宸ュ叿绫?---");
        String str = "  Hello, EST!  ";
        System.out.println("鍘熷瓧绗︿覆: '" + str + "'");
        System.out.println("鍘婚櫎绌虹櫧: '" + StringUtils.trim(str) + "'");
        System.out.println("鏄惁涓虹┖: " + StringUtils.isEmpty(str));
        
        System.out.println("\n--- 2. 浣跨敤闆嗗悎妗嗘灦 ---");
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> evenNumbers = numbers.where(n -> n % 2 == 0);
        System.out.println("鍋舵暟: " + evenNumbers.toList());
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Base 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Base 绗竴涓ず渚?===

--- 1. 浣跨敤宸ュ叿绫?---
鍘熷瓧绗︿覆: '  Hello, EST!  '
鍘婚櫎绌虹櫧: 'Hello, EST!'
鏄惁涓虹┖: false

--- 2. 浣跨敤闆嗗悎妗嗘灦 ---
鍋舵暟: [2, 4]

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Base 浜嗭紒
```

---

## 鍩虹绡?

### 1. est-utils 宸ュ叿妯″潡

EST Utils 鎻愪緵浜嗗父鐢ㄧ殑宸ュ叿绫伙紝璁╀綘涓嶇敤閲嶅閫犺疆瀛愩€?

#### est-util-common 甯哥敤宸ュ叿

```java
import ltd.idcu.est.util.common.StringUtils;
import ltd.idcu.est.util.common.NumberUtils;
import ltd.idcu.est.util.common.DateUtils;

// 瀛楃涓插伐鍏?
String str = "Hello, World!";
boolean isBlank = StringUtils.isBlank(str);
String reversed = StringUtils.reverse(str);

// 鏁板瓧宸ュ叿
String numStr = "123";
int num = NumberUtils.toInt(numStr, 0);
boolean isNumber = NumberUtils.isNumber(numStr);

// 鏃ユ湡宸ュ叿
String now = DateUtils.formatNow("yyyy-MM-dd HH:mm:ss");
```

#### est-util-format 鏍煎紡宸ュ叿

```java
import ltd.idcu.est.util.format.json.JsonUtils;
import ltd.idcu.est.util.format.xml.XmlUtils;
import ltd.idcu.est.util.format.yaml.YamlUtils;

// JSON 澶勭悊
String json = JsonUtils.toJson(user);
User user = JsonUtils.fromJson(json, User.class);

// XML 澶勭悊
String xml = XmlUtils.toXml(user);
User user = XmlUtils.fromXml(xml, User.class);

// YAML 澶勭悊
String yaml = YamlUtils.toYaml(config);
Config config = YamlUtils.fromYaml(yaml, Config.class);
```

#### est-util-io IO 宸ュ叿

```java
import ltd.idcu.est.util.io.FileUtils;
import ltd.idcu.est.util.io.IOUtils;

// 鏂囦欢鎿嶄綔
String content = FileUtils.readFileToString("data.txt");
FileUtils.writeStringToFile("output.txt", "Hello!");

// IO 娴佹搷浣?
InputStream is = ...;
String text = IOUtils.toString(is);
```

### 2. est-collection 闆嗗悎妯″潡

EST Collection 鎻愪緵浜嗘洿寮哄ぇ鐨勯泦鍚堟搷浣滐紝鍍忚璇濅竴鏍峰啓浠ｇ爜锛?

璇︾粏鏂囨。璇峰弬鑰冿細[est-collection README](./est-collection/README.md)

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

// 鍒涘缓搴忓垪
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙", "钁¤悇");

// 閾惧紡鎿嶄綔
Seq<String> result = fruits
    .where(fruit -> fruit.length() > 2)  // 绛涢€?
    .sorted()                               // 鎺掑簭
    .take(3);                               // 鍙栧墠3涓?

System.out.println(result.toList());  // [姗欏瓙, 钁¤悇, 棣欒晧]
```

### 3. est-patterns 璁捐妯″紡妯″潡

EST Patterns 鎻愪緵浜嗗父鐢ㄨ璁℃ā寮忕殑寮€绠卞嵆鐢ㄥ疄鐜般€?

璇︾粏鏂囨。璇峰弬鑰冿細[est-patterns README](./est-patterns/README.md)

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

// 鍗曚緥妯″紡
Singleton<DatabaseConnection> singleton = DefaultSingleton.of(DatabaseConnection::new);
DatabaseConnection conn1 = singleton.getInstance();
DatabaseConnection conn2 = singleton.getInstance();
System.out.println(conn1 == conn2);  // true
```

### 4. est-test 娴嬭瘯妯″潡

EST Test 鎻愪緵浜嗘祴璇曟敮鎸佸拰鍩哄噯娴嬭瘯銆?

璇︾粏鏂囨。璇峰弬鑰冿細[est-test README](./est-test/README.md)

```java
import ltd.idcu.est.test.api.Assertions;
import ltd.idcu.est.test.api.Tests;

// 鏂█
Assertions.assertEquals(2, 1 + 1);
Assertions.assertTrue(true);

// 娴嬭瘯杩愯
Tests.run(MyTestSuite.class);
```

---

## 杩涢樁绡?

### 1. 宸ュ叿妯″潡杩涢樁

#### 鑷畾涔夊伐鍏风被

浣犲彲浠ュ熀浜?EST Utils 鍒涘缓鑷繁鐨勫伐鍏风被锛?

```java
public class MyStringUtils {
    
    public static String capitalize(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
```

#### 閾惧紡宸ュ叿璋冪敤

```java
String result = StringUtils.trim(str)
    .toLowerCase()
    .replace(" ", "-");
```

### 2. 闆嗗悎妯″潡杩涢樁

璇︾粏鍐呭璇峰弬鑰冿細[est-collection 杩涢樁绡嘳(./est-collection/README.md)

### 3. 璁捐妯″紡杩涢樁

璇︾粏鍐呭璇峰弬鑰冿細[est-patterns 杩涢樁绡嘳(./est-patterns/README.md)

---

## 鏈€浣冲疄璺?

### 1. 浼樺厛浣跨敤宸ュ叿绫?

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄥ伐鍏风被
if (StringUtils.isBlank(str)) {
    // ...
}

// 鉂?涓嶆帹鑽愶細鑷繁鍐?
if (str == null || str.trim().isEmpty()) {
    // ...
}
```

### 2. 鍚堢悊浣跨敤闆嗗悎鎿嶄綔

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄩ摼寮忔搷浣?
List<String> result = users
    .where(User::isActive)
    .sortBy(User::getName)
    .pluck(User::getEmail)
    .toList();

// 鉂?涓嶆帹鑽愶細鎵嬪啓寰幆
List<String> result = new ArrayList<>();
for (User user : users) {
    if (user.isActive()) {
        result.add(user.getEmail());
    }
}
Collections.sort(result);
```

### 3. 涓嶈杩囧害璁捐

```java
// 鉁?绠€鍗曠洿鎺?
if (type == TYPE_A) {
    doA();
} else {
    doB();
}

// 鉂?杩囧害璁捐锛堥櫎闈炵‘瀹為渶瑕侊級
// 涓虹畝鍗曞満鏅娇鐢ㄥ鏉傜殑璁捐妯″紡
```

---

## 妯″潡缁撴瀯

```
est-base/
鈹溾攢鈹€ est-utils/              # 宸ュ叿妯″潡
鈹?  鈹溾攢鈹€ est-util-common/   # 甯哥敤宸ュ叿
鈹?  鈹溾攢鈹€ est-util-format/   # 鏍煎紡宸ュ叿锛圝SON銆乆ML銆乊AML锛?
鈹?  鈹斺攢鈹€ est-util-io/       # IO 宸ュ叿
鈹溾攢鈹€ est-patterns/           # 璁捐妯″紡妯″潡
鈹溾攢鈹€ est-collection/         # 闆嗗悎妗嗘灦妯″潡
鈹斺攢鈹€ est-test/               # 娴嬭瘯鏀寔妯″潡
```

---

## 鐩稿叧璧勬簮

- [est-utils README](./est-utils/README.md) - 宸ュ叿妯″潡璇︾粏鏂囨。
- [est-collection README](./est-collection/README.md) - 闆嗗悎妯″潡璇︾粏鏂囨。
- [est-patterns README](./est-patterns/README.md) - 璁捐妯″紡璇︾粏鏂囨。
- [est-test README](./est-test/README.md) - 娴嬭瘯妯″潡璇︾粏鏂囨。
- [EST Core](../est-core/README.md) - 鏍稿績妯″潡
- [绀轰緥浠ｇ爜](../est-examples/est-examples-basic/) - 绀轰緥浠ｇ爜

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
