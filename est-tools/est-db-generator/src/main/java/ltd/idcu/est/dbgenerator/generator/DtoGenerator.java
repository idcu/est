package ltd.idcu.est.dbgenerator.generator;

import ltd.idcu.est.dbgenerator.config.GeneratorConfig;
import ltd.idcu.est.dbgenerator.metadata.Column;
import ltd.idcu.est.dbgenerator.metadata.Table;
import ltd.idcu.est.dbgenerator.type.TypeMapper;
import ltd.idcu.est.scaffold.TemplateEngine;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoGenerator {

    private final GeneratorConfig config;

    public DtoGenerator(GeneratorConfig config) {
        this.config = config;
    }

    public String generate(Table table) throws IOException {
        TemplateEngine engine = new TemplateEngine();
        
        engine.setVariable("package", config.getDtoPackage());
        engine.setVariable("className", table.getClassName() + "Dto");
        engine.setVariable("fields", generateFields(table));
        engine.setVariable("gettersSetters", generateGettersSetters(table));

        return engine.renderFromFile("templates/dto.java.template");
    }

    private String generateFields(Table table) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (Column column : table.getColumns()) {
            if (!first) {
                sb.append("\n");
            }
            first = false;
            
            sb.append("    ");
            String simpleType = column.getSimpleJavaType();
            sb.append("private ").append(simpleType).append(" ").append(column.getFieldName()).append(";");
            
            if (column.getRemarks() != null && !column.getRemarks().isEmpty()) {
                sb.append(" // ").append(column.getRemarks());
            }
        }
        
        return sb.toString();
    }

    private String generateGettersSetters(Table table) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (Column column : table.getColumns()) {
            if (!first) {
                sb.append("\n");
            }
            first = false;
            
            String simpleType = column.getSimpleJavaType();
            String fieldName = column.getFieldName();
            String capitalizedFieldName = capitalize(fieldName);
            
            sb.append("\n    public ").append(simpleType).append(" get").append(capitalizedFieldName).append("() {");
            sb.append("\n        return ").append(fieldName).append(";");
            sb.append("\n    }");
            
            sb.append("\n");
            
            sb.append("\n    public void set").append(capitalizedFieldName).append("(").append(simpleType).append(" ").append(fieldName).append(") {");
            sb.append("\n        this.").append(fieldName).append(" = ").append(fieldName).append(";");
            sb.append("\n    }");
        }
        
        return sb.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
