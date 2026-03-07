package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcLambdaUpdateWrapper<T> implements LambdaUpdateWrapper<T> {

    private final Connection connection;
    private final String tableName;
    private final List<UpdateField> updateFields;
    private final List<QueryCondition> conditions;
    private String lastSql;

    public JdbcLambdaUpdateWrapper(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        this.updateFields = new ArrayList<>();
        this.conditions = new ArrayList<>();
    }

    @Override
    public <V> LambdaUpdateWrapper<T> set(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        updateFields.add(new UpdateField(LambdaUtils.getColumnName(column), value));
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> setSql(String sql) {
        updateFields.add(new UpdateField(sql, null, true));
        return this;
    }

    @Override
    public <V> LambdaUpdateWrapper<T> eq(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaUpdateWrapper<T> ne(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "!=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaUpdateWrapper<T> gt(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), ">", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaUpdateWrapper<T> ge(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), ">=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaUpdateWrapper<T> lt(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "<", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaUpdateWrapper<T> le(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "<=", value, "AND");
        return this;
    }
    
    @Override
    public LambdaUpdateWrapper<T> like(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "LIKE", "%" + value + "%", "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> in(SFunction<T, ?> column, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        addCondition(LambdaUtils.getColumnName(column), "IN", valueList, "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> notIn(SFunction<T, ?> column, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        addCondition(LambdaUtils.getColumnName(column), "NOT IN", valueList, "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> between(SFunction<T, ?> column, Object start, Object end) {
        addCondition(LambdaUtils.getColumnName(column), "BETWEEN", new Object[]{start, end}, "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> isNull(SFunction<T, ?> column) {
        addCondition(LambdaUtils.getColumnName(column), "IS NULL", null, "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> isNotNull(SFunction<T, ?> column) {
        addCondition(LambdaUtils.getColumnName(column), "IS NOT NULL", null, "AND");
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> and() {
        if (!conditions.isEmpty()) {
            conditions.get(conditions.size() - 1).logicalOperator = "AND";
        }
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> or() {
        if (!conditions.isEmpty()) {
            conditions.get(conditions.size() - 1).logicalOperator = "OR";
        }
        return this;
    }

    @Override
    public LambdaUpdateWrapper<T> last(String lastSql) {
        this.lastSql = lastSql;
        return this;
    }

    @Override
    public int update() {
        if (updateFields.isEmpty()) {
            throw new DataException("No fields to update");
        }
        
        String sql = buildUpdateSql();
        List<Object> params = buildParams();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute update: " + sql, e);
        }
    }

    @Override
    public String toSql() {
        return buildUpdateSql();
    }

    @Override
    public Map<String, Object> getBindings() {
        Map<String, Object> bindings = new LinkedHashMap<>();
        for (UpdateField field : updateFields) {
            if (!field.isSql) {
                bindings.put(field.field, field.value);
            }
        }
        for (QueryCondition condition : conditions) {
            if (condition.value != null) {
                bindings.put(condition.field, condition.value);
            }
        }
        return bindings;
    }

    private void addCondition(String field, String operator, Object value, String logicalOperator) {
        conditions.add(new QueryCondition(field, operator, value, logicalOperator));
    }

    private String buildUpdateSql() {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        
        List<String> setClauses = new ArrayList<>();
        for (UpdateField field : updateFields) {
            if (field.isSql) {
                setClauses.add(field.field);
            } else {
                setClauses.add(field.field + " = ?");
            }
        }
        sql.append(String.join(", ", setClauses));
        
        appendWhereClause(sql);
        
        if (lastSql != null) {
            sql.append(" ").append(lastSql);
        }
        
        return sql.toString();
    }

    private void appendWhereClause(StringBuilder sql) {
        if (conditions.isEmpty()) {
            return;
        }
        
        sql.append(" WHERE ");
        for (int i = 0; i < conditions.size(); i++) {
            if (i > 0) {
                sql.append(" ").append(conditions.get(i - 1).logicalOperator).append(" ");
            }
            QueryCondition c = conditions.get(i);
            sql.append(c.field).append(" ").append(c.operator);
            
            if (c.value instanceof List<?> list) {
                sql.append(" (").append(Collections.nCopies(list.size(), "?").stream()
                        .collect(Collectors.joining(", "))).append(")");
            } else if (c.value instanceof Object[] arr) {
                sql.append(" ? AND ?");
            } else if (c.value != null) {
                sql.append(" ?");
            }
        }
    }

    private List<Object> buildParams() {
        List<Object> params = new ArrayList<>();
        for (UpdateField field : updateFields) {
            if (!field.isSql) {
                params.add(field.value);
            }
        }
        for (QueryCondition condition : conditions) {
            if (condition.value instanceof List<?> list) {
                params.addAll(list);
            } else if (condition.value instanceof Object[] arr) {
                params.add(arr[0]);
                params.add(arr[1]);
            } else if (condition.value != null) {
                params.add(condition.value);
            }
        }
        return params;
    }

    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
    }

    private static class UpdateField {
        String field;
        Object value;
        boolean isSql;
        
        UpdateField(String field, Object value) {
            this.field = field;
            this.value = value;
            this.isSql = false;
        }
        
        UpdateField(String field, Object value, boolean isSql) {
            this.field = field;
            this.value = value;
            this.isSql = isSql;
        }
    }

    private static class QueryCondition {
        String field;
        String operator;
        Object value;
        String logicalOperator;
        
        QueryCondition(String field, String operator, Object value, String logicalOperator) {
            this.field = field;
            this.operator = operator;
            this.value = value;
            this.logicalOperator = logicalOperator;
        }
    }
}
