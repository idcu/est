package ltd.idcu.est.features.data.jdbc;

public class SQLiteDialect implements Dialect {
    
    @Override
    public String getName() {
        return "SQLite";
    }
    
    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder(sql);
        if (offset > 0) {
            sb.append(" LIMIT ").append(limit).append(" OFFSET ").append(offset);
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
        return "strftime('%Y-%m-%d %H:%M:%f', 'now')";
    }
    
    @Override
    public String getSequenceNextValSql(String sequenceName) {
        throw new UnsupportedOperationException("SQLite does not support sequences. Use AUTOINCREMENT instead.");
    }
}
