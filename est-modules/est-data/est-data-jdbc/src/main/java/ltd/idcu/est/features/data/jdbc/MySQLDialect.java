package ltd.idcu.est.features.data.jdbc;

public class MySQLDialect implements Dialect {
    
    @Override
    public String getName() {
        return "MySQL";
    }
    
    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder(sql);
        if (offset > 0) {
            sb.append(" LIMIT ").append(offset).append(", ").append(limit);
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
        return "`" + identifier + "`";
    }
    
    @Override
    public String getCurrentTimestampSql() {
        return "NOW()";
    }
    
    @Override
    public String getSequenceNextValSql(String sequenceName) {
        throw new UnsupportedOperationException("MySQL does not support sequences");
    }
}
