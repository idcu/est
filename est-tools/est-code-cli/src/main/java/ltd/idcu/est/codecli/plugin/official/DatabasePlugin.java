package ltd.idcu.est.codecli.plugin.official;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabasePlugin extends BaseEstPlugin {

    private static final String PLUGIN_ID = "database-plugin";
    private static final String PLUGIN_NAME = "Database Management Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "数据库管理插件，提供数据库连接、查询执行、表结构查看等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";

    private Connection connection;
    private String currentUrl;
    private String currentUser;

    public DatabasePlugin() {
        addCapability("features", Map.of(
            "connect", true,
            "query", true,
            "execute", true,
            "tables", true,
            "export", true
        ));
        addCapability("commands", new String[]{"db_connect", "db_query", "db_execute", "db_tables", "db_export"});
    }

    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }

    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }

    @Override
    protected void onInitialize() throws PluginException {
        logInfo("数据库管理插件初始化完成");
    }

    @Override
    protected void onShutdown() throws PluginException {
        closeConnection();
        logInfo("数据库管理插件已关闭");
    }

    public void connect(String url, String user, String password) throws PluginException {
        try {
            closeConnection();
            this.currentUrl = url;
            this.currentUser = user;
            this.connection = DriverManager.getConnection(url, user, password);
            logInfo("成功连接到数据库: " + url);
        } catch (SQLException e) {
            throw new PluginException("连接数据库失败: " + e.getMessage(), e);
        }
    }

    public void closeConnection() throws PluginException {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logInfo("数据库连接已关闭");
            } catch (SQLException e) {
                throw new PluginException("关闭数据库连接失败: " + e.getMessage(), e);
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public List<Map<String, Object>> executeQuery(String sql) throws PluginException {
        checkConnection();
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
            
            logInfo("查询执行成功，返回 " + results.size() + " 行");
            return results;
        } catch (SQLException e) {
            throw new PluginException("执行查询失败: " + e.getMessage(), e);
        }
    }

    public int executeUpdate(String sql) throws PluginException {
        checkConnection();
        
        try (Statement stmt = connection.createStatement()) {
            int affectedRows = stmt.executeUpdate(sql);
            logInfo("更新执行成功，影响 " + affectedRows + " 行");
            return affectedRows;
        } catch (SQLException e) {
            throw new PluginException("执行更新失败: " + e.getMessage(), e);
        }
    }

    public List<String> getTables() throws PluginException {
        checkConnection();
        List<String> tables = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
            
            logInfo("获取到 " + tables.size() + " 个表");
            return tables;
        } catch (SQLException e) {
            throw new PluginException("获取表列表失败: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getTableInfo(String tableName) throws PluginException {
        checkConnection();
        List<Map<String, Object>> columns = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                while (rs.next()) {
                    Map<String, Object> column = new HashMap<>();
                    column.put("name", rs.getString("COLUMN_NAME"));
                    column.put("type", rs.getString("TYPE_NAME"));
                    column.put("size", rs.getInt("COLUMN_SIZE"));
                    column.put("nullable", rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    column.put("default", rs.getString("COLUMN_DEF"));
                    columns.add(column);
                }
            }
            
            logInfo("获取表 " + tableName + " 的结构信息，共 " + columns.size() + " 列");
            return columns;
        } catch (SQLException e) {
            throw new PluginException("获取表结构失败: " + e.getMessage(), e);
        }
    }

    public String exportTableData(String tableName, String format) throws PluginException {
        List<Map<String, Object>> data = executeQuery("SELECT * FROM " + tableName);
        
        if ("csv".equalsIgnoreCase(format)) {
            return exportToCsv(data);
        } else if ("json".equalsIgnoreCase(format)) {
            return exportToJson(data);
        } else {
            throw new PluginException("不支持的导出格式: " + format);
        }
    }

    private String exportToCsv(List<Map<String, Object>> data) {
        if (data.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        List<String> headers = new ArrayList<>(data.get(0).keySet());
        
        sb.append(String.join(",", headers)).append("\n");
        
        for (Map<String, Object> row : data) {
            List<String> values = new ArrayList<>();
            for (String header : headers) {
                Object value = row.get(header);
                values.add(value != null ? value.toString() : "");
            }
            sb.append(String.join(",", values)).append("\n");
        }
        
        return sb.toString();
    }

    private String exportToJson(List<Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> row = data.get(i);
            sb.append("  {");
            
            List<String> keys = new ArrayList<>(row.keySet());
            for (int j = 0; j < keys.size(); j++) {
                String key = keys.get(j);
                Object value = row.get(key);
                sb.append("\"").append(key).append("\": ");
                if (value instanceof String) {
                    sb.append("\"").append(value).append("\"");
                } else {
                    sb.append(value != null ? value.toString() : "null");
                }
                if (j < keys.size() - 1) {
                    sb.append(", ");
                }
            }
            
            sb.append("}");
            if (i < data.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        
        sb.append("]");
        return sb.toString();
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    private void checkConnection() throws PluginException {
        if (!isConnected()) {
            throw new PluginException("未连接到数据库，请先连接");
        }
    }
}
