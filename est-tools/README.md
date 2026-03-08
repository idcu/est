# EST Tools 宸ュ叿妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Tools锛焆(#浠€涔堟槸-est-tools)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Tools锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Tools 灏卞儚鏄竴涓?宸ュ叿闂?銆傛兂璞′竴涓嬩綘瑕佸仛鏈ㄥ伐锛岄渶瑕佸悇绉嶅伐鍏凤細鐢甸敮銆佺數閽汇€佸埁瀛愩€佺爞绾?..

**浼犵粺鏂瑰紡**锛氭瘡娆″仛涓滆タ閮借鑷繁鍑嗗宸ュ叿锛屽緢楹荤儲銆?
**EST Tools 鏂瑰紡**锛氱粰浣犱竴涓婊″伐鍏风殑宸ュ叿闂达紝閲岄潰鏈夛細
- 馃洜锔?**鑴氭墜鏋?* - 蹇€熷垱寤洪」鐩?- 馃彈锔?**浠ｇ爜鐢熸垚** - 鏍规嵁鏁版嵁搴撶敓鎴愪唬鐮?- 馃攧 **杩佺Щ宸ュ叿** - 浠庡叾浠栨鏋惰縼绉?- 馃捇 **鍛戒护琛屽伐鍏?* - CLI 鍛戒护琛屽伐鍏?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍥惧舰鍖栫晫闈紝寮€绠卞嵆鐢?- 鈿?**蹇€熼珮鏁?* - 涓€閿敓鎴愶紝鑺傜渷鏃堕棿
- 馃敡 **鐏垫椿閰嶇疆** - 鍙嚜瀹氫箟妯℃澘
- 馃帹 **澶氱宸ュ叿** - 鑴氭墜鏋躲€佷唬鐮佺敓鎴愩€佽縼绉荤瓑

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-scaffold</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣跨敤鑴氭墜鏋跺垱寤洪」鐩?
```java
import ltd.idcu.est.scaffold.ScaffoldGenerator;
import ltd.idcu.est.scaffold.ProjectConfig;
import ltd.idcu.est.scaffold.ProjectType;

public class FirstScaffold {
    public static void main(String[] args) {
        System.out.println("=== EST Tools 绗竴涓ず渚?===\n");
        
        ProjectConfig config = new ProjectConfig();
        config.setProjectName("my-project");
        config.setGroupId("com.example");
        config.setArtifactId("my-app");
        config.setVersion("1.0.0");
        config.setProjectType(ProjectType.WEB);
        
        ScaffoldGenerator generator = new ScaffoldGenerator();
        generator.generate(config, Paths.get("./my-project"));
        
        System.out.println("椤圭洰宸茬敓鎴? ./my-project");
    }
}
```

鎴栬€呬娇鐢?Web 鐣岄潰锛?
```java
import ltd.idcu.est.scaffold.ScaffoldWebServer;

public class ScaffoldWebApp {
    public static void main(String[] args) {
        ScaffoldWebServer server = new ScaffoldWebServer();
        server.start(8080);
        System.out.println("鑴氭墜鏋?Web 鐣岄潰宸插惎鍔? http://localhost:8080");
    }
}
```

---

## 鍩虹绡?
### 1. est-scaffold 鑴氭墜鏋?
璇︾粏鏂囨。璇峰弬鑰冿細[est-scaffold README](./est-scaffold/README.md)

#### 椤圭洰绫诲瀷

EST Scaffold 鏀寔澶氱椤圭洰绫诲瀷锛?
```java
import ltd.idcu.est.scaffold.ProjectType;

// Web 搴旂敤
ProjectType.WEB

// API 搴旂敤
ProjectType.API

// 鍩虹搴旂敤
ProjectType.BASIC

// 鍛戒护琛屽簲鐢?ProjectType.CLI

// 寰湇鍔″簲鐢?ProjectType.MICROSERVICE

// 搴撻」鐩?ProjectType.LIBRARY

// 鎻掍欢椤圭洰
ProjectType.PLUGIN
```

#### 鐢熸垚椤圭洰

