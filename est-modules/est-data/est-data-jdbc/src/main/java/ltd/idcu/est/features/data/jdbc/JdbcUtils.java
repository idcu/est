package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.DataException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcUtils {

    private JdbcUtils() {
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        closeQuietly(rs);
        closeQuietly(stmt);
        closeQuietly(conn);
    }

    public static List<Map<String, Object>> queryForList(Connection conn, String sql, Object... params) {
        List<Map<String, Object>> result = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new java.util.LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to execute query: " + sql, e);
        } finally {
            closeQuietly(null, stmt, rs);
        }
    }

    public static Map<String, Object> queryForMap(Connection conn, String sql, Object... params) {
        List<Map<String, Object>> list = queryForList(conn, sql, params);
        return list.isEmpty() ? null : list.get(0);
    }

    public static <T> T queryForObject(Connection conn, String sql, Class<T> requiredType, Object... params) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Object value = rs.getObject(1);
                return requiredType.cast(value);
            }
            return null;
        } catch (SQLException e) {
            throw new DataException("Failed to execute query: " + sql, e);
        } finally {
            closeQuietly(null, stmt, rs);
        }
    }

    public static int executeUpdate(Connection conn, String sql, Object... params) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute update: " + sql, e);
        } finally {
            closeQuietly(null, stmt, null);
        }
    }

    public static long executeInsert(Connection conn, String sql, Object... params) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(stmt, params);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new DataException("Failed to execute insert: " + sql, e);
        } finally {
            closeQuietly(null, stmt, rs);
        }
    }

    private static void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    public static void beginTransaction(Connection conn) {
        try {
            if (conn != null && conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new DataException("Failed to begin transaction", e);
        }
    }

    public static void commitTransaction(Connection conn) {
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataException("Failed to commit transaction", e);
        }
    }

    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataException("Failed to rollback transaction", e);
        }
    }

    public static String escapeSql(String sql) {
        if (sql == null) {
            return null;
        }
        return sql.replace("'", "''");
    }

    public static String quoteIdentifier(String identifier) {
        if (identifier == null) {
            return null;
        }
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    public static String inClause(int size) {
        if (size <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }
}
