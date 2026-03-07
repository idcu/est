package ltd.idcu.est.features.data.jdbc;

public class PostgreSQLDialect implements Dialect {
    
    @Override
    public String getName() {
        return "PostgreSQL";
    }
    
    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder(sql);
        if (offset > 0) {
            sb.append(" OFFSET ").append(offset).append(" LIMIT ").append(limit);
        } else {
            sb.append(" LIMIT ").append(limit);
        }
        return sb.toString();
    }
    
    @Override
    public String getPageSql(String sql, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return getLimitSql(sql, offset, pageSize);
    }
    
    @Override
    public String wrapIdentifier(String identifier) {
        return "\"" + identifier + "\"";
    }
    
    @Override
    public String getCurrentTimestampSql() {
        return "CURRENT_TIMESTAMP";
    }
    
    @Override
    public String getSequenceNextValSql(String sequenceName) {
        return "SELECT nextval('" + sequenceName + "')";
    }
}
