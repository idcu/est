package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JdbcLambdaQueryWrapper<T> implements LambdaQueryWrapper<T> {

    private final Connection connection;
    private final Class<T> entityClass;
    private final EntityMapper<T> mapper;
    private final String tableName;
    private final List<ConditionNode> conditionNodes;
    private final List<JoinClause> joinClauses;
    private final List<OrderBy> orderBys;
    private final List<String> selectFields;
    private final List<String> groupByFields;
    private final List<String> includeRelations;
    private final Dialect dialect;
    private final QueryMetrics metrics;
    private Integer limitValue;
    private Integer offsetValue;
    private boolean distinctFlag;
    private String lastSql;
    private final Stack<ConditionGroup> groupStack;

    public JdbcLambdaQueryWrapper(Connection connection, Class<T> entityClass, EntityMapper<T> mapper, String tableName) {
        this.connection = connection;
        this.entityClass = entityClass;
        this.mapper = mapper;
        this.tableName = tableName;
        this.conditionNodes = new ArrayList<>();
        this.joinClauses = new ArrayList<>();
        this.orderBys = new ArrayList<>();
        this.selectFields = new ArrayList<>();
        this.groupByFields = new ArrayList<>();
        this.includeRelations = new ArrayList<>();
        this.dialect = DialectFactory.getDialect(connection);
        this.metrics = new QueryMetrics();
        this.groupStack = new Stack<>();
    }

    private JdbcLambdaQueryWrapper(Connection connection, Class<T> entityClass, EntityMapper<T> mapper, String tableName,
                                   List<ConditionNode> conditionNodes, List<OrderBy> orderBys, List<String> selectFields,
                                   List<String> groupByFields, List<String> includeRelations, Dialect dialect, QueryMetrics metrics,
                                   Integer limitValue, Integer offsetValue, boolean distinctFlag, String lastSql) {
        this.connection = connection;
        this.entityClass = entityClass;
        this.mapper = mapper;
        this.tableName = tableName;
        this.conditionNodes = conditionNodes;
        this.joinClauses = new ArrayList<>();
        this.orderBys = orderBys;
        this.selectFields = selectFields;
        this.groupByFields = groupByFields;
        this.includeRelations = includeRelations;
        this.dialect = dialect;
        this.metrics = metrics;
        this.limitValue = limitValue;
        this.offsetValue = offsetValue;
        this.distinctFlag = distinctFlag;
        this.lastSql = lastSql;
        this.groupStack = new Stack<>();
    }

    @Override
    public <V> LambdaQueryWrapper<T> eq(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> ne(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "!=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> gt(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), ">", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> ge(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), ">=", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> lt(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "<", value, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> le(SFunction<T, V> column, V value) {
        LambdaUtils.validateType(column, value);
        addCondition(LambdaUtils.getColumnName(column), "<=", value, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> like(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "LIKE", "%" + value + "%", "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> likeLeft(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "LIKE", "%" + value, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> likeRight(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "LIKE", value + "%", "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> notLike(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "NOT LIKE", "%" + value + "%", "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> notLikeLeft(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "NOT LIKE", "%" + value, "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> notLikeRight(SFunction<T, ?> column, String value) {
        addCondition(LambdaUtils.getColumnName(column), "NOT LIKE", value + "%", "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> in(SFunction<T, ?> column, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        addCondition(LambdaUtils.getColumnName(column), "IN", valueList, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> notIn(SFunction<T, ?> column, Iterable<?> values) {
        List<Object> valueList = new ArrayList<>();
        values.forEach(valueList::add);
        addCondition(LambdaUtils.getColumnName(column), "NOT IN", valueList, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> between(SFunction<T, ?> column, Object start, Object end) {
        addCondition(LambdaUtils.getColumnName(column), "BETWEEN", new Object[]{start, end}, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> notBetween(SFunction<T, ?> column, Object start, Object end) {
        addCondition(LambdaUtils.getColumnName(column), "NOT BETWEEN", new Object[]{start, end}, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> isNull(SFunction<T, ?> column) {
        addCondition(LambdaUtils.getColumnName(column), "IS NULL", null, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> isNotNull(SFunction<T, ?> column) {
        addCondition(LambdaUtils.getColumnName(column), "IS NOT NULL", null, "AND");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> and() {
        if (!conditionNodes.isEmpty()) {
            ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
            if (lastNode instanceof QueryCondition) {
                ((QueryCondition) lastNode).logicalOperator = "AND";
            } else if (lastNode instanceof ConditionGroup) {
                ((ConditionGroup) lastNode).logicalOperator = "AND";
            }
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> or() {
        if (!conditionNodes.isEmpty()) {
            ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
            if (lastNode instanceof QueryCondition) {
                ((QueryCondition) lastNode).logicalOperator = "OR";
            } else if (lastNode instanceof ConditionGroup) {
                ((ConditionGroup) lastNode).logicalOperator = "OR";
            }
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> and(Consumer<LambdaQueryWrapper<T>> consumer) {
        ConditionGroup group = new ConditionGroup("AND");
        if (!groupStack.isEmpty()) {
            groupStack.peek().nodes.add(group);
        } else {
            if (!conditionNodes.isEmpty()) {
                ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
                if (lastNode instanceof QueryCondition) {
                    ((QueryCondition) lastNode).logicalOperator = "AND";
                } else if (lastNode instanceof ConditionGroup) {
                    ((ConditionGroup) lastNode).logicalOperator = "AND";
                }
            }
            conditionNodes.add(group);
        }
        groupStack.push(group);
        
        JdbcLambdaQueryWrapper<T> subQuery = createSubQuery();
        consumer.accept(subQuery);
        
        groupStack.pop();
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> or(Consumer<LambdaQueryWrapper<T>> consumer) {
        ConditionGroup group = new ConditionGroup("OR");
        if (!groupStack.isEmpty()) {
            groupStack.peek().nodes.add(group);
        } else {
            if (!conditionNodes.isEmpty()) {
                ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
                if (lastNode instanceof QueryCondition) {
                    ((QueryCondition) lastNode).logicalOperator = "OR";
                } else if (lastNode instanceof ConditionGroup) {
                    ((ConditionGroup) lastNode).logicalOperator = "OR";
                }
            }
            conditionNodes.add(group);
        }
        groupStack.push(group);
        
        JdbcLambdaQueryWrapper<T> subQuery = createSubQuery();
        consumer.accept(subQuery);
        
        groupStack.pop();
        return this;
    }

    private JdbcLambdaQueryWrapper<T> createSubQuery() {
        return new JdbcLambdaQueryWrapper<>(connection, entityClass, mapper, tableName,
                new ArrayList<>(), orderBys, selectFields, groupByFields, includeRelations,
                dialect, metrics, limitValue, offsetValue, distinctFlag, lastSql) {
            @Override
            protected void addCondition(String field, String operator, Object value, String logicalOperator) {
                if (!groupStack.isEmpty()) {
                    groupStack.peek().nodes.add(new QueryCondition(field, operator, value, logicalOperator));
                } else {
                    conditionNodes.add(new QueryCondition(field, operator, value, logicalOperator));
                }
            }
        };
    }

    protected void addCondition(String field, String operator, Object value, String logicalOperator) {
        if (!groupStack.isEmpty()) {
            groupStack.peek().nodes.add(new QueryCondition(field, operator, value, logicalOperator));
        } else {
            if (!conditionNodes.isEmpty()) {
                ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
                if (lastNode.logicalOperator == null) {
                    lastNode.logicalOperator = "AND";
                }
            }
            conditionNodes.add(new QueryCondition(field, operator, value, logicalOperator));
        }
    }

    @Override
    public LambdaQueryWrapper<T> orderBy(SFunction<T, ?> column) {
        return orderBy(column, true);
    }

    @Override
    public LambdaQueryWrapper<T> orderBy(SFunction<T, ?> column, boolean ascending) {
        orderBys.add(new OrderBy(LambdaUtils.getColumnName(column), ascending));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        return orderBy(column, true);
    }

    @Override
    public LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        return orderBy(column, false);
    }

    @Override
    public LambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        for (SFunction<T, ?> column : columns) {
            selectFields.add(LambdaUtils.getColumnName(column));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> distinct() {
        this.distinctFlag = true;
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> groupBy(SFunction<T, ?>... columns) {
        for (SFunction<T, ?> column : columns) {
            groupByFields.add(LambdaUtils.getColumnName(column));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> last(String lastSql) {
        this.lastSql = lastSql;
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> include(SFunction<T, ?>... relations) {
        for (SFunction<T, ?> relation : relations) {
            includeRelations.add(LambdaUtils.getFieldName(relation));
        }
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> in(SFunction<T, V> column, Query<?> subQuery) {
        String fieldName = LambdaUtils.getColumnName(column);
        addSubQueryCondition(fieldName, "IN", subQuery, "AND");
        return this;
    }
    
    @Override
    public <V> LambdaQueryWrapper<T> notIn(SFunction<T, V> column, Query<?> subQuery) {
        String fieldName = LambdaUtils.getColumnName(column);
        addSubQueryCondition(fieldName, "NOT IN", subQuery, "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> exists(Query<?> subQuery) {
        addExistsCondition(false, subQuery, "AND");
        return this;
    }
    
    @Override
    public LambdaQueryWrapper<T> notExists(Query<?> subQuery) {
        addExistsCondition(true, subQuery, "AND");
        return this;
    }
    
    private void addSubQueryCondition(String field, String operator, Query<?> subQuery, String logicalOperator) {
        SubQueryCondition condition = new SubQueryCondition(field, operator, subQuery, logicalOperator);
        if (!groupStack.isEmpty()) {
            groupStack.peek().nodes.add(condition);
        } else {
            if (!conditionNodes.isEmpty()) {
                ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
                if (lastNode.logicalOperator == null) {
                    lastNode.logicalOperator = "AND";
                }
            }
            conditionNodes.add(condition);
        }
    }
    
    private void addExistsCondition(boolean not, Query<?> subQuery, String logicalOperator) {
        ExistsCondition condition = new ExistsCondition(not, subQuery, logicalOperator);
        if (!groupStack.isEmpty()) {
            groupStack.peek().nodes.add(condition);
        } else {
            if (!conditionNodes.isEmpty()) {
                ConditionNode lastNode = conditionNodes.get(conditionNodes.size() - 1);
                if (lastNode.logicalOperator == null) {
                    lastNode.logicalOperator = "AND";
                }
            }
            conditionNodes.add(condition);
        }
    }
    
    @Override
    public LambdaQueryWrapper<T> join(String table, String leftField, String rightField) {
        return join(table, leftField, "=", rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> join(String table, String leftField, String operator, String rightField) {
        return addJoin("JOIN", table, leftField, operator, rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> leftJoin(String table, String leftField, String rightField) {
        return leftJoin(table, leftField, "=", rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> leftJoin(String table, String leftField, String operator, String rightField) {
        return addJoin("LEFT JOIN", table, leftField, operator, rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> rightJoin(String table, String leftField, String rightField) {
        return rightJoin(table, leftField, "=", rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> rightJoin(String table, String leftField, String operator, String rightField) {
        return addJoin("RIGHT JOIN", table, leftField, operator, rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> innerJoin(String table, String leftField, String rightField) {
        return innerJoin(table, leftField, "=", rightField);
    }
    
    @Override
    public LambdaQueryWrapper<T> innerJoin(String table, String leftField, String operator, String rightField) {
        return addJoin("INNER JOIN", table, leftField, operator, rightField);
    }
    
    private LambdaQueryWrapper<T> addJoin(String type, String table, String leftField, String operator, String rightField) {
        joinClauses.add(new JoinClause(type, table, leftField, operator, rightField));
        return this;
    }
    
    public QueryMetrics getMetrics() {
        return metrics;
    }

    @Override
    public LambdaQueryWrapper<T> limit(int limit) {
        this.limitValue = limit;
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> offset(int offset) {
        this.offsetValue = offset;
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> page(int pageNum, int pageSize) {
        this.offsetValue = (pageNum - 1) * pageSize;
        this.limitValue = pageSize;
        return this;
    }

    @Override
    public List<T> get() {
        long startTime = System.currentTimeMillis();
        try {
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
        } finally {
            long endTime = System.currentTimeMillis();
            metrics.recordQuery(endTime - startTime);
        }
    }

    @Override
    public T getOne() {
        return getOne(false);
    }

    @Override
    public T getOne(boolean throwEx) {
        if (limitValue == null) {
            limit(1);
        }
        List<T> result = get();
        if (result.isEmpty()) {
            if (throwEx) {
                throw new DataException("Expected one result but found none");
            }
            return null;
        }
        if (result.size() > 1 && throwEx) {
            throw new DataException("Expected one result but found " + result.size());
        }
        return result.get(0);
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
    public Object max(SFunction<T, ?> column) {
        return executeAggregate("MAX", LambdaUtils.getColumnName(column));
    }

    @Override
    public Object min(SFunction<T, ?> column) {
        return executeAggregate("MIN", LambdaUtils.getColumnName(column));
    }

    @Override
    public Object sum(SFunction<T, ?> column) {
        return executeAggregate("SUM", LambdaUtils.getColumnName(column));
    }

    @Override
    public Object avg(SFunction<T, ?> column) {
        return executeAggregate("AVG", LambdaUtils.getColumnName(column));
    }

    @Override
    public String toSql() {
        return buildSelectSql();
    }

    @Override
    public Map<String, Object> getBindings() {
        Map<String, Object> bindings = new LinkedHashMap<>();
        List<Object> params = buildParams();
        for (int i = 0; i < params.size(); i++) {
            bindings.put("param" + (i + 1), params.get(i));
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
        
        appendJoinClauses(sql);
        appendWhereClause(sql);
        appendGroupByClause(sql);
        appendOrderByClause(sql);
        
        if (lastSql != null) {
            sql.append(" ").append(lastSql);
        }
        
        String baseSql = sql.toString();
        
        if (limitValue != null) {
            int offset = offsetValue != null ? offsetValue : 0;
            return dialect.getLimitSql(baseSql, offset, limitValue);
        }
        
        return baseSql;
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

    private void appendJoinClauses(StringBuilder sql) {
        for (JoinClause join : joinClauses) {
            sql.append(" ").append(join.type).append(" ").append(join.table)
               .append(" ON ").append(join.leftField).append(" ").append(join.operator)
               .append(" ").append(join.rightField);
        }
    }
    
    private void appendWhereClause(StringBuilder sql) {
        if (conditionNodes.isEmpty()) {
            return;
        }
        
        sql.append(" WHERE ");
        appendConditionNodes(sql, conditionNodes);
    }

    private void appendConditionNodes(StringBuilder sql, List<ConditionNode> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            ConditionNode node = nodes.get(i);
            if (i > 0) {
                ConditionNode prevNode = nodes.get(i - 1);
                String op = prevNode.logicalOperator != null ? prevNode.logicalOperator : "AND";
                sql.append(" ").append(op).append(" ");
            }
            
            if (node instanceof QueryCondition) {
                appendQueryCondition(sql, (QueryCondition) node);
            } else if (node instanceof SubQueryCondition) {
                appendSubQueryCondition(sql, (SubQueryCondition) node);
            } else if (node instanceof ExistsCondition) {
                appendExistsCondition(sql, (ExistsCondition) node);
            } else if (node instanceof ConditionGroup) {
                appendConditionGroup(sql, (ConditionGroup) node);
            }
        }
    }
    
    private void appendQueryCondition(StringBuilder sql, QueryCondition c) {
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
    
    private void appendSubQueryCondition(StringBuilder sql, SubQueryCondition c) {
        sql.append(c.field).append(" ").append(c.operator).append(" (");
        sql.append(c.subQuery.toSql());
        sql.append(")");
    }
    
    private void appendExistsCondition(StringBuilder sql, ExistsCondition c) {
        if (c.not) {
            sql.append("NOT ");
        }
        sql.append("EXISTS (");
        sql.append(c.subQuery.toSql());
        sql.append(")");
    }
    
    private void appendConditionGroup(StringBuilder sql, ConditionGroup group) {
        if (group.nodes.isEmpty()) {
            return;
        }
        sql.append("(");
        appendConditionNodes(sql, group.nodes);
        sql.append(")");
    }

    private void appendGroupByClause(StringBuilder sql) {
        if (groupByFields.isEmpty()) {
            return;
        }
        sql.append(" GROUP BY ").append(String.join(", ", groupByFields));
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

    private List<Object> buildParams() {
        List<Object> params = new ArrayList<>();
        collectParams(conditionNodes, params);
        return params;
    }

    private void collectParams(List<ConditionNode> nodes, List<Object> params) {
        for (ConditionNode node : nodes) {
            if (node instanceof QueryCondition) {
                QueryCondition c = (QueryCondition) node;
                if (c.value instanceof List<?> list) {
                    params.addAll(list);
                } else if (c.value instanceof Object[] arr) {
                    params.add(arr[0]);
                    params.add(arr[1]);
                } else if (c.value != null) {
                    params.add(c.value);
                }
            } else if (node instanceof SubQueryCondition) {
                SubQueryCondition c = (SubQueryCondition) node;
                Map<String, Object> subBindings = c.subQuery.getBindings();
                params.addAll(subBindings.values());
            } else if (node instanceof ExistsCondition) {
                ExistsCondition c = (ExistsCondition) node;
                Map<String, Object> subBindings = c.subQuery.getBindings();
                params.addAll(subBindings.values());
            } else if (node instanceof ConditionGroup) {
                collectParams(((ConditionGroup) node).nodes, params);
            }
        }
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

    private Object executeAggregate(String func, String field) {
        String sql = buildAggregateSql(func, field);
        return executeAggregate(sql);
    }

    private static abstract class ConditionNode {
        String logicalOperator;
    }

    private static class QueryCondition extends ConditionNode {
        String field;
        String operator;
        Object value;
        
        QueryCondition(String field, String operator, Object value, String logicalOperator) {
            this.field = field;
            this.operator = operator;
            this.value = value;
            this.logicalOperator = logicalOperator;
        }
    }
    
    private static class SubQueryCondition extends ConditionNode {
        String field;
        String operator;
        Query<?> subQuery;
        
        SubQueryCondition(String field, String operator, Query<?> subQuery, String logicalOperator) {
            this.field = field;
            this.operator = operator;
            this.subQuery = subQuery;
            this.logicalOperator = logicalOperator;
        }
    }
    
    private static class ExistsCondition extends ConditionNode {
        boolean not;
        Query<?> subQuery;
        
        ExistsCondition(boolean not, Query<?> subQuery, String logicalOperator) {
            this.not = not;
            this.subQuery = subQuery;
            this.logicalOperator = logicalOperator;
        }
    }
    
    private static class ConditionGroup extends ConditionNode {
        List<ConditionNode> nodes = new ArrayList<>();
        
        ConditionGroup(String logicalOperator) {
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
    
    private static class JoinClause {
        String type;
        String table;
        String leftField;
        String operator;
        String rightField;
        
        JoinClause(String type, String table, String leftField, String operator, String rightField) {
            this.type = type;
            this.table = table;
            this.leftField = leftField;
            this.operator = operator;
            this.rightField = rightField;
        }
    }
}
