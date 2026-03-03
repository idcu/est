package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcQuery<T> implements Query<T> {
    
    private final Connection connection;
    private final Class<T> entityClass;
    private final EntityMapper<T> mapper;
    private final String tableName;
    private final List<QueryCondition> conditions;
    private final List<OrderBy> orderBys;
    private final List<String> selectFields;
    private final List<String> groupByFields;
    private Integer limitValue;
    private Integer offsetValue;
    private boolean distinctFlag;
    
    public JdbcQuery(Connection connection, Class<T> entityClass, EntityMapper<T> mapper, String tableName) {
        this.connection = connection;
        this.entityClass = entityClass;
        this.mapper = mapper;
        this.tableName = tableName;
        this.conditions = new ArrayList<>();
        this.orderBys = new ArrayList<>();
        this.selectFields = new ArrayList<>();
        this.groupByFields = new ArrayList<>();
    }
    
    @Override
    public Query<T> where(String field, Object value) {
        return where(field, "=", value);
    }
    
    @Override
    public Query<T> where(String field, String operator, Object value) {
        conditions.add(new QueryCondition(field, operator, value, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereIn(String field, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        conditions.add(new QueryCondition(field, "IN", valueList, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotIn(String field, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        conditions.add(new QueryCondition(field, "NOT IN", valueList, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNull(String field) {
        conditions.add(new QueryCondition(field, "IS NULL", null, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotNull(String field) {
        conditions.add(new QueryCondition(field, "IS NOT NULL", null, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereLike(String field, String pattern) {
        conditions.add(new QueryCondition(field, "LIKE", pattern, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereBetween(String field, Object start, Object end) {
        conditions.add(new QueryCondition(field, "BETWEEN", new Object[]{start, end}, "AND"));
        return this;
    }
    
    @Override
    public Query<T> and() {
        if (!conditions.isEmpty()) {
            conditions.get(conditions.size() - 1).logicalOperator = "AND";
        }
        return this;
    }
    
    @Override
    public Query<T> or() {
        if (!conditions.isEmpty()) {
            conditions.get(conditions.size() - 1).logicalOperator = "OR";
        }
        return this;
    }
    
    @Override
    public Query<T> orderBy(String field) {
        return orderBy(field, true);
    }
    
    @Override
    public Query<T> orderBy(String field, boolean ascending) {
        orderBys.add(new OrderBy(field, ascending));
        return this;
    }
    
    @Override
    public Query<T> limit(int limit) {
        this.limitValue = limit;
        return this;
    }
    
    @Override
    public Query<T> offset(int offset) {
        this.offsetValue = offset;
        return this;
    }
    
    @Override
    public Query<T> select(String... fields) {
        this.selectFields.addAll(Arrays.asList(fields));
        return this;
    }
    
    @Override
    public Query<T> distinct() {
        this.distinctFlag = true;
        return this;
    }
    
    @Override
    public Query<T> groupBy(String... fields) {
        this.groupByFields.addAll(Arrays.asList(fields));
        return this;
    }
    
    @Override
    public Query<T> having(String field, String operator, Object value) {
        throw new UnsupportedOperationException("Having clause not implemented");
    }
    
    @Override
    public List<T> get() {
        String sql = buildSelectSql();
        List<Object> params = buildParams();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to execute query: " + sql, e);
        }
    }
    
    @Override
    public Optional<T> first() {
        if (limitValue == null) {
            limit(1);
        }
        List<T> result = get();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    
    @Override
    public T firstOrNull() {
        return first().orElse(null);
    }
    
    @Override
    public Optional<T> find(Object id) {
        return where("id", id).first();
    }
    
    @Override
    public long count() {
        String sql = buildCountSql();
        List<Object> params = buildParams();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataException("Failed to execute count query: " + sql, e);
        }
    }
    
    @Override
    public boolean exists() {
        return count() > 0;
    }
    
    @Override
    public Object max(String field) {
        String sql = buildAggregateSql("MAX", field);
        return executeAggregate(sql);
    }
    
    @Override
    public Object min(String field) {
        String sql = buildAggregateSql("MIN", field);
        return executeAggregate(sql);
    }
    
    @Override
    public Object sum(String field) {
        String sql = buildAggregateSql("SUM", field);
        return executeAggregate(sql);
    }
    
    @Override
    public Object avg(String field) {
        String sql = buildAggregateSql("AVG", field);
        return executeAggregate(sql);
    }
    
    @Override
    public int update(Map<String, Object> values) {
        String sql = buildUpdateSql(values);
        List<Object> params = new ArrayList<>(values.values());
        params.addAll(buildParams());
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute update: " + sql, e);
        }
    }
    
    @Override
    public int delete() {
        String sql = buildDeleteSql();
        List<Object> params = buildParams();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute delete: " + sql, e);
        }
    }
    
    @Override
    public String toSql() {
        return buildSelectSql();
    }
    
    @Override
    public Map<String, Object> getBindings() {
        Map<String, Object> bindings = new LinkedHashMap<>();
        for (QueryCondition condition : conditions) {
            if (condition.value != null) {
                bindings.put(condition.field, condition.value);
            }
        }
        return bindings;
    }
    
    private String buildSelectSql() {
        StringBuilder sql = new StringBuilder("SELECT ");
        
        if (distinctFlag) {
            sql.append("DISTINCT ");
        }
        
        if (selectFields.isEmpty()) {
            sql.append("*");
        } else {
            sql.append(String.join(", ", selectFields));
        }
        
        sql.append(" FROM ").append(tableName);
        
        appendWhereClause(sql);
        appendOrderByClause(sql);
        appendLimitOffsetClause(sql);
        
        return sql.toString();
    }
    
    private String buildCountSql() {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(tableName);
        appendWhereClause(sql);
        return sql.toString();
    }
    
    private String buildAggregateSql(String func, String field) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(func).append("(").append(field).append(") FROM ");
        sql.append(tableName);
        appendWhereClause(sql);
        return sql.toString();
    }
    
    private String buildUpdateSql(Map<String, Object> values) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        sql.append(values.keySet().stream()
                .map(k -> k + " = ?")
                .collect(Collectors.joining(", ")));
        appendWhereClause(sql);
        return sql.toString();
    }
    
    private String buildDeleteSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(tableName);
        appendWhereClause(sql);
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
    
    private void appendOrderByClause(StringBuilder sql) {
        if (orderBys.isEmpty()) {
            return;
        }
        
        sql.append(" ORDER BY ");
        sql.append(orderBys.stream()
                .map(o -> o.field + (o.ascending ? " ASC" : " DESC"))
                .collect(Collectors.joining(", ")));
    }
    
    private void appendLimitOffsetClause(StringBuilder sql) {
        if (limitValue != null) {
            sql.append(" LIMIT ").append(limitValue);
        }
        if (offsetValue != null) {
            sql.append(" OFFSET ").append(offsetValue);
        }
    }
    
    private List<Object> buildParams() {
        List<Object> params = new ArrayList<>();
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
    
    private Object executeAggregate(String sql) {
        List<Object> params = buildParams();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } catch (SQLException e) {
            throw new DataException("Failed to execute aggregate query: " + sql, e);
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
    
    private static class OrderBy {
        String field;
        boolean ascending;
        
        OrderBy(String field, boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }
    }
}
