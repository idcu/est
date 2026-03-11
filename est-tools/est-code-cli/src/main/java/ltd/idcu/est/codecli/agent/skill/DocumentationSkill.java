package ltd.idcu.est.codecli.agent.skill;

import ltd.idcu.est.agent.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentationSkill implements Skill {
    
    @Override
    public String getName() {
        return "documentation";
    }
    
    @Override
    public String getDescription() {
        return "为代码生成文档";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String filePath = input.getParameter("filePath", String.class, "");
        String code = input.getParameter("code", String.class, "");
        String docType = input.getParameter("docType", String.class, "javadoc");
        
        try {
            if (!filePath.isEmpty()) {
                code = Files.readString(Paths.get(filePath));
            }
            
            String documentation = generateDocumentation(code, docType);
            return SkillResult.success(documentation);
        } catch (Exception e) {
            return SkillResult.failure("Failed to generate documentation: " + e.getMessage());
        }
    }
    
    private String generateDocumentation(String code, String docType) {
        StringBuilder doc = new StringBuilder();
        
        switch (docType.toLowerCase()) {
            case "javadoc":
                doc.append(generateJavadoc(code));
                break;
            case "readme":
                doc.append(generateReadme(code));
                break;
            default:
                doc.append(generateGenericDoc(code));
        }
        
        return doc.toString();
    }
    
    private String generateJavadoc(String code) {
        StringBuilder javadoc = new StringBuilder();
        
        javadoc.append("/**\n");
        javadoc.append(" * [类描述]\n");
        javadoc.append(" * \n");
        javadoc.append(" * @author EST Code CLI\n");
        javadoc.append(" * @version 1.0.0\n");
        javadoc.append(" * @since " + java.time.LocalDate.now() + "\n");
        javadoc.append(" */\n");
        
        if (code.contains("public class ")) {
            int classIdx = code.indexOf("public class ");
            int bracketIdx = code.indexOf("{", classIdx);
            if (bracketIdx > classIdx) {
                String className = code.substring(classIdx + 13, bracketIdx).trim();
                javadoc = new StringBuilder();
                javadoc.append("/**\n");
                javadoc.append(" * ").append(className).append(" 类\n");
                javadoc.append(" * \n");
                javadoc.append(" * 功能描述: [在此添加类的功能描述]\n");
                javadoc.append(" * \n");
                javadoc.append(" * @author EST Code CLI\n");
                javadoc.append(" * @version 1.0.0\n");
                javadoc.append(" * @since ").append(java.time.LocalDate.now()).append("\n");
                javadoc.append(" */\n");
            }
        }
        
        return javadoc.toString();
    }
    
    private String generateReadme(String code) {
        return "# 模块说明\n\n" +
               "## 功能\n\n" +
               "[在此描述模块功能]\n\n" +
               "## 快速开始\n\n" +
               "```java\n" +
               "// 使用示例\n" +
               "```\n\n" +
               "## API\n\n" +
               "| 方法 | 说明 |\n" +
               "|------|------|\n" +
               "|      |      |\n\n" +
               "## 注意事项\n\n" +
               "- [注意点 1]\n" +
               "- [注意点 2]\n";
    }
    
    private String generateGenericDoc(String code) {
        return "=== 代码文档 ===\n\n" +
               "文件信息:\n" +
               "- 类型: " + (code.contains("@Entity") ? "Entity" : 
                             code.contains("@Controller") ? "Controller" :
                             code.contains("@Service") ? "Service" :
                             code.contains("@Mapper") ? "Mapper" : "Java Class") + "\n" +
               "- 行数: " + code.split("\n").length + "\n\n" +
               "建议文档内容:\n" +
               "1. 类/接口功能描述\n" +
               "2. 主要方法说明\n" +
               "3. 使用示例\n" +
               "4. 注意事项";
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
