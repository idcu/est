package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.DataPermissionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDataPermissionService implements DataPermissionService {
    
    private final Map<String, DataPermissionRule> rules = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> roleRules = new ConcurrentHashMap<>();
    
    public DefaultDataPermissionService() {
        initializeSampleRules();
    }
    
    private void initializeSampleRules() {
        try {
            DataPermissionRule salesRule = createRule(
                "sales-department-only",
                "销售部门数据权限",
                "仅允许访问销售部门的数据",
                DataPermissionRuleType.ROW_LEVEL
            );
            addRowCondition(salesRule.getId(), "department", "=", "Sales");
            
            DataPermissionRule hideSalaryRule = createRule(
                "hide-salary-field",
                "隐藏薪资字段",
                "对普通员工隐藏薪资字段",
                DataPermissionRuleType.FIELD_LEVEL
            );
            addFieldMask(hideSalaryRule.getId(), "salary", FieldMaskType.HIDDEN);
            
        } catch (Exception e) {
            System.err.println("Failed to initialize sample data permission rules: " + e.getMessage());
        }
    }
    
    private DataPermissionRule createRule(String id, String name, String description, DataPermissionRuleType type) {
        DefaultDataPermissionRule rule = new DefaultDataPermissionRule(id, name, description, type);
        rules.put(id, rule);
        return rule;
    }
    
    @Override
    public DataPermissionRule createRule(String name, String description, DataPermissionRuleType type) {
        String id = UUID.randomUUID().toString();
        return createRule(id, name, description, type);
    }
    
    @Override
    public DataPermissionRule updateRule(String ruleId, String name, String description) {
        DefaultDataPermissionRule rule = (DefaultDataPermissionRule) rules.get(ruleId);
        if (rule != null) {
            rule.name = name;
            rule.description = description;
        }
        return rule;
    }
    
    @Override
    public void deleteRule(String ruleId) {
        rules.remove(ruleId);
        roleRules.values().forEach(set -> set.remove(ruleId));
    }
    
    @Override
    public DataPermissionRule getRule(String ruleId) {
        return rules.get(ruleId);
    }
    
    @Override
    public List<DataPermissionRule> getAllRules() {
        return new ArrayList<>(rules.values());
    }
    
    @Override
    public void assignRuleToRole(String ruleId, String roleId) {
        roleRules.computeIfAbsent(roleId, k -> new HashSet<>()).add(ruleId);
    }
    
    @Override
    public void removeRuleFromRole(String ruleId, String roleId) {
        Set<String> ruleSet = roleRules.get(roleId);
        if (ruleSet != null) {
            ruleSet.remove(ruleId);
        }
    }
    
    @Override
    public List<DataPermissionRule> getRulesByRole(String roleId) {
        Set<String> ruleIds = roleRules.getOrDefault(roleId, new HashSet<>());
        List<DataPermissionRule> result = new ArrayList<>();
        for (String ruleId : ruleIds) {
            DataPermissionRule rule = rules.get(ruleId);
            if (rule != null) {
                result.add(rule);
            }
        }
        return result;
    }
    
    @Override
    public boolean hasRowPermission(String userId, String resourceType, String resourceId) {
        return true;
    }
    
    @Override
    public boolean hasFieldPermission(String userId, String resourceType, String fieldName) {
        return true;
    }
    
    @Override
    public Set<String> getAccessibleFields(String userId, String resourceType) {
        Set<String> fields = new HashSet<>();
        fields.add("id");
        fields.add("name");
        fields.add("email");
        return fields;
    }
    
    @Override
    public Set<String> getAccessibleResourceIds(String userId, String resourceType) {
        return new HashSet<>();
    }
    
    @Override
    public void addRowCondition(String ruleId, String field, String operator, Object value) {
        DefaultDataPermissionRule rule = (DefaultDataPermissionRule) rules.get(ruleId);
        if (rule != null) {
            rule.rowConditions.add(new DefaultRowCondition(field, operator, value));
        }
    }
    
    @Override
    public void addFieldMask(String ruleId, String field, FieldMaskType maskType) {
        DefaultDataPermissionRule rule = (DefaultDataPermissionRule) rules.get(ruleId);
        if (rule != null) {
            rule.fieldMasks.add(new DefaultFieldMask(field, maskType));
        }
    }
    
    private static class DefaultDataPermissionRule implements DataPermissionRule {
        private final String id;
        private String name;
        private String description;
        private final DataPermissionRuleType type;
        private final List<RowCondition> rowConditions = new ArrayList<>();
        private final List<FieldMask> fieldMasks = new ArrayList<>();
        
        public DefaultDataPermissionRule(String id, String name, String description, DataPermissionRuleType type) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
        }
        
        @Override
        public String getId() { return id; }
        
        @Override
        public String getName() { return name; }
        
        @Override
        public String getDescription() { return description; }
        
        @Override
        public DataPermissionRuleType getType() { return type; }
        
        @Override
        public List<RowCondition> getRowConditions() { return new ArrayList<>(rowConditions); }
        
        @Override
        public List<FieldMask> getFieldMasks() { return new ArrayList<>(fieldMasks); }
    }
    
    private static class DefaultRowCondition implements RowCondition {
        private final String field;
        private final String operator;
        private final Object value;
        
        public DefaultRowCondition(String field, String operator, Object value) {
            this.field = field;
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        public String getField() { return field; }
        
        @Override
        public String getOperator() { return operator; }
        
        @Override
        public Object getValue() { return value; }
    }
    
    private static class DefaultFieldMask implements FieldMask {
        private final String field;
        private final FieldMaskType maskType;
        
        public DefaultFieldMask(String field, FieldMaskType maskType) {
            this.field = field;
            this.maskType = maskType;
        }
        
        @Override
        public String getField() { return field; }
        
        @Override
        public FieldMaskType getMaskType() { return maskType; }
    }
}
