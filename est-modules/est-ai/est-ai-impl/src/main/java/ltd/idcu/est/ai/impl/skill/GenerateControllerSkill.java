package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;

import java.util.Map;

public class GenerateControllerSkill implements Skill {

    @Override
    public String getName() {
        return "generate-controller";
    }

    @Override
    public String getDescription() {
        return "Generates a REST API controller with CRUD operations";
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
                "controllerName", "String - Name of the controller (without 'Controller' suffix)",
                "packageName", "String - Package name"
        );
    }

    @Override
    public Map<String, String> getOutputSchema() {
        return Map.of(
                "code", "String - Generated controller code"
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> inputs) {
        String controllerName = (String) inputs.get("controllerName");
        String packageName = (String) inputs.getOrDefault("packageName", "com.example.controller");

        String className = controllerName + "Controller";
        String entityName = controllerName;
        String entityVar = decapitalize(entityName);
        String basePath = "/api/" + entityVar.toLowerCase() + "s";

        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import ltd.idcu.est.web.api.WebApplication;\n");
        code.append("import ltd.idcu.est.web.api.WebRequest;\n");
        code.append("import ltd.idcu.est.web.api.WebResponse;\n\n");
        code.append("import java.util.Map;\n");
        code.append("import java.util.concurrent.ConcurrentHashMap;\n");
        code.append("import java.util.concurrent.atomic.AtomicLong;\n\n");
        code.append("public class ").append(className).append(" {\n\n");
        code.append("    private final Map<Long, ").append(entityName).append("> ").append(entityVar).append("s = new ConcurrentHashMap<>();\n");
        code.append("    private final AtomicLong idGenerator = new AtomicLong(1);\n\n");
        code.append("    public void registerRoutes(WebApplication app) {\n");
        code.append("        app.group(\"").append(basePath).append("\", group -> {\n");
        code.append("            group.get(\"/\", this::listAll);\n");
        code.append("            group.get(\"/:id\", this::getById);\n");
        code.append("            group.post(\"/\", this::create);\n");
        code.append("            group.put(\"/:id\", this::update);\n");
        code.append("            group.delete(\"/:id\", this::delete);\n");
        code.append("        });\n");
        code.append("    }\n\n");
        code.append("    private void listAll(WebRequest req, WebResponse res) {\n");
        code.append("        res.json(").append(entityVar).append("s.values());\n");
        code.append("    }\n\n");
        code.append("    private void getById(WebRequest req, WebResponse res) {\n");
        code.append("        long id = Long.parseLong(req.pathParam(\"id\"));\n");
        code.append("        ").append(entityName).append(" entity = ").append(entityVar).append("s.get(id);\n");
        code.append("        if (entity != null) {\n");
        code.append("            res.json(entity);\n");
        code.append("        } else {\n");
        code.append("            res.status(404).json(Map.of(\n");
        code.append("                \"success\", false,\n");
        code.append("                \"error\", \"").append(entityName).append(" not found with id: \" + id\n");
        code.append("            ));\n");
        code.append("        }\n");
        code.append("    }\n\n");
        code.append("    private void create(WebRequest req, WebResponse res) {\n");
        code.append("        ").append(entityName).append(" entity = req.bodyAs(").append(entityName).append(".class);\n");
        code.append("        long id = idGenerator.getAndIncrement();\n");
        code.append("        entity.setId(id);\n");
        code.append("        ").append(entityVar).append("s.put(id, entity);\n");
        code.append("        res.status(201).json(entity);\n");
        code.append("    }\n\n");
        code.append("    private void update(WebRequest req, WebResponse res) {\n");
        code.append("        long id = Long.parseLong(req.pathParam(\"id\"));\n");
        code.append("        if (!").append(entityVar).append("s.containsKey(id)) {\n");
        code.append("            res.status(404).json(Map.of(\n");
        code.append("                \"success\", false,\n");
        code.append("                \"error\", \"").append(entityName).append(" not found with id: \" + id\n");
        code.append("            ));\n");
        code.append("            return;\n");
        code.append("        }\n");
        code.append("        ").append(entityName).append(" entity = req.bodyAs(").append(entityName).append(".class);\n");
        code.append("        entity.setId(id);\n");
        code.append("        ").append(entityVar).append("s.put(id, entity);\n");
        code.append("        res.json(entity);\n");
        code.append("    }\n\n");
        code.append("    private void delete(WebRequest req, WebResponse res) {\n");
        code.append("        long id = Long.parseLong(req.pathParam(\"id\"));\n");
        code.append("        if (").append(entityVar).append("s.remove(id) != null) {\n");
        code.append("            res.status(204).send();\n");
        code.append("        } else {\n");
        code.append("            res.status(404).json(Map.of(\n");
        code.append("                \"success\", false,\n");
        code.append("                \"error\", \"").append(entityName).append(" not found with id: \" + id\n");
        code.append("            ));\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return SkillResult.success(Map.of("code", code.toString()));
    }

    @Override
    public boolean canExecute(Map<String, Object> inputs) {
        return inputs.containsKey("controllerName");
    }

    private String decapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
