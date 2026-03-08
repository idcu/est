# EST Collection 闆嗗悎妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?

## 鐩綍
1. [浠€涔堟槸 EST Collection锛焆(#浠€涔堟槸-est-collection)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡囷細鍒涘缓鍜屾搷浣滃簭鍒梋(#鍩虹绡囧垱寤哄拰鎿嶄綔搴忓垪)
4. [杩涢樁绡囷細甯哥敤鎿嶄綔璇﹁В](#杩涢樁绡囧父鐢ㄦ搷浣滆瑙?
5. [楂樼骇绡囷細澶嶆潅鍦烘櫙瀹炴垬](#楂樼骇绡囧鏉傚満鏅疄鎴?
6. [鎬ц兘涓庢渶浣冲疄璺礭(#鎬ц兘涓庢渶浣冲疄璺?

---

## 浠€涔堟槸 EST Collection锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

鎯宠薄涓€涓嬶紝浣犳湁涓€鍫嗘暟鎹渶瑕佸鐞嗭紝姣斿锛?
- 浠?00涓敤鎴烽噷鎵惧嚭骞撮緞澶т簬18宀佺殑
- 缁欒繖浜涚敤鎴锋寜骞撮緞鎺掑簭
- 鍙彇鍓?0涓?
- 鐒跺悗鎻愬彇浠栦滑鐨勯偖绠卞湴鍧€

**浼犵粺鐨勫仛娉?*锛氫綘闇€瑕佸啓寰堝寰幆銆佷复鏃跺彉閲忥紝浠ｇ爜鍙堥暱鍙堥毦鐪嬨€?

**鐢?EST Collection**锛氬氨鍍忔祦姘寸嚎涓€鏍凤紝涓€姝ユ帴涓€姝ワ紝浠ｇ爜绠€娲佷紭闆咃紒

```java
// 浼橀泤鐨勯摼寮忔搷浣?
List<String> emails = users
    .where(user -> user.getAge() > 18)    // 绛涢€夋垚骞翠汉
    .sortBy(User::getAge)                   // 鎸夊勾榫勬帓搴?
    .take(10)                                // 鍙栧墠10涓?
    .pluck(User::getEmail)                  // 鎻愬彇閭
    .toList();                               // 杞垚 List
```

### 鏍稿績鐗圭偣
- 馃幆 **鍍忚璇濅竴鏍峰啓浠ｇ爜**锛歚where`锛堢瓫閫夛級銆乣sortBy`锛堟帓搴忥級銆乣take`锛堝彇鍓峃涓級
- 鈿?**鍩轰簬 Java Stream**锛氭€ц兘浼樼锛孞ava 鍘熺敓鏀寔
- 馃敀 **榛樿涓嶅彲鍙?*锛氬畨鍏紝涓嶄細鎰忓淇敼鏁版嵁
- 馃帹 **閾惧紡璋冪敤**锛氫唬鐮佹祦鐣咃紝鍙鎬у己

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?`pom.xml` 涓坊鍔狅細

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 绗簩姝ワ細浣犵殑绗竴涓▼搴?

鍒涘缓涓€涓畝鍗曠殑 Java 鏂囦欢锛?

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class FirstExample {
    public static void main(String[] args) {
        // 1. 鍒涘缓涓€涓簭鍒楋紙搴忓垪灏辨槸涓€缁勬暟鎹級
        Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙", "钁¤悇", "瑗跨摐");
        
        // 2. 鍋氫竴浜涙搷浣?
        Seq<String> result = fruits
            .filter(fruit -> fruit.length() > 2)  // 绛涢€夊悕瀛楅暱搴﹀ぇ浜?鐨?
            .sorted()                               // 鎺掑簭
            .take(3);                               // 鍙彇鍓?涓?
        
        // 3. 杈撳嚭缁撴灉
        System.out.println("绛涢€夌粨鏋滐細" + result.toList());
    }
}
```

杩愯涓€涓嬶紝浣犱細鐪嬪埌锛?
```
绛涢€夌粨鏋滐細[姗欏瓙, 瑗跨摐, 钁¤悇]
```

鎭枩锛佷綘宸茬粡浼氱敤 EST Collection 浜嗭紒馃帀

---

## 鍩虹绡囷細鍒涘缓鍜屾搷浣滃簭鍒?

### 3.1 浠€涔堟槸搴忓垪锛圫eq锛夛紵

**搴忓垪**灏辨槸涓€缁勬湁搴忕殑鏁版嵁锛屽氨鍍忎竴鎺掓帓闃熺殑浜恒€?

鍦?EST Collection 涓紝`Seq<T>` 鏄牳蹇冩帴鍙ｏ紝`T` 琛ㄧず搴忓垪閲岃鐨勬槸浠€涔堢被鍨嬬殑鏁版嵁銆?

### 3.2 鍒涘缓搴忓垪鐨?7 绉嶆柟娉?

#### 鏂规硶1锛氱洿鎺ュ垱寤猴紙鏈€甯哥敤锛?

```java
// 鐢?of() 鏂规硶锛岀洿鎺ユ妸鏁版嵁鏀捐繘鍘?
Seq<String> seq1 = Seqs.of("a", "b", "c");
Seq<Integer> seq2 = Seqs.of(1, 2, 3, 4, 5);
```

#### 鏂规硶2锛氫粠 List 鍒涘缓

```java
import java.util.Arrays;
import java.util.List;

List<String> list = Arrays.asList("x", "y", "z");
Seq<String> seq = Seqs.from(list);
```

#### 鏂规硶3锛氬垱寤虹┖搴忓垪

```java
Seq<String> emptySeq = Seqs.empty();
```

#### 鏂规硶4锛氬垱寤烘暟瀛楄寖鍥?

```java
// 浠?1 鍒?10锛堜笉鍖呭惈10锛?
Seq<Integer> range1 = Seqs.range(1, 10);      // [1, 2, 3, 4, 5, 6, 7, 8, 9]

// 浠?1 鍒?10锛屾闀夸负 2
Seq<Integer> range2 = Seqs.range(1, 10, 2);   // [1, 3, 5, 7, 9]
```

#### 鏂规硶5锛氱敓鎴愬簭鍒?

```java
import java.util.Random;

// 鐢熸垚 5 涓殢鏈烘暟
Seq<Double> randoms = Seqs.generate(5, Math::random);

// 鐢熸垚 10 涓?0-100 鐨勯殢鏈烘暣鏁?
Random random = new Random();
Seq<Integer> randomInts = Seqs.generate(10, () -> random.nextInt(100));
```

#### 鏂规硶6锛氶噸澶嶅厓绱?

```java
// 閲嶅 "hello" 5 娆?
Seq<String> repeated = Seqs.repeat("hello", 5);  // ["hello", "hello", "hello", "hello", "hello"]
```

#### 鏂规硶7锛氫粠 Stream 鍒涘缓

```java
import java.util.stream.Stream;

Stream<String> stream = Stream.of("a", "b", "c");
Seq<String> seq = Seqs.from(stream);
```

### 3.3 搴忓垪鐨勫熀鏈俊鎭?

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙");

// 鑾峰彇鍏冪礌涓暟
int size = fruits.size();  // 3

// 鍒ゆ柇鏄惁涓虹┖
boolean isEmpty = fruits.isEmpty();  // false
boolean isNotEmpty = fruits.isNotEmpty();  // true

// 鍒ゆ柇鏄惁鍖呭惈鏌愪釜鍏冪礌
boolean hasApple = fruits.contains("鑻规灉");  // true
boolean hasGrape = fruits.contains("钁¤悇");  // false
```

### 3.4 鑾峰彇搴忓垪涓殑鍏冪礌

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙", "钁¤悇", "瑗跨摐");

// 鑾峰彇绗竴涓厓绱?
String first = fruits.firstOrNull();  // "鑻规灉"
String firstOrDefault = fruits.firstOr("榛樿鍊?);  // "鑻规灉"

// 鑾峰彇鏈€鍚庝竴涓厓绱?
String last = fruits.lastOrNull();  // "瑗跨摐"

// 鏍规嵁绱㈠紩鑾峰彇锛堢储寮曚粠 0 寮€濮嬶級
String second = fruits.get(1);  // "棣欒晧"
String fifth = fruits.elementAt(4);  // "瑗跨摐"

// 瀹夊叏鑾峰彇锛堢储寮曡秺鐣屾椂杩斿洖 null锛?
String outOfBounds = fruits.elementAtOrNull(10);  // null
```

---

## 杩涢樁绡囷細甯哥敤鎿嶄綔璇﹁В

### 4.1 绛涢€夋搷浣滐細鍙暀涓嬩綘鎯宠鐨?

#### filter / where锛氭寜鏉′欢绛涢€?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 鐣欎笅鍋舵暟
Seq<Integer> evenNumbers = numbers.filter(n -> n % 2 == 0);
// 鎴栬€呯敤 where锛堟晥鏋滀竴鏍凤紝鍐欐硶鏇寸洿瑙傦級
Seq<Integer> evenNumbers2 = numbers.where(n -> n % 2 == 0);

// 鐣欎笅澶т簬 5 鐨勬暟
Seq<Integer> greaterThan5 = numbers.where(n -> n > 5);

System.out.println(evenNumbers.toList());  // [2, 4, 6, 8, 10]
System.out.println(greaterThan5.toList());  // [6, 7, 8, 9, 10]
```

#### filterNot / whereNot锛氭帓闄ょ鍚堟潯浠剁殑

```java
// 鎺掗櫎鍋舵暟锛岀暀涓嬪鏁?
Seq<Integer> oddNumbers = numbers.filterNot(n -> n % 2 == 0);
// 鎴栬€?
Seq<Integer> oddNumbers2 = numbers.whereNot(n -> n % 2 == 0);

System.out.println(oddNumbers.toList());  // [1, 3, 5, 7, 9]
```

#### distinct锛氬幓閲?

```java
Seq<String> words = Seqs.of("a", "b", "a", "c", "b", "d");
Seq<String> uniqueWords = words.distinct();

System.out.println(uniqueWords.toList());  // [a, b, c, d]
```

#### distinctBy锛氭寜鏌愪釜灞炴€у幓閲?

```java
// 鍋囪鏈変竴涓?User 绫?
class User {
    private String name;
    private String email;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
}

Seq<User> users = Seqs.of(
    new User("寮犱笁", "zhangsan@example.com"),
    new User("鏉庡洓", "lisi@example.com"),
    new User("寮犱笁", "zhangsan@example.com"),  // 閲嶅鐨勯偖绠?
    new User("鐜嬩簲", "wangwu@example.com")
);

// 鎸夐偖绠卞幓閲?
Seq<User> uniqueUsers = users.distinctBy(User::getEmail);
```

### 4.2 鏄犲皠鎿嶄綔锛氭妸鏁版嵁鍙樻垚鍙︿竴绉嶆牱瀛?

#### map / pluck锛氳浆鎹㈡瘡涓厓绱?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 姣忎釜鏁颁箻浠?2
Seq<Integer> doubled = numbers.map(n -> n * 2);
System.out.println(doubled.toList());  // [2, 4, 6, 8, 10]

// 鏁板瓧杞瓧绗︿覆
Seq<String> asStrings = numbers.map(n -> "鏁板瓧锛? + n);
System.out.println(asStrings.toList());  // [鏁板瓧锛?, 鏁板瓧锛?, 鏁板瓧锛?, 鏁板瓧锛?, 鏁板瓧锛?]

// pluck 鏄?map 鐨勫埆鍚嶏紝鐢ㄤ簬鎻愬彇瀵硅薄灞炴€э紙鏇寸洿瑙傦級
Seq<User> users = Seqs.of(
    new User("寮犱笁", "zhangsan@example.com"),
    new User("鏉庡洓", "lisi@example.com")
);
Seq<String> names = users.pluck(User::getName);
System.out.println(names.toList());  // [寮犱笁, 鏉庡洓]
```

#### mapIndexed锛氬甫绱㈠紩鐨勬槧灏?

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙");

// 绱㈠紩浠?0 寮€濮?
Seq<String> indexed = fruits.mapIndexed((index, fruit) -> index + ": " + fruit);
System.out.println(indexed.toList());  // [0: 鑻规灉, 1: 棣欒晧, 2: 姗欏瓙]
```

### 4.3 鎺掑簭鎿嶄綔锛氳鏁版嵁浜曠劧鏈夊簭

#### sorted锛氳嚜鐒舵帓搴?

```java
Seq<Integer> numbers = Seqs.of(5, 2, 8, 1, 9);
Seq<Integer> sorted = numbers.sorted();
System.out.println(sorted.toList());  // [1, 2, 5, 8, 9]

Seq<String> words = Seqs.of("banana", "apple", "cherry");
Seq<String> sortedWords = words.sorted();
System.out.println(sortedWords.toList());  // [apple, banana, cherry]
```

#### sortBy锛氭寜鏌愪釜灞炴€ф帓搴?

```java
Seq<User> users = Seqs.of(
    new User("寮犱笁", "zhangsan@example.com", 25),
    new User("鏉庡洓", "lisi@example.com", 18),
    new User("鐜嬩簲", "wangwu@example.com", 30)
);

// 鎸夊勾榫勪粠灏忓埌澶ф帓搴?
Seq<User> sortedByAge = users.sortBy(User::getAge);

// 鎸夊鍚嶆帓搴?
Seq<User> sortedByName = users.sortBy(User::getName);
```

#### sortByDesc锛氭寜鏌愪釜灞炴€ч檷搴忔帓搴?

```java
// 鎸夊勾榫勪粠澶у埌灏忔帓搴?
Seq<User> sortedByAgeDesc = users.sortByDesc(User::getAge);
```

#### reversed锛氬弽杞『搴?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
Seq<Integer> reversed = numbers.reversed();
System.out.println(reversed.toList());  // [5, 4, 3, 2, 1]
```

#### shuffled锛氶殢鏈烘墦涔?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
Seq<Integer> shuffled = numbers.shuffled();
System.out.println(shuffled.toList());  // 闅忔満椤哄簭锛屾瘮濡?[3, 1, 5, 2, 4]
```

### 4.4 鎴彇鎿嶄綔锛氬彧鍙栭儴鍒嗘暟鎹?

#### take锛氬彇鍓?N 涓?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 鍙栧墠 3 涓?
Seq<Integer> first3 = numbers.take(3);
System.out.println(first3.toList());  // [1, 2, 3]
```

#### takeWhile锛氫竴鐩村彇锛岀洿鍒版潯浠朵笉婊¤冻

```java
// 鍙栧皬浜?5 鐨勬暟
Seq<Integer> lessThan5 = numbers.takeWhile(n -> n < 5);
System.out.println(lessThan5.toList());  // [1, 2, 3, 4]
```

#### takeLast锛氬彇鍚?N 涓?

```java
// 鍙栧悗 3 涓?
Seq<Integer> last3 = numbers.takeLast(3);
System.out.println(last3.toList());  // [8, 9, 10]
```

#### drop锛氳烦杩囧墠 N 涓?

```java
// 璺宠繃鍓?3 涓?
Seq<Integer> dropped3 = numbers.drop(3);
System.out.println(dropped3.toList());  // [4, 5, 6, 7, 8, 9, 10]
```

#### dropWhile锛氫竴鐩磋烦杩囷紝鐩村埌鏉′欢涓嶆弧瓒?

```java
// 璺宠繃灏忎簬 5 鐨勬暟
Seq<Integer> from5 = numbers.dropWhile(n -> n < 5);
System.out.println(from5.toList());  // [5, 6, 7, 8, 9, 10]
```

#### slice锛氬垏鐗囷紙鎸囧畾鑼冨洿锛?

```java
// 浠庣储寮?2 鍒?5锛堜笉鍖呭惈 5锛?
Seq<Integer> slice = numbers.slice(2, 5);
System.out.println(slice.toList());  // [3, 4, 5]
```

### 4.5 鏌ユ壘涓庡尮閰嶏細鍒ゆ柇鏁版嵁鏄惁绗﹀悎瑕佹眰

#### 鏌ユ壘鍏冪礌

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 鎵剧涓€涓ぇ浜?5 鐨勬暟
Integer firstGreaterThan5 = numbers.first(n -> n > 5).orElse(null);  // 6

// 鎵炬渶鍚庝竴涓皬浜?5 鐨勬暟
Integer lastLessThan5 = numbers.last(n -> n < 5).orElse(null);  // 4

// 闅忔満鍙栦竴涓?
Integer random = numbers.random().orElse(null);  // 闅忔満涓€涓暟

// 闅忔満鍙?3 涓?
Seq<Integer> sample = numbers.sample(3);  // 闅忔満 3 涓暟
```

#### 鍖归厤鍒ゆ柇

```java
// 鏄惁鎵€鏈夋暟閮藉ぇ浜?0锛?
boolean allPositive = numbers.all(n -> n > 0);  // true

// 鏄惁鏈夊ぇ浜?10 鐨勬暟锛?
boolean anyGreaterThan10 = numbers.any(n -> n > 10);  // false

// 鏄惁娌℃湁璐熸暟锛?
boolean noneNegative = numbers.none(n -> n < 0);  // true
```

#### 璁℃暟

```java
// 鎬诲叡鏈夊灏戜釜鍏冪礌
int total = numbers.count();  // 10

// 鏈夊灏戜釜鍋舵暟
int evenCount = numbers.count(n -> n % 2 == 0);  // 5
```

### 4.6 杈撳嚭杞崲锛氭妸搴忓垪杞垚鍏朵粬鏍煎紡

#### 杞垚 List

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙");
List<String> list = fruits.toList();
```

#### 杞垚 Set

```java
Set<String> set = fruits.toSet();
```

#### 杩炴帴鎴愬瓧绗︿覆

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙");

// 鐩存帴杩炴帴
String joined1 = fruits.joinToString();  // "鑻规灉棣欒晧姗欏瓙"

// 鐢ㄩ€楀彿鍒嗛殧
String joined2 = fruits.joinToString(", ");  // "鑻规灉, 棣欒晧, 姗欏瓙"

// 甯﹀墠缂€鍜屽悗缂€
String joined3 = fruits.joinToString(", ", "[", "]");  // "[鑻规灉, 棣欒晧, 姗欏瓙]"
```

#### 杞垚 Stream

```java
// 鍙互浣跨敤 Java 鍘熺敓 Stream API
Stream<String> stream = fruits.stream();
Stream<String> parallelStream = fruits.parallelStream();
```

---

## 楂樼骇绡囷細澶嶆潅鍦烘櫙瀹炴垬

### 5.1 鑱氬悎鎿嶄綔锛氭妸鏁版嵁鍚堝苟璧锋潵

#### reduce / fold锛氱疮绉搷浣?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 姹傚拰
Integer sum = numbers.reduce(0, (a, b) -> a + b);  // 15

// 鎴栬€呯敤 fold锛堟晥鏋滀竴鏍凤級
Integer sum2 = numbers.fold(0, (a, b) -> a + b);  // 15

// 姹備箻绉?
Integer product = numbers.reduce(1, (a, b) -> a * b);  // 120

// 鎵惧嚭鏈€澶у€?
Integer max = numbers.reduce(Integer::max).orElse(null);  // 5
```

#### groupBy锛氬垎缁?

```java
Seq<User> users = Seqs.of(
    new User("寮犱笁", "鍖椾含", 25),
    new User("鏉庡洓", "涓婃捣", 18),
    new User("鐜嬩簲", "鍖椾含", 30),
    new User("璧靛叚", "涓婃捣", 22),
    new User("閽变竷", "骞垮窞", 28)
);

// 鎸夊煄甯傚垎缁?
Map<String, Seq<User>> byCity = users.groupBy(User::getCity);

// 鏌ョ湅缁撴灉
byCity.forEach((city, cityUsers) -> {
    System.out.println(city + ": " + cityUsers.pluck(User::getName).toList());
});
// 杈撳嚭锛?
// 鍖椾含: [寮犱笁, 鐜嬩簲]
// 涓婃捣: [鏉庡洓, 璧靛叚]
// 骞垮窞: [閽变竷]
```

#### keyBy / associateBy锛氳浆鎴?Map

```java
// 鎸夐偖绠变綔涓?key锛孶ser 瀵硅薄浣滀负 value
Map<String, User> byEmail = users.keyBy(User::getEmail);

// 鎸夐偖绠变綔涓?key锛屽鍚嶄綔涓?value
Map<String, String> emailToName = users.associateBy(User::getEmail, User::getName);
```

#### countBy锛氭寜鏌愪釜灞炴€ц鏁?

```java
// 缁熻姣忎釜鍩庡競鏈夊灏戜汉
Map<String, Long> countByCity = users.countBy(User::getCity);
System.out.println(countByCity);  // {鍖椾含=2, 涓婃捣=2, 骞垮窞=1}
```

#### minBy / maxBy锛氭壘鏈€鍊?

```java
// 鎵惧勾榫勬渶灏忕殑鐢ㄦ埛
User youngest = users.minBy(User::getAge).orElse(null);

// 鎵惧勾榫勬渶澶х殑鐢ㄦ埛
User oldest = users.maxBy(User::getAge).orElse(null);
```

### 5.2 闆嗗悎鎿嶄綔锛氬涓簭鍒椾箣闂寸殑杩愮畻

```java
Seq<Integer> a = Seqs.of(1, 2, 3, 4);
Seq<Integer> b = Seqs.of(3, 4, 5, 6);

// 娣诲姞鍏冪礌
Seq<Integer> plus = a.plus(5);  // [1, 2, 3, 4, 5]
Seq<Integer> plusAll = a.plusAll(b);  // [1, 2, 3, 4, 3, 4, 5, 6]

// 绉婚櫎鍏冪礌
Seq<Integer> minus = a.minus(2);  // [1, 3, 4]
Seq<Integer> minusAll = a.minusAll(b);  // [1, 2]

// 浜ら泦锛堜袱涓簭鍒楅兘鏈夌殑鍏冪礌锛?
Seq<Integer> intersect = a.intersect(b);  // [3, 4]

// 骞堕泦锛堝幓閲嶅悎骞讹級
Seq<Integer> union = a.union(b);  // [1, 2, 3, 4, 5, 6]

// 宸泦锛堝湪 a 涓絾涓嶅湪 b 涓殑鍏冪礌锛?
Seq<Integer> diff = a.diff(b);  // [1, 2]
```

### 5.3 楂樼骇鎿嶄綔

#### partition锛氬垎鍖猴紙鍒嗘垚涓ら儴鍒嗭級

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 鍒嗘垚鍋舵暟鍜屽鏁颁袱閮ㄥ垎
Pair<Seq<Integer>, Seq<Integer>> partitioned = numbers.partition(n -> n % 2 == 0);

Seq<Integer> evens = partitioned.getFirst();  // 鍋舵暟
Seq<Integer> odds = partitioned.getSecond();  // 濂囨暟
```

#### zip锛氭媺閾炬搷浣滐紙涓や釜搴忓垪鍚堝苟锛?

```java
Seq<String> letters = Seqs.of("a", "b", "c");
Seq<Integer> numbers = Seqs.of(1, 2, 3);

// 鍚堝苟鎴?Pair 搴忓垪
Seq<Pair<String, Integer>> zipped = letters.zip(numbers);

zipped.forEach(pair -> {
    System.out.println(pair.getFirst() + " -> " + pair.getSecond());
});
// 杈撳嚭锛?
// a -> 1
// b -> 2
// c -> 3
```

#### zipWithIndex锛氬甫绱㈠紩鐨勬媺閾?

```java
Seq<String> fruits = Seqs.of("鑻规灉", "棣欒晧", "姗欏瓙");
Seq<Pair<Integer, String>> indexed = fruits.zipWithIndex();

indexed.forEach(pair -> {
    System.out.println(pair.getFirst() + ": " + pair.getSecond());
});
// 杈撳嚭锛?
// 0: 鑻规灉
// 1: 棣欒晧
// 2: 姗欏瓙
```

#### chunked / chunk锛氬垎鍧?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 姣?3 涓垎鎴愪竴鍧?
Seq<List<Integer>> chunks = numbers.chunked(3);

chunks.forEach(chunk -> {
    System.out.println(chunk);
});
// 杈撳嚭锛?
// [1, 2, 3]
// [4, 5, 6]
// [7, 8, 9]
// [10]
```

#### windowed锛氭粦鍔ㄧ獥鍙?

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 绐楀彛澶у皬涓?3锛屾闀夸负 1
Seq<List<Integer>> windows = numbers.windowed(3);

windows.forEach(window -> {
    System.out.println(window);
});
// 杈撳嚭锛?
// [1, 2, 3]
// [2, 3, 4]
// [3, 4, 5]
```

### 5.4 瀹屾暣瀹炴垬妗堜緥

#### 妗堜緥1锛氱數鍟嗘暟鎹鐞?

鍋囪鎴戜滑鏈変竴涓數鍟嗚鍗曟暟鎹紝闇€瑕佸仛浠ヤ笅澶勭悊锛?
1. 绛涢€夊嚭宸叉敮浠樼殑璁㈠崟
2. 绛涢€夊嚭閲戦澶т簬 100 鍏冪殑璁㈠崟
3. 鎸夐噾棰濅粠澶у埌灏忔帓搴?
4. 鍙彇鍓?10 涓鍗?
5. 鎻愬彇璁㈠崟鍙峰拰閲戦
6. 璁＄畻鎬婚噾棰?

```java
class Order {
    private String orderId;
    private double amount;
    private boolean paid;
    private String userId;
    
    public Order(String orderId, double amount, boolean paid, String userId) {
        this.orderId = orderId;
        this.amount = amount;
        this.paid = paid;
        this.userId = userId;
    }
    
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }
    public String getUserId() { return userId; }
}

Seq<Order> orders = Seqs.of(
    new Order("ORDER001", 150.0, true, "USER001"),
    new Order("ORDER002", 80.0, true, "USER002"),
    new Order("ORDER003", 200.0, false, "USER003"),
    new Order("ORDER004", 300.0, true, "USER004"),
    new Order("ORDER005", 50.0, true, "USER005"),
    new Order("ORDER006", 180.0, true, "USER006")
);

// 閾惧紡澶勭悊
Seq<Pair<String, Double>> result = orders
    .where(Order::isPaid)                    // 宸叉敮浠?
    .where(order -> order.getAmount() > 100) // 閲戦 > 100
    .sortByDesc(Order::getAmount)             // 鎸夐噾棰濋檷搴?
    .take(10)                                  // 鍙栧墠10涓?
    .map(order -> new Pair<>(order.getOrderId(), order.getAmount()));  // 鎻愬彇璁㈠崟鍙峰拰閲戦

// 璁＄畻鎬婚噾棰?
double totalAmount = orders
    .where(Order::isPaid)
    .where(order -> order.getAmount() > 100)
    .fold(0.0, (sum, order) -> sum + order.getAmount());

System.out.println("绗﹀悎鏉′欢鐨勮鍗曪細");
result.forEach(pair -> {
    System.out.println(pair.getFirst() + ": 楼" + pair.getSecond());
});
System.out.println("鎬婚噾棰濓細楼" + totalAmount);
```

---

## 鎬ц兘涓庢渶浣冲疄璺?

### 6.1 鎬ц兘鐗圭偣

- 鉁?**鍩轰簬 Java Stream API**锛氬厖鍒嗗埄鐢?JVM 鐨勪紭鍖栵紝鎬ц兘浼樼
- 鉁?**鎳掑姞杞?*锛氫腑闂存搷浣滀笉浼氱珛鍗虫墽琛岋紝鐩村埌缁堢鎿嶄綔鎵嶄細璁＄畻
- 鉁?**涓嶅彲鍙樹紭鍏?*锛氶粯璁や笉鍙彉锛岄伩鍏嶅壇浣滅敤锛岀嚎绋嬪畨鍏?

### 6.2 鏈€浣冲疄璺?

#### 1. 浼樺厛浣跨敤涓嶅彲鍙?Seq

```java
// 鎺ㄨ崘锛氫娇鐢ㄤ笉鍙彉 Seq
Seq<String> seq = Seqs.of("a", "b", "c");

// 涓嶆帹鑽愶細闄ら潪纭疄闇€瑕佷慨鏀?
MutableSeq<String> mutable = seq.mutable();
```

#### 2. 鐏垫椿鍒囨崲

```java
Seq<User> users = ...;

// 闇€瑕佷慨鏀规椂锛岃幏鍙栧彲鍙樼増鏈?
MutableSeq<User> mutable = users.mutable();
mutable.add(newUser);

// 淇敼瀹屽悗锛屽彲浠ヨ浆鍥炰笉鍙彉
Seq<User> immutable = mutable.immutable();
```

#### 3. 娣峰悎浣跨敤 Stream API

```java
Seq<String> seq = Seqs.of("a", "b", "c");

// 鍙互闅忔椂杞垚 Stream 浣跨敤 Java 鍘熺敓 API
List<String> result = seq.stream()
    .filter(s -> s.length() > 0)
    .collect(Collectors.toList());
```

#### 4. 浣跨敤鐩磋鐨勫埆鍚?

```java
// 鎺ㄨ崘锛氫娇鐢?where銆乸luck銆乻ortBy 绛夋洿鐩磋鐨勫埆鍚?
Seq<String> result = users
    .where(User::isActive)
    .sortBy(User::getName)
    .pluck(User::getEmail);

// 涔熷彲浠ョ敤 filter銆乵ap銆乻orted
Seq<String> result2 = users
    .filter(User::isActive)
    .sorted(Comparator.comparing(User::getName))
    .map(User::getEmail);
```

### 6.3 甯歌闂

#### Q: 鍜?Java Stream 鏈変粈涔堝尯鍒紵

A: EST Collection 鍩轰簬 Java Stream锛屼絾鎻愪緵浜嗘洿鍙嬪ソ鐨?API锛?
- 鏇寸洿瑙傜殑鏂规硶鍚嶏紙where銆乸luck銆乻ortBy锛?
- 榛樿涓嶅彲鍙橈紝鏇村畨鍏?
- 鏇翠赴瀵岀殑鎿嶄綔锛坈hunked銆亀indowed銆亃ip 绛夛級
- 鍙互闅忔椂鍜?Stream 浜掔浉杞崲

#### Q: 鎬ц兘鎬庝箞鏍凤紵

A: 鐢变簬鍩轰簬 Java Stream锛屾€ц兘鍜?Stream 鐩稿綋銆傚湪澶у鏁板満鏅笅锛屾€ц兘閮借冻澶熷ソ銆?

#### Q: 绾跨▼瀹夊叏鍚楋紵

A: 榛樿鐨?Seq 鏄笉鍙彉鐨勶紝鎵€浠ユ槸绾跨▼瀹夊叏鐨勩€侻utableSeq 涓嶆槸绾跨▼瀹夊叏鐨勶紝濡傛灉鍦ㄥ绾跨▼鐜涓嬩娇鐢紝闇€瑕佽嚜宸卞鐞嗗悓姝ャ€?

---

## 鐩稿叧璧勬簮

- [API 鏂囨。](../../docs/api/collection/)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-basic/)
- [瀹屾暣娴嬭瘯鐢ㄤ緥](./est-collection-impl/src/test/java/)

---

## 鎬荤粨

鎭枩浣狅紒浣犲凡缁忔帉鎻′簡 EST Collection 鐨勬牳蹇冪敤娉曪紒

- 馃摎 **鍩虹绡?*锛氬垱寤哄簭鍒椼€佸熀鏈搷浣?
- 馃幆 **杩涢樁绡?*锛氱瓫閫夈€佹槧灏勩€佹帓搴忋€佹埅鍙?
- 馃殌 **楂樼骇绡?*锛氳仛鍚堛€侀泦鍚堣繍绠椼€佸疄鎴樻渚?
- 馃挕 **鏈€浣冲疄璺?*锛氭€ц兘浼樺寲銆佷娇鐢ㄦ妧宸?

鐜板湪锛屽幓鐢?EST Collection 璁╀綘鐨勪唬鐮佹洿浼橀泤鍚э紒馃帀

