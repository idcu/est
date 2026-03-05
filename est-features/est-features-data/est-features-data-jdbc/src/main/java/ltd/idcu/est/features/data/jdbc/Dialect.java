package ltd.idcu.est.features.data.jdbc;

public interface Dialect {
    
    String getName();
    
    String getLimitSql(String sql, int offset, int limit);
    
    String getPageSql(String sql, int pageNum, int pageSize);
    
    String wrapIdentifier(String identifier);
    
    String getCurrentTimestampSql();
    
    String getSequenceNextValSql(String sequenceName);
}