```java
import ltd.idcu.est.scaffold.ScaffoldGenerator;
import ltd.idcu.est.scaffold.ProjectConfig;

ProjectConfig config = new ProjectConfig();
config.setProjectName("My Web App");
config.setGroupId("com.example");
config.setArtifactId("my-web-app");
config.setVersion("1.0.0");
config.setPackageName("com.example.myapp");
config.setProjectType(ProjectType.WEB);
config.setDescription("鎴戠殑 Web 搴旂敤");
config.setAuthor("Your Name");

// 娣诲姞渚濊禆
config.addDependency("est-web");
config.addDependency("est-data-jdbc");
config.addDependency("est-cache");

// 鐢熸垚椤圭洰
ScaffoldGenerator generator = new ScaffoldGenerator();
generator.generate(config, Paths.get("./my-web-app"));
```

#### 鑷畾涔夋ā鏉?
```java
import ltd.idcu.est.scaffold.TemplateEngine;
import ltd.idcu.est.scaffold.TemplateFileSystem;

// 鍔犺浇鑷畾涔夋ā鏉?TemplateFileSystem templateFs = new TemplateFileSystem(Paths.get("./my-templates"));
TemplateEngine engine = new TemplateEngine(templateFs);

// 浣跨敤鑷畾涔夋ā鏉跨敓鎴?engine.render("my-template.ftl", Map.of("name", "寮犱笁"));
```

### 2. est-codegen 浠ｇ爜鐢熸垚

EST CodeGen 鐢ㄤ簬鏍规嵁鏁版嵁搴撹〃鐢熸垚浠ｇ爜銆?
```java
import ltd.idcu.est.codegen.CodeGenerator;
import ltd.idcu.est.codegen.db.DatabaseCodeGenerator;
import ltd.idcu.est.codegen.pojo.PojoGenerator;

// 浠庢暟鎹簱鐢熸垚浠ｇ爜
DatabaseCodeGenerator dbGenerator = new DatabaseCodeGenerator();
dbGenerator.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
dbGenerator.setUsername("root");
dbGenerator.setPassword("password");
dbGenerator.setPackageName("com.example.entity");
dbGenerator.setOutputDir(Paths.get("./src/main/java"));

dbGenerator.generate();

// 鐢熸垚 POJO
PojoGenerator pojoGenerator = new PojoGenerator();
pojoGenerator.setClassName("User");
pojoGenerator.setPackageName("com.example.entity");
pojoGenerator.addField("id", "Long");
pojoGenerator.addField("name", "String");
pojoGenerator.addField("email", "String");

String code = pojoGenerator.generate();
System.out.println(code);
```

### 3. est-migration 杩佺Щ宸ュ叿

EST Migration 鐢ㄤ簬浠庡叾浠栨鏋惰縼绉诲埌 EST銆?
```java
import ltd.idcu.est.migration.MigrationEngine;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.rules.SpringBootAnnotationMigrationRule;
import ltd.idcu.est.migration.rules.SpringBootImportMigrationRule;

// 閰嶇疆杩佺Щ
MigrationConfig config = new MigrationConfig();
config.setSourceDir(Paths.get("./spring-boot-app"));
config.setTargetDir(Paths.get("./est-app"));
config.setSourceFramework("spring-boot");
config.setTargetFramework("est");

// 鍒涘缓杩佺Щ寮曟搸
MigrationEngine engine = new MigrationEngine(config);

// 娣诲姞杩佺Щ瑙勫垯
engine.addRule(new SpringBootAnnotationMigrationRule());
engine.addRule(new SpringBootImportMigrationRule());
engine.addRule(new SpringBootMainClassMigrationRule());

// 鎵ц杩佺Щ
MigrationResult result = engine.migrate();
System.out.println("杩佺Щ瀹屾垚: " + result.getMigratedFiles() + " 涓枃浠?);
```

### 4. est-cli 鍛戒护琛屽伐鍏?
EST CLI 鎻愪緵鍛戒护琛屽伐鍏枫€?
```java
import ltd.idcu.est.cli.EstCliMain;

public class CliApp {
    public static void main(String[] args) {
        EstCliMain.main(args);
    }
}
```

浣跨敤鍛戒护琛岋細

```bash
# 鍒涘缓鏂伴」鐩?java -jar est-cli.jar create --name my-app --type web

# 鐢熸垚浠ｇ爜
java -jar est-cli.jar generate --entity User --fields id:Long,name:String

# 杩佺Щ椤圭洰
java -jar est-cli.jar migrate --source ./spring-app --target ./est-app
```

---

