package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataPermissionService {
    
    DataPermissionRule createRule(String name, String description, DataPermissionRuleType type);
    
    DataPermissionRule updateRule(String ruleId, String name, String description);
    
    void deleteRule(String ruleId);
    
    DataPermissionRule getRule(String ruleId);
    
    List<DataPermissionRule> getAllRules();
    
    void assignRuleToRole(String ruleId, String roleId);
    
    void removeRuleFromRole(String ruleId, String roleId);
    
    List<DataPermissionRule> getRulesByRole(String roleId);
    
    boolean hasRowPermission(String userId, String resourceType, String resourceId);
    
    boolean hasFieldPermission(String userId, String resourceType, String fieldName);
    
    Set<String> getAccessibleFields(String userId, String resourceType);
    
    Set<String> getAccessibleResourceIds(String userId, String resourceType);
    
    void addRowCondition(String ruleId, String field, String operator, Object value);
    
    void addFieldMask(String ruleId, String field, FieldMaskType maskType);
    
    enum DataPermissionRuleType {
        ROW_LEVEL,
        FIELD_LEVEL,
        COMBINED
    }
    
    enum FieldMaskType {
        HIDDEN,
        READ_ONLY,
        MASKED
    }
    
    interface DataPermissionRule {
        String getId();
        String getName();
        String getDescription();
        DataPermissionRuleType getType();
        List<RowCondition> getRowConditions();
        List<FieldMask> getFieldMasks();
    }
    
    interface RowCondition {
        String getField();
        String getOperator();
        Object getValue();
    }
    
    interface FieldMask {
        String getField();
        FieldMaskType getMaskType();
    }
}
