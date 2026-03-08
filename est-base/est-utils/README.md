# EST Utils 宸ュ叿妯″潡

鎻愪緵閫氱敤宸ュ叿绫伙紝鍖呮嫭閫氱敤宸ュ叿銆両O宸ュ叿鍜屾牸寮忓寲宸ュ叿銆?

## 妯″潡缁撴瀯

```
est-utils/
鈹溾攢鈹€ est-utils-common/      # 閫氱敤宸ュ叿
鈹溾攢鈹€ est-utils-io/        # IO宸ュ叿
鈹溾攢鈹€ est-utils-format/  # 鏍煎紡鍖栧伐鍏?
鈹?  鈹溾攢鈹€ est-utils-format-json/
鈹?  鈹溾攢鈹€ est-utils-format-xml/
鈹?  鈹斺攢鈹€ est-utils-format-yaml/
鈹斺攢鈹€ pom.xml
```

## 閫氱敤宸ュ叿 (est-utils-common)

### 1. AssertUtils - 鏂█宸ュ叿绫?

鐢ㄤ簬鍙傛暟楠岃瘉鍜屽墠缃潯浠舵鏌ャ€?

```java
import ltd.idcu.est.utils.common.AssertUtils;

public class UserService {
    
    public void createUser(User user) {
        // 妫€鏌ュ弬鏁颁笉涓?null
        AssertUtils.notNull(user, "User cannot be null");
        AssertUtils.notNull(user.getEmail(), "User email cannot be null");
        
        // 妫€鏌ュ瓧绗︿覆鏈夊唴瀹?
        AssertUtils.hasText(user.getName(), "User name cannot be blank");
        AssertUtils.hasLength(user.getEmail(), "User email cannot be empty");
        
        // 妫€鏌ユ潯浠舵槸鍚︽弧瓒?
        AssertUtils.isTrue(user.getAge() > 0, "Age must be positive");
        AssertUtils.isPositive(user.getAge(), "Age must be greater than zero");
        
        // 妫€鏌ヨ寖鍥?
        AssertUtils.inRange(user.getAge(), 0, 150, "Age must be between 0 and 150");
        
        // 妫€鏌ョ被鍨?
        AssertUtils.isInstanceOf(User.class, user, "Must be a User instance");
        
        // 鐘舵€佹鏌?
        AssertUtils.state(user.isActive(), "User must be active");
        
        // 鍒涘缓鐢ㄦ埛...
    }
    
    public void updateUser(String userId, User updateData) {
        // 妫€鏌ラ泦鍚堜笉涓虹┖
        AssertUtils.notEmpty(userId, "User ID cannot be empty");
        
        // 妫€鏌ユ暟缁勪笉涓虹┖涓旀病鏈?null 鍏冪礌
        String[] roles = updateData.getRoles();
        AssertUtils.notEmpty(roles, "Roles cannot be empty");
        AssertUtils.noNullElements(roles, "Roles cannot contain null elements");
        
        // 鏇存柊鐢ㄦ埛...
    }
}
```

**瀹屾暣鏂规硶鍒楄〃锛?*

| 鏂规硶 | 璇存槑 |
|------|------|
| `isTrue(expression, message)` | 妫€鏌ヨ〃杈惧紡鏄惁涓?true |
| `isFalse(expression, message)` | 妫€鏌ヨ〃杈惧紡鏄惁涓?false |
| `isNull(object, message)` | 妫€鏌ュ璞℃槸鍚︿负 null |
| `notNull(object, message)` | 妫€鏌ュ璞′笉涓?null |
| `requireNonNull(object, message)` | 瑕佹眰瀵硅薄涓嶄负 null锛岃繑鍥炲璞?|
| `hasLength(text, message)` | 妫€鏌ュ瓧绗︿覆鏈夐暱搴?|
| `hasText(text, message)` | 妫€鏌ュ瓧绗︿覆鏈夊唴瀹癸紙闈炵┖鐧斤級 |
| `notEmpty(array/collection/map, message)` | 妫€鏌ラ泦鍚?鏁扮粍/Map 涓嶄负绌?|
| `noNullElements(array/collection, message)` | 妫€鏌ユ病鏈?null 鍏冪礌 |
| `isInstanceOf(type, obj, message)` | 妫€鏌ョ被鍨嬪疄渚?|
| `isAssignable(superType, subType, message)` | 妫€鏌ョ被鍨嬪彲璧嬪€?|
| `state(expression, message)` | 鐘舵€佹鏌ワ紙鎶?IllegalStateException锛?|
| `inRange(value, min, max, message)` | 鑼冨洿妫€鏌?|
| `isPositive(value, message)` | 姝ｆ暟妫€鏌?|
| `isNegative(value, message)` | 璐熸暟妫€鏌?|
| `notBlank(str, message)` | 闈炵┖瀛楃涓叉鏌?|
| `matches(str, regex, message)` | 姝ｅ垯鍖归厤妫€鏌?|
| `equals(obj1, obj2, message)` | 鐩哥瓑妫€鏌?|
| `notEquals(obj1, obj2, message)` | 涓嶇瓑妫€鏌?|

