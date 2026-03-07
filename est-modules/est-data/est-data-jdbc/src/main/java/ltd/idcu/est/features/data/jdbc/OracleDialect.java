package ltd.idcu.est.features.data.jdbc;

public class OracleDialect implements Dialect {
    
    @Override
    public String getName() {
        return "Oracle";
    }
    
    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder();
        if (offset > 0) {
            sb.append("SELECT * FROM (SELECT rownum rnum, t.* FROM (").append(sql).append(") t WHERE rownum <= ").append(offset + limit).append(") WHERE rnum > ").append(offset);
        } else {
            sb.append("SELECT * FROM (").append(sql).append(") WHERE rownum <= ").append(limit);
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
        return "\"" + identifier.toUpperCase() + "\"";
    }
    
    @Override
    public String getCurrentTimestampSql() {
        return "SYSTIMESTAMP";
    }
    
    @Override
    public String getSequenceNextValSql(String sequenceName) {
        return "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
    }
}
