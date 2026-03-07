package ltd.idcu.est.codegen.pojo;

import ltd.idcu.est.codegen.CodeGenerator;
import ltd.idcu.est.scaffold.FileWriterUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PojoGenerator implements CodeGenerator {
    private final PojoConfig config;

    public PojoGenerator(PojoConfig config) {
        this.config = config;
    }

    @Override
    public void generate() throws IOException {
        Path outputDir = Paths.get(config.getOutputDir());
        
        for (PojoDefinition definition : config.getDefinitions()) {
            String pojoCode = generatePojoCode(definition);
            String fileName = definition.getClassName() + ".java";
            Path filePath = outputDir.resolve(toPath(config.getPackageName())).resolve(fileName);
            FileWriterUtil.writeFile(filePath, pojoCode, false);
            System.out.println("Generated POJO: " + filePath);
        }
        
        System.out.println("POJO generation completed!");
    }

    private String generatePojoCode(PojoDefinition definition) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("package ").append(config.getPackageName()).append(";\n\n");
        
        if (config.isUseLombok()) {
            sb.append("import lombok.Data;\n");
            sb.append("import lombok.NoArgsConstructor;\n");
            sb.append("import lombok.AllArgsConstructor;\n\n");
        }
        
        sb.append("/**\n");
        sb.append(" * ").append(definition.getDescription()).append("\n");
        sb.append(" */\n");
        
        if (config.isUseLombok()) {
            sb.append("@Data\n");
            sb.append("@NoArgsConstructor\n");
            sb.append("@AllArgsConstructor\n");
        }
        
        sb.append("public class ").append(definition.getClassName()).append(" {\n\n");
        
        for (PojoField field : definition.getFields()) {
            sb.append("    private ").append(field.getType()).append(" ").append(field.getName()).append(";\n");
        }
        
        sb.append("\n");
        
        if (!config.isUseLombok()) {
            for (PojoField field : definition.getFields()) {
                String getterName = "get" + capitalize(field.getName());
                sb.append("    public ").append(field.getType()).append(" ").append(getterName).append("() {\n");
                sb.append("        return ").append(field.getName()).append(";\n");
                sb.append("    }\n\n");
                
                String setterName = "set" + capitalize(field.getName());
                sb.append("    public void ").append(setterName).append("(").append(field.getType()).append(" ").append(field.getName()).append(") {\n");
                sb.append("        this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
                sb.append("    }\n\n");
            }
        }
        
        sb.append("}\n");
        
        return sb.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String toPath(String packageName) {
        return packageName.replace('.', '/');
    }

    public static class PojoConfig {
        private String packageName;
        private String outputDir = ".";
        private boolean useLombok = false;
        private List<PojoDefinition> definitions = new ArrayList<>();

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(String outputDir) {
            this.outputDir = outputDir;
        }

        public boolean isUseLombok() {
            return useLombok;
        }

        public void setUseLombok(boolean useLombok) {
            this.useLombok = useLombok;
        }

        public List<PojoDefinition> getDefinitions() {
            return definitions;
        }

        public void setDefinitions(List<PojoDefinition> definitions) {
            this.definitions = definitions;
        }

        public void addDefinition(PojoDefinition definition) {
            this.definitions.add(definition);
        }
    }

    public static class PojoDefinition {
        private String className;
        private String description;
        private List<PojoField> fields = new ArrayList<>();

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<PojoField> getFields() {
            return fields;
        }

        public void setFields(List<PojoField> fields) {
            this.fields = fields;
        }

        public void addField(PojoField field) {
            this.fields.add(field);
        }
    }

    public static class PojoField {
        private String name;
        private String type;

        public PojoField(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