### 2. ObjectUtils - 瀵硅薄宸ュ叿绫?

鐢ㄤ簬瀵硅薄 null 妫€鏌ャ€佹瘮杈冦€佽浆鎹㈢瓑鎿嶄綔銆?

```java
import ltd.idcu.est.utils.common.ObjectUtils;

public class DataProcessor {
    
    public void processData(Object data) {
        // null 妫€鏌?
        if (ObjectUtils.isNull(data)) {
            System.out.println("Data is null");
            return;
        }
        
        if (ObjectUtils.isNotNull(data)) {
            System.out.println("Data is not null");
        }
        
        // 绌哄€兼鏌ワ紙鏀寔瀛楃涓层€侀泦鍚堛€佹暟缁勩€丮ap銆丱ptional锛?
        if (ObjectUtils.isEmpty(data)) {
            System.out.println("Data is empty");
        }
        
        if (ObjectUtils.isNotEmpty(data)) {
            System.out.println("Data is not empty");
        }
        
        // 澶勭悊鏁版嵁...
    }
    
    public String formatUser(User user) {
        // 榛樿鍊煎鐞?
        String name = ObjectUtils.defaultIfNull(user.getName(), "Unknown");
        String email = ObjectUtils.defaultIfNull(user.getEmail(), () -> "default@example.com");
        
        // 鑾峰彇绗竴涓潪 null 鍊?
        String displayName = ObjectUtils.firstNonNull(
            user.getNickname(),
            user.getFullName(),
            user.getUsername(),
            "Anonymous"
        );
        
        return displayName;
    }
    
    public boolean compareUsers(User user1, User user2) {
        // 瀹夊叏鐨?equals 姣旇緝
        return ObjectUtils.equals(user1, user2);
    }
    
    public String safeToString(Object obj) {
        // 瀹夊叏鐨?toString
        return ObjectUtils.toString(obj, "N/A");
    }
    
    public int getLength(Object obj) {
        // 鑾峰彇闀垮害锛堟敮鎸佸瓧绗︿覆銆佹暟缁勩€侀泦鍚堛€丮ap銆丱ptional锛?
        return ObjectUtils.length(obj);
    }
    
    public <T> java.util.Optional<T> toOptional(T obj) {
        // 杞崲涓?Optional
        return ObjectUtils.toOptional(obj);
    }
}
```

**瀹屾暣鏂规硶鍒楄〃锛?*

