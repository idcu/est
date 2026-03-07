package ltd.idcu.est.features.data.jdbc;

import java.util.ArrayList;
import java.util.List;

public class SqlBuilder {
    private final StringBuilder sql;
    private final List<Object> parameters;
    
    public SqlBuilder() {
        this.sql = new StringBuilder();
        this.parameters = new ArrayList<>();
    }
    
    public SqlBuilder append(String part) {
        sql.append(part);
        return this;
    }
    
    public SqlBuilder append(String part, Object param) {
        sql.append(part);
        parameters.add(param);
        return this;
    }
    
    public SqlBuilder appendSpace() {
        sql.append(" ");
        return this;
    }
    
    public SqlBuilder select(String... columns) {
        sql.append("SELECT ");
        if (columns.length == 0) {
            sql.append("*");
        } else {
            sql.append(String.join(", ", columns));
        }
        return this;
    }
    
    public SqlBuilder from(String table) {
        sql.append(" FROM ").append(table);
        return this;
    }
    
    public SqlBuilder where(String condition) {
        sql.append(" WHERE ").append(condition);
        return this;
    }
    
    public SqlBuilder where(String condition, Object param) {
        sql.append(" WHERE ").append(condition);
        parameters.add(param);
        return this;
    }
    
    public SqlBuilder and(String condition) {
        sql.append(" AND ").append(condition);
        return this;
    }
    
    public SqlBuilder and(String condition, Object param) {
        sql.append(" AND ").append(condition);
        parameters.add(param);
        return this;
    }
    
    public SqlBuilder or(String condition) {
        sql.append(" OR ").append(condition);
        return this;
    }
    
    public SqlBuilder or(String condition, Object param) {
        sql.append(" OR ").append(condition);
        parameters.add(param);
        return this;
    }
    
    public SqlBuilder orderBy(String column) {
        sql.append(" ORDER BY ").append(column);
        return this;
    }
    
    public SqlBuilder orderByAsc(String column) {
        sql.append(" ORDER BY ").append(column).append(" ASC");
        return this;
    }
    
    public SqlBuilder orderByDesc(String column) {
        sql.append(" ORDER BY ").append(column).append(" DESC");
        return this;
    }
    
    public SqlBuilder limit(int limit) {
        sql.append(" LIMIT ").append(limit);
        return this;
    }
    
    public SqlBuilder offset(int offset) {
        sql.append(" OFFSET ").append(offset);
        return this;
    }
    
    public SqlBuilder insertInto(String table, String... columns) {
        sql.append("INSERT INTO ").append(table).append(" (");
        sql.append(String.join(", ", columns));
        sql.append(") VALUES (");
        sql.append(String.join(", ", java.util.Collections.nCopies(columns.length, "?")));
        sql.append(")");
        return this;
    }
    
    public SqlBuilder update(String table) {
        sql.append("UPDATE ").append(table);
        return this;
    }
    
    public SqlBuilder set(String column, Object value) {
        if (parameters.isEmpty()) {
            sql.append(" SET ");
        } else {
            sql.append(", ");
        }
        sql.append(column).append(" = ?");
        parameters.add(value);
        return this;
    }
    
    public SqlBuilder deleteFrom(String table) {
        sql.append("DELETE FROM ").append(table);
        return this;
    }
    
    public String build() {
        return sql.toString();
    }
    
    public List<Object> getParameters() {
        return new ArrayList<>(parameters);
    }
    
    public void addParameter(Object param) {
        parameters.add(param);
    }
    
    public void clear() {
        sql.setLength(0);
        parameters.clear();
    }
    
    @Override
    public String toString() {
        return "SqlBuilder{sql='" + sql + "', params=" + parameters + "}";
    }
}
