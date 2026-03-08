package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;

import java.util.Map;

public class GenerateServiceSkill implements Skill {

    @Override
    public String getName() {
        return "generate-service";
    }

    @Override
    public String getDescription() {
        return "Generates a service interface and implementation";
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
                "serviceName", "String - Name of the service (without 'Service' suffix)",
                "packageName", "String - Package name",
                "entityName", "String - Name of the entity class"
        );
    }

    @Override
    public Map<String, String> getOutputSchema() {
        return Map.of(
                "code", "String - Generated service code"
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> inputs) {
        String serviceName = (String) inputs.get("serviceName");
        String packageName = (String) inputs.getOrDefault("packageName", "com.example.service");
        String entityName = (String) inputs.getOrDefault("entityName", serviceName);

        String interfaceName = serviceName + "Service";
        String implName = interfaceName + "Impl";
        String repositoryName = entityName + "Repository";
        String entityVar = decapitalize(entityName);

        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n\n");
        code.append("public interface ").append(interfaceName).append(" {\n\n");
        code.append("    ").append(entityName).append(" create(").append(entityName).append(" ").append(entityVar).append(");\n\n");
        code.append("    Optional<").append(entityName).append("> getById(Long id);\n\n");
        code.append("    List<").append(entityName).append("> getAll();\n\n");
        code.append("    ").append(entityName).append(" update(").append(entityName).append(" ").append(entityVar).append(");\n\n");
        code.append("    void deleteById(Long id);\n\n");
        code.append("    boolean existsById(Long id);\n\n");
        code.append("    long count();\n\n");
        code.append("}\n\n");
        code.append("class ").append(implName).append(" implements ").append(interfaceName).append(" {\n\n");
        code.append("    private final ").append(repositoryName).append(" repository;\n\n");
        code.append("    public ").append(implName).append("(").append(repositoryName).append(" repository) {\n");
        code.append("        this.repository = repository;\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public ").append(entityName).append(" create(").append(entityName).append(" ").append(entityVar).append(") {\n");
        code.append("        return repository.save(").append(entityVar).append(");\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public Optional<").append(entityName).append("> getById(Long id) {\n");
        code.append("        return repository.findById(id);\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public List<").append(entityName).append("> getAll() {\n");
        code.append("        return repository.findAll();\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public ").append(entityName).append(" update(").append(entityName).append(" ").append(entityVar).append(") {\n");
        code.append("        return repository.save(").append(entityVar).append(");\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public void deleteById(Long id) {\n");
        code.append("        repository.deleteById(id);\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public boolean existsById(Long id) {\n");
        code.append("        return repository.existsById(id);\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public long count() {\n");
        code.append("        return repository.count();\n");
        code.append("    }\n");
        code.append("}\n");

        return SkillResult.success(Map.of("code", code.toString()));
    }

    @Override
    public boolean canExecute(Map<String, Object> inputs) {
        return inputs.containsKey("serviceName");
    }

    private String decapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