## 杩涢樁绡?
### 1. 鑴氭墜鏋惰繘闃?
璇︾粏鍐呭璇峰弬鑰冿細[est-scaffold 杩涢樁绡嘳(./est-scaffold/README.md)

#### 鍒涘缓鑷畾涔夐」鐩被鍨?
```java
import ltd.idcu.est.scaffold.ProjectType;
import ltd.idcu.est.scaffold.ScaffoldGenerator;

// 娉ㄥ唽鑷畾涔夐」鐩被鍨?ProjectType customType = new ProjectType("custom", "鑷畾涔夐」鐩?);
ScaffoldGenerator.registerProjectType(customType);
```

#### 浠ｇ爜鐗囨

```java
import ltd.idcu.est.scaffold.CodeSnippetGenerator;

CodeSnippetGenerator snippetGen = new CodeSnippetGenerator();

// 鐢熸垚 Controller 浠ｇ爜
String controllerCode = snippetGen.generateController("User", "com.example.controller");

// 鐢熸垚 Service 浠ｇ爜
String serviceCode = snippetGen.generateService("User", "com.example.service");

// 鐢熸垚 Repository 浠ｇ爜
String repoCode = snippetGen.generateRepository("User", "com.example.repository");
```

### 2. 浠ｇ爜鐢熸垚杩涢樁

#### 鑷畾涔変唬鐮佹ā鏉?
```java
import ltd.idcu.est.codegen.CodeGenerator;

// 鑷畾涔?Entity 妯℃澘
String entityTemplate = """
    package ${packageName};
    
    public class ${className} {
        ${fields}
        
        ${gettersSetters}
    }
    """;

CodeGenerator generator = new CodeGenerator();
generator.setEntityTemplate(entityTemplate);
```

### 3. 杩佺Щ宸ュ叿杩涢樁

#### 鑷畾涔夎縼绉昏鍒?
```java
import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationResult;

public class MyMigrationRule extends AbstractMigrationRule {
    
    @Override
    public MigrationResult apply(String content) {
        String migrated = content
            .replace("@Autowired", "@Inject")
            .replace("@RestController", "@Component");
        
        return MigrationResult.success(migrated);
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 浣跨敤鑴氭墜鏋跺揩閫熷紑濮?
```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄨ剼鎵嬫灦鍒涘缓椤圭洰
ProjectConfig config = new ProjectConfig();
config.setProjectType(ProjectType.WEB);
generator.generate(config, outputDir);

// 鉂?涓嶆帹鑽愶細鎵嬪姩鍒涘缓鎵€鏈夋枃浠?// 瀹规槗閬楁紡锛屾晥鐜囦綆
```

### 2. 鍚堢悊浣跨敤浠ｇ爜鐢熸垚

```java
// 鉁?鎺ㄨ崘锛氱敓鎴愬熀纭€浠ｇ爜锛岀劧鍚庢墜鍔ㄤ慨鏀?String entityCode = generator.generateEntity("User");
// 鐒跺悗娣诲姞鑷畾涔変笟鍔￠€昏緫

// 鉂?涓嶆帹鑽愶細瀹屽叏渚濊禆浠ｇ爜鐢熸垚
// 鐢熸垚鐨勪唬鐮佸彲鑳戒笉绗﹀悎鐗瑰畾闇€姹?```

### 3. 杩佺Щ鍓嶅浠?
```java
// 鉁?鎺ㄨ崘锛氳縼绉诲墠澶囦唤鍘熼」鐩?Path sourceDir = Paths.get("./old-project");
Path backupDir = Paths.get("./old-project-backup");
Files.copy(sourceDir, backupDir);

// 鐒跺悗鎵ц杩佺Щ
engine.migrate();
```

---

## 妯″潡缁撴瀯

```
est-tools/
鈹溾攢鈹€ est-scaffold/     # 鑴氭墜鏋剁敓鎴愬櫒
鈹溾攢鈹€ est-codegen/      # 浠ｇ爜鐢熸垚鍣?鈹溾攢鈹€ est-migration/    # 杩佺Щ宸ュ叿
鈹斺攢鈹€ est-cli/          # 鍛戒护琛屽伐鍏?```

---

## 鐩稿叧璧勬簮

- [est-scaffold README](./est-scaffold/README.md) - 鑴氭墜鏋惰缁嗘枃妗?- [绀轰緥浠ｇ爜](../est-examples/est-examples-basic/) - 鍩虹绀轰緥
- [EST App](../est-app/README.md) - 搴旂敤妯″潡
- [EST Core](../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
