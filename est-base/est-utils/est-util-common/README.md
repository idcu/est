# EST Utils Common - 閫氱敤宸ュ叿闆?

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Utils Common 鏄?EST 妗嗘灦鐨勯€氱敤宸ュ叿妯″潡锛屾彁渚涗簡涓€绯诲垪寮€绠卞嵆鐢ㄧ殑宸ュ叿绫伙紝甯姪浣犺В鍐虫棩甯稿紑鍙戜腑鐨勫悇绉嶅父瑙侀棶棰橈紝閬垮厤閲嶅閫犺疆瀛愩€?

---

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡囷細瀛楃涓插伐鍏穄(#鍩虹绡囧瓧绗︿覆宸ュ叿)
- [鍩虹绡囷細瀵硅薄宸ュ叿](#鍩虹绡囧璞″伐鍏?
- [鍩虹绡囷細鏂█宸ュ叿](#鍩虹绡囨柇瑷€宸ュ叿)
- [杩涢樁绡囷細鏁扮粍宸ュ叿](#杩涢樁绡囨暟缁勫伐鍏?
- [杩涢樁绡囷細鏁板€煎伐鍏穄(#杩涢樁绡囨暟鍊煎伐鍏?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸閫氱敤宸ュ叿锛?

鎯宠薄浣犲湪鍋氳彍锛屾瘡娆″仛楗兘闇€瑕佽彍鍒€銆佽彍鏉裤€侀攨纰楃摙鐩嗐€傝繖浜涘伐鍏蜂笉鏄彍鏈韩锛屼絾娌℃湁瀹冧滑浣犲緢闅惧仛鍑鸿彍鏉ャ€?

**EST Utils Common** 灏辨槸缂栫▼涓殑"鍘ㄦ埧宸ュ叿鍖?锛屽畠鎻愪緵浜嗭細
- 馃敧 **瀛楃涓插鐞?* - 鍍忓垏鑿滀竴鏍峰鐞嗘枃鏈?
- 馃嵆 **瀵硅薄鎿嶄綔** - 鍍忔憜鐩樹竴鏍锋暣鐞嗘暟鎹?
- 馃 **鏂█楠岃瘉** - 鍍忓皾鍜告贰涓€鏍锋鏌ュ弬鏁?
- 绛夌瓑...

### 5鍒嗛挓涓婃墜

#### 1. 娣诲姞渚濊禆

鍦ㄤ綘鐨?`pom.xml` 涓坊鍔狅細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2. 绗竴涓伐鍏蜂娇鐢?

璁╂垜浠敤 `StringUtils` 鏉ュ鐞嗗瓧绗︿覆锛?

```java
import ltd.idcu.est.utils.common.StringUtils;

public class FirstExample {
    public static void main(String[] args) {
        // 妫€鏌ュ瓧绗︿覆鏄惁涓虹┖
        String name = "  EST Framework  ";
        System.out.println("鏄惁涓虹┖: " + StringUtils.isEmpty(name));
        
        // 鍘婚櫎绌烘牸
        String trimmed = StringUtils.trim(name);
        System.out.println("鍘婚櫎绌烘牸: " + trimmed);
        
        // 棣栧瓧姣嶅ぇ鍐?
        String capitalized = StringUtils.capitalize(trimmed);
        System.out.println("棣栧瓧姣嶅ぇ鍐? " + capitalized);
    }
}
```

杩愯缁撴灉锛?
```
鏄惁涓虹┖: false
鍘婚櫎绌烘牸: EST Framework
棣栧瓧姣嶅ぇ鍐? Est Framework
```

鎭枩锛佷綘宸茬粡瀛︿細浣跨敤 EST 宸ュ叿浜嗭紒 馃帀

---

## 馃敯 鍩虹绡囷細瀛楃涓插伐鍏?

### 鐢熸椿绫绘瘮

瀛楃涓插氨鍍忎竴娈垫枃瀛椾俊鎭紝姣斿鐭俊銆侀偖浠跺唴瀹广€俙StringUtils` 灏辨槸甯綘澶勭悊杩欎簺鏂囧瓧鐨?绉樹功"锛屽府浣犳鏌ャ€佷慨鏀广€佹暣鐞嗘枃瀛椼€?

### 甯哥敤鍔熻兘

#### 1. 绌哄€兼鏌?

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringEmptyExample {
    public static void main(String[] args) {
        // isEmpty - 妫€鏌ユ槸鍚︿负 null 鎴?""
        System.out.println(StringUtils.isEmpty(null));      // true
        System.out.println(StringUtils.isEmpty(""));        // true
        System.out.println(StringUtils.isEmpty("  "));      // false (鏈夌┖鏍?
        
        // isBlank - 妫€鏌ユ槸鍚︿负 null銆?" 鎴栧叏鏄┖鏍?
        System.out.println(StringUtils.isBlank(null));       // true
        System.out.println(StringUtils.isBlank(""));         // true
        System.out.println(StringUtils.isBlank("  "));       // true
        System.out.println(StringUtils.isBlank("EST"));      // false
    }
}
```

**浠€涔堟椂鍊欑敤鍝釜锛?*
- `isEmpty`锛氫弗鏍兼鏌ワ紝鍙湁 null 鎴?"" 鎵嶇畻绌?
- `isBlank`锛氬鏉炬鏌ワ紝鍏ㄦ槸绌烘牸涔熺畻绌猴紙鏇村父鐢級

#### 2. 鍘婚櫎绌烘牸

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringTrimExample {
    public static void main(String[] args) {
        String str = "   Hello EST   ";
        
        // trim - 鍘婚櫎棣栧熬绌烘牸
        System.out.println(StringUtils.trim(str));                 // "Hello EST"
        
        // trimToNull - 鍘婚櫎绌烘牸鍚庡鏋滄槸绌哄垯杩斿洖 null
        System.out.println(StringUtils.trimToNull("   "));         // null
        
        // trimToEmpty - 鍘婚櫎绌烘牸鍚庡鏋滄槸绌哄垯杩斿洖 ""
        System.out.println(StringUtils.trimToEmpty(null));         // ""
    }
}
```

---

## 馃敯 鍩虹绡囷細瀵硅薄宸ュ叿

### 鐢熸椿绫绘瘮

瀵硅薄灏卞儚鍚勭鐗╁搧锛宍ObjectUtils` 灏辨槸甯綘妫€鏌ャ€佹瘮杈冦€佸鍒惰繖浜涚墿鍝佺殑"绠″"銆?

### 甯哥敤鍔熻兘

#### 1. 绌哄€兼鏌?

```java
import ltd.idcu.est.utils.common.ObjectUtils;
import java.util.ArrayList;
import java.util.List;

public class ObjectEmptyExample {
    public static void main(String[] args) {
        // isEmpty - 妫€鏌ュ璞℃槸鍚︿负绌猴紙鏀寔澶氱绫诲瀷锛?
        System.out.println(ObjectUtils.isEmpty(null));              // true
        System.out.println(ObjectUtils.isEmpty(""));                // true
        System.out.println(ObjectUtils.isEmpty(new Object[0]));     // true
        System.out.println(ObjectUtils.isEmpty(new ArrayList<>()));  // true
    }
}
```

---

## 馃敯 鍩虹绡囷細鏂█宸ュ叿

### 鐢熸椿绫绘瘮

鏂█灏卞儚"瀹夋"锛屽湪杩涘叆閲嶈鍖哄煙鍓嶆鏌ユ槸鍚︾鍚堣姹傘€俙AssertUtils` 甯姪浣犲湪浠ｇ爜鎵ц鍓嶉獙璇佸弬鏁帮紝鎻愬墠鍙戠幇闂銆?

### 甯哥敤鏂█

```java
import ltd.idcu.est.utils.common.AssertUtils;

public class AssertExample {
    public static void main(String[] args) {
        // notNull - 妫€鏌ヤ笉涓?null
        String name = "EST";
        AssertUtils.notNull(name, "鍚嶇О涓嶈兘涓虹┖");
        
        // isPositive - 妫€鏌ュ繀椤讳负姝ｆ暟
        int count = 10;
        AssertUtils.isPositive(count, "鏁伴噺蹇呴』澶т簬0");
    }
}
```

---

## 馃搱 杩涢樁绡囷細鏁扮粍宸ュ叿

### 鐢熸椿绫绘瘮

鏁扮粍灏卞儚涓€鎺掑偍鐗╂煖锛屾瘡涓煖瀛愭斁涓€涓笢瑗裤€俙ArrayUtils` 甯綘绠＄悊杩欎簺鍌ㄧ墿鏌溿€?

### 甯哥敤鍔熻兘

```java
import ltd.idcu.est.utils.common.ArrayUtils;
import java.util.Arrays;

public class ArrayExample {
    public static void main(String[] args) {
        String[] fruits = {"apple", "banana", "orange"};
        
        // isEmpty - 妫€鏌ユ暟缁勬槸鍚︿负绌?
        System.out.println(ArrayUtils.isEmpty(fruits));  // false
        
        // contains - 妫€鏌ユ槸鍚﹀寘鍚厓绱?
        System.out.println(ArrayUtils.contains(fruits, "banana"));  // true
        
        // add - 娣诲姞鍏冪礌
        String[] moreFruits = ArrayUtils.add(fruits, "grape");
        System.out.println(Arrays.toString(moreFruits));  // [apple, banana, orange, grape]
    }
}
```

---

## 馃搱 杩涢樁绡囷細鏁板€煎伐鍏?

### 鐢熸椿绫绘瘮

鏁板€煎氨鍍忔暟瀛﹂涓殑鏁板瓧锛宍NumberUtils` 灏辨槸浣犵殑"璁＄畻鍣?锛屽府浣犺浆鎹€佽绠椼€侀獙璇佹暟瀛椼€?

### 甯哥敤鍔熻兘

```java
import ltd.idcu.est.utils.common.NumberUtils;

public class NumberExample {
    public static void main(String[] args) {
        // 瀛楃涓茶浆鏁板瓧锛堝甫榛樿鍊硷級
        String str = "123";
        int num = NumberUtils.toInt(str, 0);
        System.out.println(num);  // 123
        
        // 瀹夊叏杞崲
        String invalid = "abc";
        int safeNum = NumberUtils.toInt(invalid, 0);
        System.out.println(safeNum);  // 0
        
        // 姹傛渶澶ф渶灏忓€?
        int[] scores = {85, 92, 78, 90};
        System.out.println(NumberUtils.max(scores));  // 92
        System.out.println(NumberUtils.min(scores));  // 78
    }
}
```

---

## 鉁?鏈€浣冲疄璺?

### 1. 浼樺厛浣跨敤 `isBlank` 鑰屼笉鏄?`isEmpty`

```java
// 鉁?鎺ㄨ崘
if (StringUtils.isBlank(username)) {
    // 澶勭悊绌哄€?
}

// 鉂?涓嶆帹鑽愶紙闇€瑕侀澶栧鐞嗙┖鏍硷級
if (StringUtils.isEmpty(username) || username.trim().isEmpty()) {
    // 澶勭悊绌哄€?
}
```

### 2. 浣跨敤鏂█鎻愬墠楠岃瘉鍙傛暟

```java
// 鉁?鎺ㄨ崘
public void process(String data, int count) {
    AssertUtils.hasText(data, "鏁版嵁涓嶈兘涓虹┖");
    AssertUtils.isPositive(count, "鏁伴噺蹇呴』澶т簬0");
    // 涓氬姟閫昏緫
}
```

### 3. 浣跨敤榛樿鍊奸伩鍏嶇┖鎸囬拡

```java
// 鉁?鎺ㄨ崘
String name = ObjectUtils.defaultIfNull(user.getName(), "鏈煡");

// 鉂?涓嶆帹鑽?
String name = user.getName() != null ? user.getName() : "鏈煡";
```

---

## 馃摝 妯″潡闆嗘垚

### 涓?est-collection 闆嗘垚

```java
import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        Seqs.of("apple", "banana", "cherry")
            .filter(StringUtils::isNotBlank)
            .map(StringUtils::capitalize)
            .forEach(System.out::println);
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
