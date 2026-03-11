package ltd.idcu.est.codecli.agent.skill;

import ltd.idcu.est.agent.api.*;

import java.util.Map;

public class CodeGenerationSkill implements Skill {
    
    @Override
    public String getName() {
        return "code_generation";
    }
    
    @Override
    public String getDescription() {
        return "根据描述生成符合 EST 规范的代码";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String description = input.getParameter("description", String.class, "");
        String type = input.getParameter("type", String.class, "entity");
        
        String code = generateCode(description, type);
        return SkillResult.success(code);
    }
    
    private String generateCode(String description, String type) {
        switch (type.toLowerCase()) {
            case "entity":
                return generateEntityCode(description);
            case "controller":
                return generateControllerCode(description);
            case "service":
                return generateServiceCode(description);
            case "mapper":
                return generateMapperCode(description);
            default:
                return generateGenericCode(description);
        }
    }
    
    private String generateEntityCode(String description) {
        return "package com.example.entity;\n\n" +
               "import ltd.idcu.est.data.annotation.Entity;\n" +
               "import ltd.idcu.est.data.annotation.Id;\n" +
               "import ltd.idcu.est.data.annotation.Column;\n\n" +
               "@Entity\n" +
               "public class " + extractClassName(description) + " {\n" +
               "    @Id\n" +
               "    private Long id;\n\n" +
               "    @Column\n" +
               "    private String name;\n\n" +
               "    public Long getId() { return id; }\n" +
               "    public void setId(Long id) { this.id = id; }\n" +
               "    public String getName() { return name; }\n" +
               "    public void setName(String name) { this.name = name; }\n" +
               "}";
    }
    
    private String generateControllerCode(String description) {
        return "package com.example.controller;\n\n" +
               "import ltd.idcu.est.web.annotation.Controller;\n" +
               "import ltd.idcu.est.web.annotation.GetMapping;\n" +
               "import ltd.idcu.est.web.annotation.PostMapping;\n" +
               "import ltd.idcu.est.web.annotation.RequestBody;\n" +
               "import ltd.idcu.est.web.annotation.PathVariable;\n\n" +
               "@Controller\n" +
               "public class " + extractClassName(description) + "Controller {\n" +
               "    @GetMapping(\"/items\")\n" +
               "    public Object list() {\n" +
               "        return \"List of items\";\n" +
               "    }\n\n" +
               "    @GetMapping(\"/items/{id}\")\n" +
               "    public Object get(@PathVariable Long id) {\n" +
               "        return \"Item: \" + id;\n" +
               "    }\n\n" +
               "    @PostMapping(\"/items\")\n" +
               "    public Object create(@RequestBody Object item) {\n" +
               "        return \"Item created\";\n" +
               "    }\n" +
               "}";
    }
    
    private String generateServiceCode(String description) {
        return "package com.example.service;\n\n" +
               "import ltd.idcu.est.core.annotation.Service;\n" +
               "import ltd.idcu.est.core.annotation.Inject;\n\n" +
               "@Service\n" +
               "public class " + extractClassName(description) + "Service {\n" +
               "    public Object create(Object dto) {\n" +
               "        return dto;\n" +
               "    }\n\n" +
               "    public Object getById(Long id) {\n" +
               "        return null;\n" +
               "    }\n\n" +
               "    public Object update(Long id, Object dto) {\n" +
               "        return dto;\n" +
               "    }\n\n" +
               "    public void delete(Long id) {\n" +
               "    }\n" +
               "}";
    }
    
    private String generateMapperCode(String description) {
        return "package com.example.mapper;\n\n" +
               "import ltd.idcu.est.data.annotation.Mapper;\n" +
               "import ltd.idcu.est.data.annotation.Select;\n" +
               "import ltd.idcu.est.data.annotation.Insert;\n" +
               "import ltd.idcu.est.data.annotation.Update;\n" +
               "import ltd.idcu.est.data.annotation.Delete;\n\n" +
               "@Mapper\n" +
               "public interface " + extractClassName(description) + "Mapper {\n" +
               "    @Select(\"SELECT * FROM table_name WHERE id = ?\")\n" +
               "    Object findById(Long id);\n\n" +
               "    @Insert(\"INSERT INTO table_name (...) VALUES (...)\")\n" +
               "    int insert(Object entity);\n\n" +
               "    @Update(\"UPDATE table_name SET ... WHERE id = ?\")\n" +
               "    int update(Object entity);\n\n" +
               "    @Delete(\"DELETE FROM table_name WHERE id = ?\")\n" +
               "    int delete(Long id);\n" +
               "}";
    }
    
    private String generateGenericCode(String description) {
        return "// Generated code based on: " + description + "\n" +
               "// Please provide more specific type (entity/controller/service/mapper)";
    }
    
    private String extractClassName(String description) {
        String[] words = description.split("\\s+");
        StringBuilder className = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                className.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    className.append(word.substring(1).toLowerCase());
                }
            }
        }
        String result = className.toString();
        return result.isEmpty() ? "Sample" : result;
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
