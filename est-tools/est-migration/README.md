# est-migration - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-migration](#浠€涔堟槸-est-migration)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-migration

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-migration 灏卞儚"鏁版嵁搴撴惉瀹跺叕鍙?锛屽府浣犵鐞嗘暟鎹簱鐗堟湰鍙樻洿锛屽垱寤鸿〃銆佷慨鏀瑰瓧娈点€佹暟鎹縼绉伙紝涓€濂楄剼鏈悶瀹氥€?
### 鏍稿績鐗圭偣
- **鐗堟湰绠＄悊**锛氭寜鐗堟湰绠＄悊鏁版嵁搴撳彉鏇?- **SQL鑴氭湰**锛氭敮鎸?SQL 鑴氭湰杩佺Щ
- **Java杩佺Щ**锛氭敮鎸?Java 浠ｇ爜杩佺Щ
- **鍥炴粴鏀寔**锛氭敮鎸佽縼绉诲洖婊?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-migration</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍒涘缓杩佺Щ鑴氭湰
```sql
-- db/migration/V1__Create_user_table.sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. 鎵ц杩佺Щ
```bash
# 鎵ц杩佺Щ
est migrate

# 鏌ョ湅鐘舵€?est migrate status

# 鍥炴粴鍒版寚瀹氱増鏈?est migrate rollback 1
```

---

## 鏍稿績鍔熻兘

### Java 杩佺Щ
```java
@Migration(version = 2, description = "娣诲姞鐢ㄦ埛绱㈠紩")
public class V2__Add_user_index implements JavaMigration {
    
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE INDEX idx_username ON user(username)");
        }
    }
}
```

### 杩佺Щ閰嶇疆
```yaml
est:
  migration:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
```

---

## 妯″潡缁撴瀯

```
est-migration/
鈹溾攢鈹€ src/main/java/
鈹?  鈹斺攢鈹€ ltd/idcu/est/tools/migration/
鈹?      鈹溾攢鈹€ migration/
鈹?      鈹斺攢鈹€ resolver/
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [CLI 宸ュ叿](../est-cli/README.md)