| 鏂规硶 | 璇存槑 |
|------|------|
| `isNull(obj)` | 妫€鏌ユ槸鍚︿负 null |
| `isNotNull(obj)` | 妫€鏌ユ槸鍚︿笉涓?null |
| `isEmpty(obj)` | 妫€鏌ユ槸鍚︿负绌猴紙鏀寔澶氱绫诲瀷锛?|
| `isNotEmpty(obj)` | 妫€鏌ユ槸鍚︿笉涓虹┖ |
| `allNull(array)` | 妫€鏌ユ槸鍚﹀叏閮ㄤ负 null |
| `allNotNull(array)` | 妫€鏌ユ槸鍚﹀叏閮ㄤ笉涓?null |
| `anyNull(array)` | 妫€鏌ユ槸鍚︽湁浠讳綍涓?null |
| `equals(o1, o2)` | 瀹夊叏鐨?equals 姣旇緝 |
| `notEqual(o1, o2)` | 瀹夊叏鐨?not equals 姣旇緝 |
| `hashCode(obj)` | 瀹夊叏鐨?hashCode |
| `hash(values)` | 澶氫釜鍊肩殑 hashCode |
| `identityToString(obj)` | 韬唤瀛楃涓诧紙绫诲悕@hash锛?|
| `defaultIfNull(object, defaultValue)` | null 鏃惰繑鍥為粯璁ゅ€?|
| `defaultIfNull(object, supplier)` | null 鏃堕€氳繃 supplier 鑾峰彇榛樿鍊?|
| `firstNonNull(values)` | 鑾峰彇绗竴涓潪 null 鍊?|
| `clone(obj)` | 鍏嬮殕瀵硅薄 |
| `cloneSerializable(obj)` | 搴忓垪鍖栧厠闅?|
| `serialize(obj)` | 搴忓垪鍖?|
| `deserialize(data)` | 鍙嶅簭鍒楀寲 |
| `toString(obj)` | 瀹夊叏鐨?toString |
| `toString(obj, nullStr)` | 甯?null 鏇夸唬鐨?toString |
| `nullToEmpty(array/charSequence)` | null 杞负绌?|
| `emptyToNull(array/charSequence)` | 绌鸿浆涓?null |
| `compare(c1, c2)` | 姣旇緝 |
| `min(values)` | 鑾峰彇鏈€灏忓€?|
| `max(values)` | 鑾峰彇鏈€澶у€?|
| `length(obj)` | 鑾峰彇闀垮害 |
| `isArray(obj)` | 妫€鏌ユ槸鍚︿负鏁扮粍 |
| `contains(array, value)` | 妫€鏌ユ暟缁勬槸鍚﹀寘鍚€?|
| `indexOf(array, value)` | 鏌ユ壘鍊煎湪鏁扮粍涓殑绱㈠紩 |
| `requireNonNull(obj)` | 瑕佹眰闈?null |
| `isSameType(o1, o2)` | 妫€鏌ユ槸鍚﹀悓绫诲瀷 |
| `isInstanceOf(type, obj)` | 妫€鏌ユ槸鍚︽槸瀹炰緥 |
| `toOptional(obj)` | 杞负 Optional |
| `toOptionalIfEmpty(obj)` | 绌哄€兼椂杞负 empty Optional |

### 3. StringUtils - 瀛楃涓插伐鍏风被

