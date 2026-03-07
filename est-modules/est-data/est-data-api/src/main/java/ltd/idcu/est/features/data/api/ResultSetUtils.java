package ltd.idcu.est.features.data.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ResultSetUtils {
    
    private ResultSetUtils() {
    }
    
    public static String getString(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }
    
    public static String getString(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }
    
    public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
        int value = rs.getInt(columnLabel);
        return rs.wasNull() ? null : value;
    }
    
    public static Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return rs.wasNull() ? null : value;
    }
    
    public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
        long value = rs.getLong(columnLabel);
        return rs.wasNull() ? null : value;
    }
    
    public static Long getLong(ResultSet rs, int columnIndex) throws SQLException {
        long value = rs.getLong(columnIndex);
        return rs.wasNull() ? null : value;
    }
    
    public static Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
        double value = rs.getDouble(columnLabel);
        return rs.wasNull() ? null : value;
    }
    
    public static Double getDouble(ResultSet rs, int columnIndex) throws SQLException {
        double value = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : value;
    }
    
    public static Float getFloat(ResultSet rs, String columnLabel) throws SQLException {
        float value = rs.getFloat(columnLabel);
        return rs.wasNull() ? null : value;
    }
    
    public static Boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
        boolean value = rs.getBoolean(columnLabel);
        return rs.wasNull() ? null : value;
    }
    
    public static Boolean getBoolean(ResultSet rs, int columnIndex) throws SQLException {
        boolean value = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : value;
    }
    
    public static java.sql.Date getDate(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getDate(columnLabel);
    }
    
    public static java.sql.Timestamp getTimestamp(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }
    
    public static java.sql.Time getTime(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getTime(columnLabel);
    }
    
    public static byte[] getBytes(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }
    
    public static <T> List<T> toList(ResultSet rs, EntityMapper<T> mapper) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapper.map(rs));
        }
        return list;
    }
    
    public static <T> T first(ResultSet rs, EntityMapper<T> mapper) throws SQLException {
        if (rs.next()) {
            return mapper.map(rs);
        }
        return null;
    }
}
