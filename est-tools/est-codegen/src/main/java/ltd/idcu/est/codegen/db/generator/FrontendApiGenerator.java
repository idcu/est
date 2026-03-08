package ltd.idcu.est.codegen.db.generator;

import ltd.idcu.est.codegen.db.config.GeneratorConfig;
import ltd.idcu.est.codegen.db.metadata.Column;
import ltd.idcu.est.codegen.db.metadata.Table;

public class FrontendApiGenerator {
    
    private final GeneratorConfig config;
    
    public FrontendApiGenerator(GeneratorConfig config) {
        this.config = config;
    }
    
    public String generate(Table table) {
        String className = table.getClassName();
        String varName = toCamelCase(className);
        String resourcePath = toKebabCase(className);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("import request from '@/utils/request'\n\n");
        
        sb.append("export interface ").append(className).append("Info {\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            String tsType = toTsType(column.getJavaType());
            sb.append("  ").append(fieldName).append(": ").append(tsType).append("\n");
        }
        sb.append("}\n\n");
        
        sb.append("export interface ").append(className).append("Query {\n");
        sb.append("  page?: number\n");
        sb.append("  pageSize?: number\n");
        for (Column column : table.getColumns()) {
            String fieldName = toCamelCase(column.getName());
            String tsType = toTsType(column.getJavaType());
            sb.append("  ").append(fieldName).append("?: ").append(tsType).append("\n");
        }
        sb.append("}\n\n");
        
        sb.append("export function get").append(className).append("List(params: ").append(className).append("Query) {\n");
        sb.append("  return request.get<{ list: ").append(className).append("Info[]; total: number }>(`/api/").append(resourcePath).append("`, { params })\n");
        sb.append("}\n\n");
        
        sb.append("export function get").append(className).append("(id: string | number) {\n");
        sb.append("  return request.get<").append(className).append("Info>(`/api/").append(resourcePath).append("/${id}`)\n");
        sb.append("}\n\n");
        
        sb.append("export function create").append(className).append("(data: Partial<").append(className).append("Info>) {\n");
        sb.append("  return request.post(`/api/").append(resourcePath).append("`, data)\n");
        sb.append("}\n\n");
        
        sb.append("export function update").append(className).append("(id: string | number, data: Partial<").append(className).append("Info>) {\n");
        sb.append("  return request.put(`/api/").append(resourcePath).append("/${id}`, data)\n");
        sb.append("}\n\n");
        
        sb.append("export function delete").append(className).append("(id: string | number) {\n");
        sb.append("  return request.delete(`/api/").append(resourcePath).append("/${id}`)\n");
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] parts = input.split("_");
        StringBuilder result = new StringBuilder();
        result.append(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            result.append(Character.toUpperCase(parts[i].charAt(0)))
                  .append(parts[i].substring(1).toLowerCase());
        }
        return result.toString();
    }
    
    private String toKebabCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append("-");
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private String toTsType(String javaType) {
        if (javaType == null) {
            return "any";
        }
        return switch (javaType) {
            case "String" -> "string";
            case "Integer", "Long", "int", "long" -> "number";
            case "Boolean", "boolean" -> "boolean";
            case "Double", "Float", "double", "float", "BigDecimal" -> "number";
            case "Date", "LocalDateTime", "LocalDate" -> "string";
            default -> "any";
        };
    }
}
