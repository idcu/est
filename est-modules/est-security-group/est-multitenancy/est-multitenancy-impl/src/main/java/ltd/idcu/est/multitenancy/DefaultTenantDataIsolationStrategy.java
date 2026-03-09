package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantDataIsolationStrategy;
import ltd.idcu.est.rbac.api.Tenant;

import java.util.EnumSet;
import java.util.Set;

public class DefaultTenantDataIsolationStrategy implements TenantDataIsolationStrategy {
    
    private static final String DEFAULT_TENANT_COLUMN = "tenant_id";
    private static final String DEFAULT_TENANT_SCHEMA_PREFIX = "tenant_";
    private static final String DEFAULT_TENANT_DATABASE_PREFIX = "tenant_";
    
    private final String tenantColumnName;
    private final String tenantSchemaPrefix;
    private final String tenantDatabasePrefix;
    
    public DefaultTenantDataIsolationStrategy() {
        this(DEFAULT_TENANT_COLUMN, DEFAULT_TENANT_SCHEMA_PREFIX, DEFAULT_TENANT_DATABASE_PREFIX);
    }
    
    public DefaultTenantDataIsolationStrategy(String tenantColumnName, String tenantSchemaPrefix, String tenantDatabasePrefix) {
        this.tenantColumnName = tenantColumnName;
        this.tenantSchemaPrefix = tenantSchemaPrefix;
        this.tenantDatabasePrefix = tenantDatabasePrefix;
    }
    
    @Override
    public String getTenantColumnName() {
        return tenantColumnName;
    }
    
    @Override
    public String getTenantColumnValue(Tenant tenant) {
        return tenant != null ? tenant.getId() : null;
    }
    
    @Override
    public String applyTenantFilter(String sql, Tenant tenant) {
        if (tenant == null) {
            return sql;
        }
        
        String tenantId = tenant.getId();
        String upperSql = sql.toUpperCase().trim();
        
        if (upperSql.startsWith("SELECT")) {
            return applySelectTenantFilter(sql, tenantId);
        } else if (upperSql.startsWith("INSERT")) {
            return applyInsertTenantFilter(sql, tenantId);
        } else if (upperSql.startsWith("UPDATE")) {
            return applyUpdateTenantFilter(sql, tenantId);
        } else if (upperSql.startsWith("DELETE")) {
            return applyDeleteTenantFilter(sql, tenantId);
        }
        
        return sql;
    }
    
    private String applySelectTenantFilter(String sql, String tenantId) {
        int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex == -1) {
            return sql + " WHERE " + tenantColumnName + " = '" + tenantId + "'";
        } else {
            return sql.substring(0, whereIndex + 7) + 
                   tenantColumnName + " = '" + tenantId + "' AND " + 
                   sql.substring(whereIndex + 7);
        }
    }
    
    private String applyInsertTenantFilter(String sql, String tenantId) {
        int valuesIndex = sql.toUpperCase().indexOf(" VALUES ");
        if (valuesIndex != -1) {
            String beforeValues = sql.substring(0, valuesIndex);
            String afterValues = sql.substring(valuesIndex);
            
            if (beforeValues.contains("(") && beforeValues.contains(")")) {
                int firstParen = beforeValues.indexOf('(');
                int lastParen = beforeValues.lastIndexOf(')');
                String columns = beforeValues.substring(firstParen + 1, lastParen);
                String newColumns = columns + ", " + tenantColumnName;
                beforeValues = beforeValues.substring(0, firstParen + 1) + newColumns + beforeValues.substring(lastParen);
                
                int firstValueParen = afterValues.indexOf('(');
                int lastValueParen = afterValues.lastIndexOf(')');
                String values = afterValues.substring(firstValueParen + 1, lastValueParen);
                String newValues = values + ", '" + tenantId + "'";
                afterValues = afterValues.substring(0, firstValueParen + 1) + newValues + afterValues.substring(lastValueParen);
                
                return beforeValues + afterValues;
            }
        }
        return sql;
    }
    
    private String applyUpdateTenantFilter(String sql, String tenantId) {
        int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex == -1) {
            return sql + " WHERE " + tenantColumnName + " = '" + tenantId + "'";
        } else {
            return sql.substring(0, whereIndex + 7) + 
                   tenantColumnName + " = '" + tenantId + "' AND " + 
                   sql.substring(whereIndex + 7);
        }
    }
    
    private String applyDeleteTenantFilter(String sql, String tenantId) {
        int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex == -1) {
            return sql + " WHERE " + tenantColumnName + " = '" + tenantId + "'";
        } else {
            return sql.substring(0, whereIndex + 7) + 
                   tenantColumnName + " = '" + tenantId + "' AND " + 
                   sql.substring(whereIndex + 7);
        }
    }
    
    @Override
    public String applyTenantSchema(String sql, Tenant tenant) {
        if (tenant == null) {
            return sql;
        }
        String schemaName = tenantSchemaPrefix + tenant.getCode();
        return sql.replaceAll("(?i)FROM\\s+(\\w+)", "FROM " + schemaName + ".$1")
                  .replaceAll("(?i)INTO\\s+(\\w+)", "INTO " + schemaName + ".$1")
                  .replaceAll("(?i)UPDATE\\s+(\\w+)", "UPDATE " + schemaName + ".$1")
                  .replaceAll("(?i)DELETE\\s+FROM\\s+(\\w+)", "DELETE FROM " + schemaName + ".$1");
    }
    
    @Override
    public String applyTenantDatabase(String sql, Tenant tenant) {
        if (tenant == null) {
            return sql;
        }
        String dbName = tenantDatabasePrefix + tenant.getCode();
        return "USE " + dbName + ";\n" + sql;
    }
    
    @Override
    public boolean supports(Tenant.TenantMode mode) {
        Set<Tenant.TenantMode> supportedModes = EnumSet.allOf(Tenant.TenantMode.class);
        return supportedModes.contains(mode);
    }
}
