package ltd.idcu.est.codegen.db.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

    private static final Map<String, String> SQL_TYPE_TO_JAVA = new HashMap<>();
    private static final Map<Integer, String> JDBC_TYPE_TO_JAVA = new HashMap<>();

    static {
        SQL_TYPE_TO_JAVA.put("TINYINT", "Integer");
        SQL_TYPE_TO_JAVA.put("SMALLINT", "Integer");
        SQL_TYPE_TO_JAVA.put("MEDIUMINT", "Integer");
        SQL_TYPE_TO_JAVA.put("INT", "Integer");
        SQL_TYPE_TO_JAVA.put("INTEGER", "Integer");
        SQL_TYPE_TO_JAVA.put("BIGINT", "Long");
        SQL_TYPE_TO_JAVA.put("FLOAT", "Float");
        SQL_TYPE_TO_JAVA.put("DOUBLE", "Double");
        SQL_TYPE_TO_JAVA.put("DECIMAL", "java.math.BigDecimal");
        SQL_TYPE_TO_JAVA.put("NUMERIC", "java.math.BigDecimal");
        SQL_TYPE_TO_JAVA.put("REAL", "Double");
        SQL_TYPE_TO_JAVA.put("BIT", "Boolean");
        SQL_TYPE_TO_JAVA.put("BOOLEAN", "Boolean");
        SQL_TYPE_TO_JAVA.put("CHAR", "String");
        SQL_TYPE_TO_JAVA.put("VARCHAR", "String");
        SQL_TYPE_TO_JAVA.put("TINYTEXT", "String");
        SQL_TYPE_TO_JAVA.put("TEXT", "String");
        SQL_TYPE_TO_JAVA.put("MEDIUMTEXT", "String");
        SQL_TYPE_TO_JAVA.put("LONGTEXT", "String");
        SQL_TYPE_TO_JAVA.put("JSON", "String");
        SQL_TYPE_TO_JAVA.put("ENUM", "String");
        SQL_TYPE_TO_JAVA.put("SET", "String");
        SQL_TYPE_TO_JAVA.put("BINARY", "byte[]");
        SQL_TYPE_TO_JAVA.put("VARBINARY", "byte[]");
        SQL_TYPE_TO_JAVA.put("TINYBLOB", "byte[]");
        SQL_TYPE_TO_JAVA.put("BLOB", "byte[]");
        SQL_TYPE_TO_JAVA.put("MEDIUMBLOB", "byte[]");
        SQL_TYPE_TO_JAVA.put("LONGBLOB", "byte[]");
        SQL_TYPE_TO_JAVA.put("DATE", "java.time.LocalDate");
        SQL_TYPE_TO_JAVA.put("TIME", "java.time.LocalTime");
        SQL_TYPE_TO_JAVA.put("DATETIME", "java.time.LocalDateTime");
        SQL_TYPE_TO_JAVA.put("TIMESTAMP", "java.time.LocalDateTime");
        SQL_TYPE_TO_JAVA.put("YEAR", "Integer");

        JDBC_TYPE_TO_JAVA.put(Types.TINYINT, "Integer");
        JDBC_TYPE_TO_JAVA.put(Types.SMALLINT, "Integer");
        JDBC_TYPE_TO_JAVA.put(Types.INTEGER, "Integer");
        JDBC_TYPE_TO_JAVA.put(Types.BIGINT, "Long");
        JDBC_TYPE_TO_JAVA.put(Types.FLOAT, "Float");
        JDBC_TYPE_TO_JAVA.put(Types.DOUBLE, "Double");
        JDBC_TYPE_TO_JAVA.put(Types.DECIMAL, "java.math.BigDecimal");
        JDBC_TYPE_TO_JAVA.put(Types.NUMERIC, "java.math.BigDecimal");
        JDBC_TYPE_TO_JAVA.put(Types.REAL, "Double");
        JDBC_TYPE_TO_JAVA.put(Types.BIT, "Boolean");
        JDBC_TYPE_TO_JAVA.put(Types.BOOLEAN, "Boolean");
        JDBC_TYPE_TO_JAVA.put(Types.CHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.VARCHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.LONGVARCHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.BINARY, "byte[]");
        JDBC_TYPE_TO_JAVA.put(Types.VARBINARY, "byte[]");
        JDBC_TYPE_TO_JAVA.put(Types.LONGVARBINARY, "byte[]");
        JDBC_TYPE_TO_JAVA.put(Types.DATE, "java.time.LocalDate");
        JDBC_TYPE_TO_JAVA.put(Types.TIME, "java.time.LocalTime");
        JDBC_TYPE_TO_JAVA.put(Types.TIMESTAMP, "java.time.LocalDateTime");
        JDBC_TYPE_TO_JAVA.put(Types.CLOB, "String");
        JDBC_TYPE_TO_JAVA.put(Types.BLOB, "byte[]");
        JDBC_TYPE_TO_JAVA.put(Types.ARRAY, "java.util.List");
        JDBC_TYPE_TO_JAVA.put(Types.STRUCT, "Object");
        JDBC_TYPE_TO_JAVA.put(Types.REF, "Object");
        JDBC_TYPE_TO_JAVA.put(Types.DATALINK, "java.net.URL");
        JDBC_TYPE_TO_JAVA.put(Types.ROWID, "String");
        JDBC_TYPE_TO_JAVA.put(Types.NCHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.NVARCHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.LONGNVARCHAR, "String");
        JDBC_TYPE_TO_JAVA.put(Types.NCLOB, "String");
        JDBC_TYPE_TO_JAVA.put(Types.SQLXML, "String");
    }

    public static String sqlTypeToJava(String sqlType) {
        if (sqlType == null) {
            return "Object";
        }
        String upperType = sqlType.toUpperCase();
        for (Map.Entry<String, String> entry : SQL_TYPE_TO_JAVA.entrySet()) {
            if (upperType.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Object";
    }

    public static String jdbcTypeToJava(int jdbcType) {
        return JDBC_TYPE_TO_JAVA.getOrDefault(jdbcType, "Object");
    }

    public static String getFullyQualifiedType(String javaType) {
        if (javaType == null) {
            return null;
        }
        switch (javaType) {
            case "String":
            case "Integer":
            case "Long":
            case "Double":
            case "Float":
            case "Boolean":
            case "Byte":
            case "Short":
            case "Character":
                return javaType;
            default:
                return javaType;
        }
    }

    public static boolean needsImport(String javaType) {
        if (javaType == null) {
            return false;
        }
        switch (javaType) {
            case "String":
            case "Integer":
            case "Long":
            case "Double":
            case "Float":
            case "Boolean":
            case "Byte":
            case "Short":
            case "Character":
                return false;
            case "int":
            case "long":
            case "double":
            case "float":
            case "boolean":
            case "byte":
            case "short":
            case "char":
                return false;
            default:
                return true;
        }
    }
}