鐢ㄤ簬瀛楃涓插鐞嗐€佹鏌ャ€佽浆鎹㈢瓑鎿嶄綔銆?

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringProcessor {
    
    public void validateInput(String input) {
        // 瀛楃涓叉鏌?
        boolean isEmpty = StringUtils.isEmpty(input);           // 妫€鏌ユ槸鍚︿负绌?
        boolean isNotEmpty = StringUtils.isNotEmpty(input);     // 妫€鏌ユ槸鍚︿笉涓虹┖
        boolean isBlank = StringUtils.isBlank(input);           // 妫€鏌ユ槸鍚︿负绌烘垨浠呯┖鐧藉瓧绗?
        boolean isNotBlank = StringUtils.isNotBlank(input);     // 妫€鏌ユ槸鍚︿笉涓虹┖涓斾笉鍙槸绌虹櫧瀛楃
        
        System.out.println("isEmpty: " + isEmpty);
        System.out.println("isBlank: " + isBlank);
    }
    
    public String cleanString(String str) {
        // 瀛楃涓蹭慨鍓?
        String trimmed = StringUtils.trim(str);                // 鍘婚櫎棣栧熬绌虹櫧
        String trimToNull = StringUtils.trimToNull(str);       // 淇壀鍚庡鏋滀负绌鸿繑鍥?null
        String trimToEmpty = StringUtils.trimToEmpty(str);     // 淇壀鍚庡鏋滀负绌鸿繑鍥?""
        
        // 榛樿鍊煎鐞?
        String defaultIfEmpty = StringUtils.defaultIfEmpty(str, "default");
        String defaultIfBlank = StringUtils.defaultIfBlank(str, "default");
        
        return trimmed;
    }
    
    public String formatName(String firstName, String lastName) {
        // 瀛楃涓叉瘮杈?
        boolean equals = StringUtils.equals(firstName, lastName);
        boolean equalsIgnoreCase = StringUtils.equalsIgnoreCase(firstName, lastName);
        
        // 澶у皬鍐欒浆鎹?
        String upper = StringUtils.upperCase(firstName);
        String lower = StringUtils.lowerCase(lastName);
        String capitalized = StringUtils.capitalize(firstName);
        String uncapitalized = StringUtils.uncapitalize(firstName);
        
        return capitalized + " " + capitalized;
    }
    
    public void checkPrefixSuffix(String str) {
        // 鍓嶇紑鍚庣紑妫€鏌?
        boolean startsWith = StringUtils.startsWith(str, "http");
        boolean startsWithIgnoreCase = StringUtils.startsWithIgnoreCase(str, "HTTP");
        boolean endsWith = StringUtils.endsWith(str, ".com");
        boolean endsWithIgnoreCase = StringUtils.endsWithIgnoreCase(str, ".COM");
        
        // 鍖呭惈妫€鏌?
        boolean contains = StringUtils.contains(str, "@");
        boolean containsIgnoreCase = StringUtils.containsIgnoreCase(str, "EST");
        
        // 绱㈠紩鏌ユ壘
        int indexOf = StringUtils.indexOf(str, "@");
        int lastIndexOf = StringUtils.lastIndexOf(str, ".");
    }
    
    public String extractSubstring(String str) {
        // 瀛愬瓧绗︿覆鎿嶄綔
        String substring = StringUtils.substring(str, 5);        // 浠庣储寮?5 寮€濮?
        String substringRange = StringUtils.substring(str, 0, 10);  // 浠?0 鍒?10
        String left = StringUtils.left(str, 5);                 // 宸﹁竟 5 涓瓧绗?
        String right = StringUtils.right(str, 5);               // 鍙宠竟 5 涓瓧绗?
        String mid = StringUtils.mid(str, 2, 3);                // 浠庣储寮?2 寮€濮嬪彇 3 涓瓧绗?
        
        return left;
    }
    
    public String[] splitAndJoin(String str) {
        // 瀛楃涓插垎鍓?
        String[] parts = StringUtils.split(str, ",");            // 鐢ㄩ€楀彿鍒嗗壊
        String[] partsWithLimit = StringUtils.split(str, ",", 3, false); // 鏈€澶氬垎鍓?3 娆?
        
        // 瀛楃涓叉嫾鎺?
        String[] array = {"a", "b", "c"};
        String joinedArray = StringUtils.join(array, ", ");
        
        java.util.List<String> list = java.util.Arrays.asList("x", "y", "z");
        String joinedList = StringUtils.join(list, " - ");
        
        return parts;
    }
    
    public String modifyString(String str) {
        // 瀛楃涓蹭慨鏀?
        String reversed = StringUtils.reverse(str);              // 鍙嶈浆
        String repeated = StringUtils.repeat(str, 3);            // 閲嶅 3 娆?
        String replaced = StringUtils.replace(str, "old", "new"); // 鏇挎崲
        String replacedAll = StringUtils.replaceAll(str, "\\d", "*"); // 姝ｅ垯鏇挎崲鍏ㄩ儴
        String replacedFirst = StringUtils.replaceFirst(str, "\\d", "*"); // 姝ｅ垯鏇挎崲绗竴涓?
        
        // 绉婚櫎
        String removed = StringUtils.remove(str, "remove");
        String removedStart = StringUtils.removeStart(str, "prefix-");
        String removedEnd = StringUtils.removeEnd(str, "-suffix");
        
        return replaced;
    }
    
    public String padString(String str) {
        // 濉厖
        String padLeft = StringUtils.padLeft(str, 10, '0');      // 宸﹀～鍏呭埌 10 浣嶏紝鐢?0
        String padRight = StringUtils.padRight(str, 10, ' ');    // 鍙冲～鍏呭埌 10 浣嶏紝鐢ㄧ┖鏍?
        String center = StringUtils.center(str, 10, '-');        // 灞呬腑锛岀敤 - 濉厖
        
        return padLeft;
    }
    
    public String truncateString(String str) {
        // 鎴柇
        String truncated = StringUtils.truncate(str, 20);         // 鎴柇鍒?20 瀛楃锛岄粯璁ょ敤 ...
        String truncatedWithIndicator = StringUtils.truncate(str, 20, "[...]"); // 鑷畾涔夋埅鏂
        
        return truncated;
    }
    
    public boolean checkStringType(String str) {
        // 瀛楃涓茬被鍨嬫鏌?
        boolean isNumeric = StringUtils.isNumeric(str);           // 鏄惁绾暟瀛?
        boolean isAlpha = StringUtils.isAlpha(str);               // 鏄惁绾瓧姣?
        boolean isAlphanumeric = StringUtils.isAlphanumeric(str); // 鏄惁瀛楁瘝鏁板瓧
        boolean isWhitespace = StringUtils.isWhitespace(str);     // 鏄惁绾┖鐧?
        
        return isNumeric;
    }
    
    public int countOccurrences(String str, String sub) {
        // 缁熻鍑虹幇娆℃暟
        return StringUtils.countMatches(str, sub);
    }
}
```

**瀹屾暣鏂规硶鍒楄〃锛堥儴鍒嗭級锛?*

| 鍒嗙被 | 鏂规硶 |
|------|------|
| **妫€鏌?* | `isEmpty`, `isNotEmpty`, `isBlank`, `isNotBlank` |
| **淇壀** | `trim`, `trimToNull`, `trimToEmpty` |
| **榛樿鍊?* | `defaultIfEmpty`, `defaultIfBlank` |
| **姣旇緝** | `equals`, `equalsIgnoreCase`, `compare`, `compareIgnoreCase` |
| **鍓嶇紑鍚庣紑** | `startsWith`, `startsWithIgnoreCase`, `endsWith`, `endsWithIgnoreCase` |
| **鍖呭惈** | `contains`, `containsIgnoreCase`, `indexOf`, `lastIndexOf` |
| **瀛愬瓧绗︿覆** | `substring`, `left`, `right`, `mid` |
| **鍒嗗壊鎷兼帴** | `split`, `join` |
| **澶у皬鍐?* | `upperCase`, `lowerCase`, `capitalize`, `uncapitalize` |
| **淇敼** | `reverse`, `repeat`, `replace`, `replaceAll`, `replaceFirst` |
| **绉婚櫎** | `remove`, `removeStart`, `removeEnd` |
| **濉厖** | `padLeft`, `padRight`, `center` |
| **鎴柇** | `truncate`, `abbreviate` |
| **绫诲瀷妫€鏌?* | `isNumeric`, `isAlpha`, `isAlphanumeric`, `isWhitespace` |
| **鍏朵粬** | `length`, `countMatches`, `strip`, `stripStart`, `stripEnd`, `chomp`, `chop` |

### 4. 鍏朵粬宸ュ叿绫?

- **ArrayUtils**锛氭暟缁勫伐鍏风被
- **NumberUtils**锛氭暟瀛楀伐鍏风被
- **DateUtils**锛氭棩鏈熷伐鍏风被
- **ClassUtils**锛氱被宸ュ叿绫?

## IO宸ュ叿 (est-utils-io)

```java
import ltd.idcu.est.utils.io.Files;

