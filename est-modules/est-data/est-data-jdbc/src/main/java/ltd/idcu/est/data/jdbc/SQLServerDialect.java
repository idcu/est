package ltd.idcu.est.data.jdbc;

public class SQLServerDialect implements Dialect {
    
    @Override
    public String getName() {
        return "SQLServer";
    }
    
    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder(sql);
        if (offset > 0) {
            sb.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(limit).append(" ROWS ONLY");
        } else {
            sb.append(" OFFSET 0 ROWS FETCH NEXT ").append(limit).append(" ROWS ONLY");
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
        return "[" + identifier + "]";
    }
    
    @Override
    public String getCurrentTimestampSql() {
        return "GETDATE()";
    }
    
    @Override
    public String getSequenceNextValSql(String sequenceName) {
        return "SELECT NEXT VALUE FOR " + sequenceName;
    }
}
