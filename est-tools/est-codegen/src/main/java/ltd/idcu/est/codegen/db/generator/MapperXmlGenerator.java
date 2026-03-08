package ltd.idcu.est.codegen.db.generator;

import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.metadata.Column;
import ltd.idcu.est.codegen.db.metadata.Table;
import ltd.idcu.est.codegen.db.util.NamingUtils;

public class MapperXmlGenerator {
    
    private final GeneratorConfig config;
    
    public MapperXmlGenerator(GeneratorConfig config) {
        this.config = config;
    }
    
    public String generate(Table table) {
        StringBuilder sb = new StringBuilder();
        String className = table.getClassName();
        String tableName = table.getName();
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"").append(config.getRepositoryPackage()).append(".").append(className).append("Repository\">\n\n");
        
        sb.append("    <resultMap id=\"BaseResultMap\" type=\"").append(config.getEntityPackage()).append(".").append(className).append("\">\n");
        for (Column column : table.getColumns()) {
            String property = NamingUtils.toCamelCase(column.getColumnName(), false);
            if (column.isPrimaryKey()) {
                sb.append("        <id column=\"").append(column.getColumnName()).append("\" property=\"").append(property).append("\"/>\n");
            } else {
                sb.append("        <result column=\"").append(column.getColumnName()).append("\" property=\"").append(property).append("\"/>\n");
            }
        }
        sb.append("    </resultMap>\n\n");
        
        sb.append("    <sql id=\"Base_Column_List\">\n");
        sb.append("        ");
        boolean first = true;
        for (Column column : table.getColumns()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(column.getColumnName());
            first = false;
        }
        sb.append("\n");
        sb.append("    </sql>\n\n");
        
        sb.append("    <select id=\"findById\" resultMap=\"BaseResultMap\">\n");
        sb.append("        SELECT\n");
        sb.append("        <include refid=\"Base_Column_List\"/>\n");
        sb.append("        FROM ").append(tableName).append("\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        WHERE ").append(table.getPrimaryKey().getColumnName()).append(" = #{id}\n");
        }
        sb.append("    </select>\n\n");
        
        sb.append("    <select id=\"findAll\" resultMap=\"BaseResultMap\">\n");
        sb.append("        SELECT\n");
        sb.append("        <include refid=\"Base_Column_List\"/>\n");
        sb.append("        FROM ").append(tableName).append("\n");
        sb.append("    </select>\n\n");
        
        sb.append("    <insert id=\"insert\" parameterType=\"").append(config.getEntityPackage()).append(".").append(className).append("\"");
        if (table.getPrimaryKey() != null && table.getPrimaryKey().isAutoIncrement()) {
            sb.append(" useGeneratedKeys=\"true\" keyProperty=\"").append(NamingUtils.toCamelCase(table.getPrimaryKey().getColumnName(), false)).append("\"");
        }
        sb.append(">\n");
        sb.append("        INSERT INTO ").append(tableName).append(" (\n");
        first = true;
        for (Column column : table.getColumns()) {
            if (!column.isAutoIncrement()) {
                if (!first) {
                    sb.append("            , ");
                } else {
                    sb.append("            ");
                }
                sb.append(column.getColumnName());
                first = false;
            }
        }
        sb.append("\n        ) VALUES (\n");
        first = true;
        for (Column column : table.getColumns()) {
            if (!column.isAutoIncrement()) {
                if (!first) {
                    sb.append("            , ");
                } else {
                    sb.append("            ");
                }
                sb.append("#{").append(NamingUtils.toCamelCase(column.getColumnName(), false)).append("}");
                first = false;
            }
        }
        sb.append("\n        )\n");
        sb.append("    </insert>\n\n");
        
        sb.append("    <update id=\"update\" parameterType=\"").append(config.getEntityPackage()).append(".").append(className).append("\">\n");
        sb.append("        UPDATE ").append(tableName).append("\n");
        sb.append("        <set>\n");
        for (Column column : table.getColumns()) {
            if (!column.isPrimaryKey()) {
                sb.append("            <if test=\"").append(NamingUtils.toCamelCase(column.getColumnName(), false)).append(" != null\">\n");
                sb.append("                ").append(column.getColumnName()).append(" = #{").append(NamingUtils.toCamelCase(column.getColumnName(), false)).append("},\n");
                sb.append("            </if>\n");
            }
        }
        sb.append("        </set>\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        WHERE ").append(table.getPrimaryKey().getColumnName()).append(" = #{").append(NamingUtils.toCamelCase(table.getPrimaryKey().getColumnName(), false)).append("}\n");
        }
        sb.append("    </update>\n\n");
        
        sb.append("    <delete id=\"delete\">\n");
        sb.append("        DELETE FROM ").append(tableName).append("\n");
        if (table.getPrimaryKey() != null) {
            sb.append("        WHERE ").append(table.getPrimaryKey().getColumnName()).append(" = #{id}\n");
        }
        sb.append("    </delete>\n\n");
        
        sb.append("</mapper>\n");
        
        return sb.toString();
    }
}