// 鏂囦欢璇诲彇
String content = Files.readString("./file.txt");
java.util.List<String> lines = Files.readLines("./file.txt");
byte[] bytes = Files.readBytes("./file.bin");

// 鏂囦欢鍐欏叆
Files.writeString("./file.txt", content);
Files.writeBytes("./file.bin", bytes);

// 鏂囦欢鎿嶄綔
Files.createDirectories("./path/to/dir");
Files.deleteIfExists("./file.txt");
boolean exists = Files.exists("./file.txt");
```

## 鏍煎紡鍖栧伐鍏?

### JSON 鏍煎紡鍖?

```java
import ltd.idcu.est.utils.format.json.Json;

// JSON瑙ｆ瀽
User user = Json.parse(jsonString, User.class);
String json = Json.stringify(user);

// 鏍煎紡鍖?
String pretty = Json.prettyPrint(jsonString);
```

### XML 鏍煎紡鍖?

```java
import ltd.idcu.est.utils.format.xml.Xml;

// XML瑙ｆ瀽
User user = Xml.parse(xmlString, User.class);
String xml = Xml.stringify(user);
```

### YAML 鏍煎紡鍖?

```java
import ltd.idcu.est.utils.format.yaml.Yaml;

// YAML瑙ｆ瀽
User user = Yaml.parse(yamlString, User.class);
String yaml = Yaml.stringify(user);
```

## 渚濊禆

```xml
<!-- 閫氱敤宸ュ叿 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- IO宸ュ叿 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-io</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- JSON鏍煎紡鍖?-->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-format-json</artifactId>
    <version>2.1.0</version>
</dependency>
```

## 鏈€浣冲疄璺?

1. **浼樺厛浣跨敤宸ュ叿绫?*锛氶伩鍏嶆墜鍐?null 妫€鏌ャ€佸瓧绗︿覆澶勭悊绛夐€氱敤閫昏緫
2. **浠ｇ爜瀹℃煡**锛氬鏌ヤ唬鐮佹椂纭繚浣跨敤浜嗙幇鏈夌殑宸ュ叿绫?
3. **涓嶈閲嶅閫犺疆瀛?*锛氬湪鍐欓€氱敤閫昏緫鍓嶏紝鍏堟鏌ユ槸鍚﹀凡鏈夊伐鍏风被鏂规硶
4. **淇濇寔涓€鑷存€?*锛氬洟闃熺粺涓€浣跨敤杩欎簺宸ュ叿绫伙紝淇濇寔浠ｇ爜椋庢牸涓€鑷?
