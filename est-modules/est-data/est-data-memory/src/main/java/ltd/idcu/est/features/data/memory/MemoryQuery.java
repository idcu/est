package ltd.idcu.est.features.data.memory;

import ltd.idcu.est.features.data.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemoryQuery<T> implements Query<T> {
    
    private final List<T> data;
    private final Class<T> entityClass;
    private final List<QueryCondition> conditions;
    private final List<OrderBy> orderBys;
    private final List<String> selectFields;
    private final List<String> groupByFields;
    private final List<QueryCondition> havingConditions;
    private Integer limitValue;
    private Integer offsetValue;
    private boolean distinctFlag;
    private QueryCondition currentCondition;
    private String currentLogicalOperator;
    private String lastSql;
    
    public MemoryQuery(List<T> data, Class<T> entityClass) {
        this.data = data;
        this.entityClass = entityClass;
        this.conditions = new ArrayList<>();
        this.orderBys = new ArrayList<>();
        this.selectFields = new ArrayList<>();
        this.groupByFields = new ArrayList<>();
        this.havingConditions = new ArrayList<>();
        this.limitValue = null;
        this.offsetValue = null;
        this.distinctFlag = false;
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
    public Query<T> whereLikeLeft(String field, String pattern) {
        conditions.add(new QueryCondition(field, "LIKE", "%" + pattern, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereLikeRight(String field, String pattern) {
        conditions.add(new QueryCondition(field, "LIKE", pattern + "%", "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotLike(String field, String pattern) {
        conditions.add(new QueryCondition(field, "NOT LIKE", pattern, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotLikeLeft(String field, String pattern) {
        conditions.add(new QueryCondition(field, "NOT LIKE", "%" + pattern, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotLikeRight(String field, String pattern) {
        conditions.add(new QueryCondition(field, "NOT LIKE", pattern + "%", "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereBetween(String field, Object start, Object end) {
        conditions.add(new QueryCondition(field, "BETWEEN", new Object[]{start, end}, "AND"));
        return this;
    }
    
    @Override
    public Query<T> whereNotBetween(String field, Object start, Object end) {
        conditions.add(new QueryCondition(field, "NOT BETWEEN", new Object[]{start, end}, "AND"));
        return this;
    }
    
    @Override
    public Query<T> eq(String field, Object value) {
        return where(field, "=", value);
    }
    
    @Override
    public Query<T> ne(String field, Object value) {
        return where(field, "!=", value);
    }
    
    @Override
    public Query<T> gt(String field, Object value) {
        return where(field, ">", value);
    }
    
    @Override
    public Query<T> ge(String field, Object value) {
        return where(field, ">=", value);
    }
    
    @Override
    public Query<T> lt(String field, Object value) {
        return where(field, "<", value);
    }
    
    @Override
    public Query<T> le(String field, Object value) {
        return where(field, "<=", value);
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
    public Query<T> and(Consumer<Query<T>> consumer) {
        return this;
    }
    
    @Override
    public Query<T> or(Consumer<Query<T>> consumer) {
        return this;
    }
    
    @Override
    public Query<T> join(String table, String leftField, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> join(String table, String leftField, String operator, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> leftJoin(String table, String leftField, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> leftJoin(String table, String leftField, String operator, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> rightJoin(String table, String leftField, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> rightJoin(String table, String leftField, String operator, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> innerJoin(String table, String leftField, String rightField) {
        return this;
    }
    
    @Override
    public Query<T> innerJoin(String table, String leftField, String operator, String rightField) {
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
    public Query<T> orderByAsc(String field) {
        return orderBy(field, true);
    }
    
    @Override
    public Query<T> orderByDesc(String field) {
        return orderBy(field, false);
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
    public Query<T> page(int pageNum, int pageSize) {
        this.offsetValue = (pageNum - 1) * pageSize;
        this.limitValue = pageSize;
        return this;
    }
    
    @Override
    public Query<T> last(String sql) {
        this.lastSql = sql;
        return this;
    }
    
    @Override
    public Query<T> include(String... relations) {
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
        havingConditions.add(new QueryCondition(field, operator, value, "AND"));
        return this;
    }
    
    @Override
    public List<T> get() {
        List<T> result = new ArrayList<>(data);
        
        result = applyConditions(result);
        result = applyOrderBy(result);
        
        if (offsetValue != null) {
            result = result.stream()
                    .skip(offsetValue)
                    .collect(Collectors.toList());
        }
        
        if (limitValue != null) {
            result = result.stream()
                    .limit(limitValue)
                    .collect(Collectors.toList());
        }
        
        if (distinctFlag) {
            result = result.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        
        return result;
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
    public Optional<T> find(Object id) {
        return data.stream()
                .filter(entity -> hasId(entity, id))
                .findFirst();
    }
    
    @Override
    public long count() {
        List<T> result = applyConditions(new ArrayList<>(data));
        return result.size();
    }
    
    @Override
    public boolean exists() {
        return count() > 0;
    }
    
    @Override
    public Object max(String field) {
        return get().stream()
                .map(entity -> getFieldValue(entity, field))
                .filter(Objects::nonNull)
                .max(Comparator.comparing(o -> ((Comparable) o)))
                .orElse(null);
    }
    
    @Override
    public Object min(String field) {
        return get().stream()
                .map(entity -> getFieldValue(entity, field))
                .filter(Objects::nonNull)
                .min(Comparator.comparing(o -> ((Comparable) o)))
                .orElse(null);
    }
    
    @Override
    public Object sum(String field) {
        return get().stream()
                .map(entity -> getFieldValue(entity, field))
                .filter(Objects::nonNull)
                .mapToDouble(o -> ((Number) o).doubleValue())
                .sum();
    }
    
    @Override
    public Object avg(String field) {
        return get().stream()
                .map(entity -> getFieldValue(entity, field))
                .filter(Objects::nonNull)
                .mapToDouble(o -> ((Number) o).doubleValue())
                .average()
                .orElse(0.0);
    }
    
    @Override
    public Page<T> page(Page<T> page) {
        limit((int) page.getPageSize());
        offset((int) page.getOffset());
        
        long total = count();
        List<T> records = get();
        
        page.setTotal(total);
        page.setRecords(records);
        return page;
    }
    
    @Override
    public int update(Map<String, Object> values) {
        throw new UnsupportedOperationException("Update operation not supported in memory query");
    }
    
    @Override
    public int delete() {
        throw new UnsupportedOperationException("Delete operation not supported in memory query");
    }
    
    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(selectFields.isEmpty() ? "*" : String.join(", ", selectFields));
        sql.append(" FROM ").append(entityClass.getSimpleName());
        
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    sql.append(" ").append(conditions.get(i - 1).logicalOperator).append(" ");
                }
                QueryCondition c = conditions.get(i);
                sql.append(c.field).append(" ").append(c.operator);
                if (c.value != null) {
                    sql.append(" ?");
                }
            }
        }
        
        if (!orderBys.isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(orderBys.stream()
                    .map(o -> o.field + (o.ascending ? " ASC" : " DESC"))
                    .collect(Collectors.joining(", ")));
        }
        
        if (limitValue != null) {
            sql.append(" LIMIT ").append(limitValue);
        }
        
        if (offsetValue != null) {
            sql.append(" OFFSET ").append(offsetValue);
        }
        
        return sql.toString();
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
    
    @Override
    public Query<T> enableSqlLog() {
        return this;
    }
    
    @Override
    public Query<T> disableSqlLog() {
        return this;
    }
    
    @Override
    public Query<T> timeout(int seconds) {
        return this;
    }
    
    @Override
    public Query<T> noTimeout() {
        return this;
    }
    
    @Override
    public Query<T> whereIn(String field, Query<?> subQuery) {
        return this;
    }
    
    @Override
    public Query<T> whereNotIn(String field, Query<?> subQuery) {
        return this;
    }
    
    @Override
    public Query<T> exists(Query<?> subQuery) {
        return this;
    }
    
    @Override
    public Query<T> notExists(Query<?> subQuery) {
        return this;
    }
    
    @SuppressWarnings("unchecked")
    private List<T> applyConditions(List<T> data) {
        if (conditions.isEmpty()) {
            return data;
        }
        
        return data.stream()
                .filter(entity -> {
                    boolean result = true;
                    for (QueryCondition condition : conditions) {
                        boolean match = evaluateCondition(entity, condition);
                        if ("OR".equals(condition.logicalOperator)) {
                            if (match) {
                                result = true;
                            }
                        } else {
                            result = result && match;
                        }
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    private boolean evaluateCondition(T entity, QueryCondition condition) {
        Object fieldValue = getFieldValue(entity, condition.field);
        
        return switch (condition.operator.toUpperCase()) {
            case "=" -> Objects.equals(fieldValue, condition.value);
            case "!=" -> !Objects.equals(fieldValue, condition.value);
            case ">" -> compareValues(fieldValue, condition.value) > 0;
            case "<" -> compareValues(fieldValue, condition.value) < 0;
            case ">=" -> compareValues(fieldValue, condition.value) >= 0;
            case "<=" -> compareValues(fieldValue, condition.value) <= 0;
            case "IN" -> {
                if (condition.value instanceof List<?> list) {
                    yield list.contains(fieldValue);
                }
                yield false;
            }
            case "NOT IN" -> {
                if (condition.value instanceof List<?> list) {
                    yield !list.contains(fieldValue);
                }
                yield true;
            }
            case "IS NULL" -> fieldValue == null;
            case "IS NOT NULL" -> fieldValue != null;
            case "LIKE" -> {
                if (fieldValue == null || condition.value == null) {
                    yield false;
                }
                String pattern = condition.value.toString()
                        .replace("%", ".*")
                        .replace("_", ".");
                yield fieldValue.toString().matches(pattern);
            }
            case "NOT LIKE" -> {
                if (fieldValue == null || condition.value == null) {
                    yield true;
                }
                String pattern = condition.value.toString()
                        .replace("%", ".*")
                        .replace("_", ".");
                yield !fieldValue.toString().matches(pattern);
            }
            case "BETWEEN" -> {
                if (condition.value instanceof Object[] range) {
                    Comparable<Object> val = (Comparable<Object>) fieldValue;
                    yield val != null && val.compareTo(range[0]) >= 0 && val.compareTo(range[1]) <= 0;
                }
                yield false;
            }
            case "NOT BETWEEN" -> {
                if (condition.value instanceof Object[] range) {
                    Comparable<Object> val = (Comparable<Object>) fieldValue;
                    yield val == null || val.compareTo(range[0]) < 0 || val.compareTo(range[1]) > 0;
                }
                yield true;
            }
            default -> false;
        };
    }
    
    @SuppressWarnings("unchecked")
    private int compareValues(Object a, Object b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return ((Comparable<Object>) a).compareTo(b);
    }
    
    private List<T> applyOrderBy(List<T> data) {
        if (orderBys.isEmpty()) {
            return data;
        }
        
        return data.stream()
                .sorted((a, b) -> {
                    int result = 0;
                    for (OrderBy orderBy : orderBys) {
                        Object va = getFieldValue(a, orderBy.field);
                        Object vb = getFieldValue(b, orderBy.field);
                        result = compareValues(va, vb);
                        if (!orderBy.ascending) {
                            result = -result;
                        }
                        if (result != 0) {
                            break;
                        }
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    private Object getFieldValue(T entity, String field) {
        try {
            java.lang.reflect.Field declaredField = entity.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(entity);
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean hasId(T entity, Object id) {
        try {
            java.lang.reflect.Field[] fields = entity.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.isAnnotationPresent(ltd.idcu.est.features.data.api.Id.class)) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(entity);
                    return Objects.equals(fieldValue, id);
                }
            }
        } catch (Exception ignored) {
        }
        return false;
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