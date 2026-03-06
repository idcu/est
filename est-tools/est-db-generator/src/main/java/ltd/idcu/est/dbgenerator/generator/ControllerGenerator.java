package ltd.idcu.est.dbgenerator.generator;

import ltd.idcu.est.dbgenerator.config.GeneratorConfig;
import ltd.idcu.est.dbgenerator.metadata.Column;
import ltd.idcu.est.dbgenerator.metadata.PrimaryKey;
import ltd.idcu.est.dbgenerator.metadata.Table;
import ltd.idcu.est.dbgenerator.type.TypeMapper;
import ltd.idcu.est.dbgenerator.util.NamingUtils;
import ltd.idcu.est.scaffold.TemplateEngine;

import java.io.IOException;

public class ControllerGenerator {

    private final GeneratorConfig config;

    public ControllerGenerator(GeneratorConfig config) {
        this.config = config;
    }

    public String generate(Table table) throws IOException {
        TemplateEngine engine = new TemplateEngine();
        
        String idType = getIdType(table);
        String resourcePath = NamingUtils.pluralize(NamingUtils.toCamelCase(table.getName()));
        
        engine.setVariable("package", config.getControllerPackage());
        engine.setVariable("className", table.getClassName());
        engine.setVariable("entityPackage", config.getEntityPackage());
        engine.setVariable("servicePackage", config.getServicePackage());
        engine.setVariable("idType", idType);
        engine.setVariable("resourcePath", resourcePath);

        return engine.renderFromFile("templates/controller.java.template");
    }

    private String getIdType(Table table) {
        PrimaryKey pk = table.getPrimaryKey();
        if (pk == null || pk.getColumns().isEmpty()) {
            return "Long";
        }
        
        Column idColumn = pk.getFirstColumn();
        String javaType = idColumn.getJavaType();
        String simpleType = TypeMapper.getFullyQualifiedType(javaType);
        
        if (simpleType.equals("Object")) {
            return "Long";
        }
        
        return simpleType;
    }
}
