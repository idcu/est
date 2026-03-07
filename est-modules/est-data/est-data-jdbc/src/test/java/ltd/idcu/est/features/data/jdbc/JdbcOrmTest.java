package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.*;

public class JdbcOrmTest {
    
    @Test
    public void testDialectFactory() {
        Dialect mysql = DialectFactory.getDialectByName("MySQL");
        Assertions.assertTrue(mysql instanceof MySQLDialect);
        
        Dialect postgres = DialectFactory.getDialectByName("PostgreSQL");
        Assertions.assertTrue(postgres instanceof PostgreSQLDialect);
        
        Dialect oracle = DialectFactory.getDialectByName("Oracle");
        Assertions.assertTrue(oracle instanceof OracleDialect);
        
        Dialect sqlServer = DialectFactory.getDialectByName("Microsoft SQL Server");
        Assertions.assertTrue(sqlServer instanceof SQLServerDialect);
        
        Dialect unknown = DialectFactory.getDialectByName("UnknownDB");
        Assertions.assertTrue(unknown instanceof MySQLDialect);
    }
    
    @Test
    public void testMySQLDialect() {
        Dialect dialect = new MySQLDialect();
        Assertions.assertEquals("MySQL", dialect.getName());
        
        String sql = "SELECT * FROM users";
        String limitSql = dialect.getLimitSql(sql, 0, 10);
        Assertions.assertEquals("SELECT * FROM users LIMIT 10", limitSql);
        
        String offsetSql = dialect.getLimitSql(sql, 20, 10);
        Assertions.assertEquals("SELECT * FROM users LIMIT 20, 10", offsetSql);
        
        String pageSql = dialect.getPageSql(sql, 3, 10);
        Assertions.assertEquals("SELECT * FROM users LIMIT 20, 10", pageSql);
        
        Assertions.assertEquals("`user`", dialect.wrapIdentifier("user"));
        Assertions.assertEquals("NOW()", dialect.getCurrentTimestampSql());
    }
    
    @Test
    public void testPostgreSQLDialect() {
        Dialect dialect = new PostgreSQLDialect();
        Assertions.assertEquals("PostgreSQL", dialect.getName());
        
        String sql = "SELECT * FROM users";
        String limitSql = dialect.getLimitSql(sql, 0, 10);
        Assertions.assertEquals("SELECT * FROM users LIMIT 10", limitSql);
        
        String offsetSql = dialect.getLimitSql(sql, 20, 10);
        Assertions.assertEquals("SELECT * FROM users OFFSET 20 LIMIT 10", offsetSql);
        
        String pageSql = dialect.getPageSql(sql, 3, 10);
        Assertions.assertEquals("SELECT * FROM users OFFSET 20 LIMIT 10", pageSql);
        
        Assertions.assertEquals("\"user\"", dialect.wrapIdentifier("user"));
        Assertions.assertEquals("CURRENT_TIMESTAMP", dialect.getCurrentTimestampSql());
        Assertions.assertEquals("SELECT nextval('user_seq')", dialect.getSequenceNextValSql("user_seq"));
    }
    
    @Test
    public void testOracleDialect() {
        Dialect dialect = new OracleDialect();
        Assertions.assertEquals("Oracle", dialect.getName());
        
        String sql = "SELECT * FROM users";
        String limitSql = dialect.getLimitSql(sql, 0, 10);
        Assertions.assertTrue(limitSql.contains("rownum <= 10"));
        
        String offsetSql = dialect.getLimitSql(sql, 20, 10);
        Assertions.assertTrue(offsetSql.contains("rnum > 20"));
        Assertions.assertTrue(offsetSql.contains("rownum <= 30"));
        
        String pageSql = dialect.getPageSql(sql, 3, 10);
        Assertions.assertTrue(pageSql.contains("rnum > 20"));
        
        Assertions.assertEquals("\"USER\"", dialect.wrapIdentifier("user"));
        Assertions.assertEquals("SYSTIMESTAMP", dialect.getCurrentTimestampSql());
        Assertions.assertEquals("SELECT user_seq.NEXTVAL FROM DUAL", dialect.getSequenceNextValSql("user_seq"));
    }
    
    @Test
    public void testSQLServerDialect() {
        Dialect dialect = new SQLServerDialect();
        Assertions.assertEquals("SQLServer", dialect.getName());
        
        String sql = "SELECT * FROM users";
        String limitSql = dialect.getLimitSql(sql, 0, 10);
        Assertions.assertEquals("SELECT * FROM users OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY", limitSql);
        
        String offsetSql = dialect.getLimitSql(sql, 20, 10);
        Assertions.assertEquals("SELECT * FROM users OFFSET 20 ROWS FETCH NEXT 10 ROWS ONLY", offsetSql);
        
        String pageSql = dialect.getPageSql(sql, 3, 10);
        Assertions.assertEquals("SELECT * FROM users OFFSET 20 ROWS FETCH NEXT 10 ROWS ONLY", pageSql);
        
        Assertions.assertEquals("[user]", dialect.wrapIdentifier("user"));
        Assertions.assertEquals("GETDATE()", dialect.getCurrentTimestampSql());
        Assertions.assertEquals("SELECT NEXT VALUE FOR user_seq", dialect.getSequenceNextValSql("user_seq"));
    }
}