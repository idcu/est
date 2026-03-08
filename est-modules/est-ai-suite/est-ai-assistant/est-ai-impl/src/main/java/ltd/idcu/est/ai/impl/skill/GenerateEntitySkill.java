package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;

import java.util.List;
import java.util.Map;

public class GenerateEntitySkill implements Skill {

    @Override
    public String getName() {
        return "generate-entity";
    }

    @Override
    public String getDescription() {
        return "Generates a Java entity class with getters and setters";
    }

    @Override
    public String getCategory() {
        return "code-generation";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
                "className", "String - Name of the entity class",
                "packageName", "String - Package name",
                "fields", "List<String> - List of fields in format 'name:type'"
        );
    }

    @Override
    public Map<String, String> getOutputSchema() {
        return Map.of(
                "code", "String - Generated Java code"
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> inputs) {
        String className = (String) inputs.get("className");
        String packageName = (String) inputs.getOrDefault("packageName", "com.example.entity");
        @SuppressWarnings("unchecked")
        List<String> fields = (List<String>) inputs.getOrDefault("fields", 
                List.of("id:Long", "name:String", "createdAt:LocalDateTime"));

        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import java.time.LocalDateTime;\n\n");
        code.append("public class ").append(className).append(" {\n\n");

        for (String fieldDef : fields) {
            String[] parts = fieldDef.split(":");
            String fieldName = parts[0];
            String fieldType = parts.length > 1 ? parts[1] : "String";
            code.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
        }

        code.append("\n    public ").append(className).append("() {}\n\n");

        for (String fieldDef : fields) {
            String[] parts = fieldDef.split(":");
            String fieldName = parts[0];
            String fieldType = parts.length > 1 ? parts[1] : "String";
            String capitalized = capitalize(fieldName);

            code.append("\n    public ").append(fieldType).append(" get").append(capitalized).append("() {\n");
            code.append("        return ").append(fieldName).append(";\n");
            code.append("    }\n");

            code.append("\n    public void set").append(capitalized).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            code.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            code.append("    }\n");
        }

        code.append("}\n");

        return SkillResult.success(Map.of("code", code.toString()));
    }

    @Override
    public boolean canExecute(Map<String, Object> inputs) {
        return inputs.containsKey("className");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
