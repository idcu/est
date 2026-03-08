package ltd.idcu.est.codegen.db.generator;

import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.metadata.Column;
import ltd.idcu.est.codegen.db.metadata.Table;
import ltd.idcu.est.codegen.db.util.NamingUtils;

public class TestGenerator {
    
    private final GeneratorConfig config;
    
    public TestGenerator(GeneratorConfig config) {
        this.config = config;
    }
    
    public String generate(Table table) {
        StringBuilder sb = new StringBuilder();
        String className = table.getClassName();
        String varName = NamingUtils.toCamelCase(className, false);
        
        sb.append("package ").append(config.getTestPackage()).append(";\n\n");
        sb.append("import org.junit.jupiter.api.Test;\n");
        sb.append("import org.junit.jupiter.api.BeforeEach;\n");
        sb.append("import static org.junit.jupiter.api.Assertions.*;\n");
        sb.append("import ").append(config.getEntityPackage()).append(".").append(className).append(";\n");
        sb.append("import ").append(config.getServicePackage()).append(".").append(className).append("Service;\n");
        sb.append("import java.util.List;\n\n");
        
        sb.append("public class ").append(className).append("ServiceTest {\n\n");
        sb.append("    private ").append(className).append("Service service;\n\n");
        
        sb.append("    @BeforeEach\n");
        sb.append("    void setUp() {\n");
        sb.append("        service = new ").append(className).append("Service();\n");
        sb.append("    }\n\n");
        
        sb.append("    @Test\n");
        sb.append("    void testCreate() {\n");
        sb.append("        ").append(className).append(" ").append(varName).append(" = createTest").append(className).append("();\n");
        sb.append("        ").append(className).append(" saved = service.create(").append(varName).append(");\n");
        sb.append("        assertNotNull(saved);\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        assertNotNull(saved.get").append(NamingUtils.toCamelCase(table.getPrimaryKey().getColumnName(), true)).append("());\n");
        }
        sb.append("    }\n\n");
        
        sb.append("    @Test\n");
        sb.append("    void testFindById() {\n");
        sb.append("        ").append(className).append(" ").append(varName).append(" = createTest").append(className).append("();\n");
        sb.append("        ").append(className).append(" saved = service.create(").append(varName).append(");\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        ").append(className).append(" found = service.findById(saved.get").append(NamingUtils.toCamelCase(table.getPrimaryKey().getColumnName(), true)).append("());\n");
            sb.append("        assertNotNull(found);\n");
        }
        sb.append("    }\n\n");
        
        sb.append("    @Test\n");
        sb.append("    void testFindAll() {\n");
        sb.append("        service.create(createTest").append(className).append("());\n");
        sb.append("        service.create(createTest").append(className).append("());\n");
        sb.append("        List<").append(className).append("> list = service.findAll();\n");
        sb.append("        assertFalse(list.isEmpty());\n");
        sb.append("    }\n\n");
        
        sb.append("    @Test\n");
        sb.append("    void testUpdate() {\n");
        sb.append("        ").append(className).append(" ").append(varName).append(" = createTest").append(className).append("();\n");
        sb.append("        ").append(className).append(" saved = service.create(").append(varName).append(");\n");
        sb.append("        ").append(className).append(" updated = service.update(saved);\n");
        sb.append("        assertNotNull(updated);\n");
        sb.append("    }\n\n");
        
        sb.append("    @Test\n");
        sb.append("    void testDelete() {\n");
        sb.append("        ").append(className).append(" ").append(varName).append(" = createTest").append(className).append("();\n");
        sb.append("        ").append(className).append(" saved = service.create(").append(varName).append(");\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        boolean deleted = service.delete(saved.get").append(NamingUtils.toCamelCase(table.getPrimaryKey().getColumnName(), true)).append("());\n");
            sb.append("        assertTrue(deleted);\n");
        }
        sb.append("    }\n\n");
        
        sb.append("    private ").append(className).append(" createTest").append(className).append("() {\n");
        sb.append("        ").append(className).append(" ").append(varName).append(" = new ").append(className).append("();\n");
        
        for (Column column : table.getColumns()) {
            if (!column.isPrimaryKey() || !column.isAutoIncrement()) {
                String fieldName = NamingUtils.toCamelCase(column.getColumnName(), false);
                String setterName = "set" + NamingUtils.toCamelCase(column.getColumnName(), true);
                String defaultValue = getDefaultValue(column);
                if (defaultValue != null) {
                    sb.append("        ").append(varName).append(".").append(setterName).append("(").append(defaultValue).append(");\n");
                }
            }
        }
        
        sb.append("        return ").append(varName).append(";\n");
        sb.append("    }\n");
        
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private String getDefaultValue(Column column) {
        String javaType = column.getJavaType();
        switch (javaType) {
            case "String":
                return "\"test\"";
            case "Integer":
            case "int":
                return "1";
            case "Long":
            case "long":
                return "1L";
            case "Boolean":
            case "boolean":
                return "true";
            case "java.math.BigDecimal":
                return "new java.math.BigDecimal(\"100.00\")";
            case "java.time.LocalDateTime":
                return "java.time.LocalDateTime.now()";
            case "java.time.LocalDate":
                return "java.time.LocalDate.now()";
            default:
                return null;
        }
    }
}
