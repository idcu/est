package ltd.idcu.est.codegen.db.reader;

import ltd.idcu.est.codegen.db.metadata.Column;
import ltd.idcu.est.codegen.db.metadata.ForeignKey;
import ltd.idcu.est.codegen.db.metadata.Index;
import ltd.idcu.est.codegen.db.metadata.PrimaryKey;
import ltd.idcu.est.codegen.db.metadata.Table;
import ltd.idcu.est.codegen.db.type.TypeMapper;
import ltd.idcu.est.codegen.db.util.NamingUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLMetadataReader implements DatabaseMetadataReader {

    @Override
    public List<String> getTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        }
        return tableNames;
    }

    @Override
    public Table readTable(Connection connection, String tableName) throws SQLException {
        Table table = new Table();
        table.setName(tableName);
        table.setClassName(NamingUtils.toPascalCase(tableName));

        DatabaseMetaData metaData = connection.getMetaData();

        readColumns(metaData, table);
        readPrimaryKey(metaData, table);
        readForeignKeys(metaData, table);
        readIndexes(metaData, table);
        readTableRemarks(metaData, table);

        return table;
    }

    @Override
    public List<Table> readAllTables(Connection connection) throws SQLException {
        List<String> tableNames = getTableNames(connection);
        return readTables(connection, tableNames);
    }

    @Override
    public List<Table> readTables(Connection connection, List<String> tableNames) throws SQLException {
        List<Table> tables = new ArrayList<>();
        for (String tableName : tableNames) {
            tables.add(readTable(connection, tableName));
        }
        return tables;
    }

    private void readColumns(DatabaseMetaData metaData, Table table) throws SQLException {
        try (ResultSet rs = metaData.getColumns(null, null, table.getName(), "%")) {
            while (rs.next()) {
                Column column = new Column();
                column.setName(rs.getString("COLUMN_NAME"));
                column.setFieldName(NamingUtils.toCamelCase(column.getName()));
                column.setSqlType(rs.getString("TYPE_NAME"));
                column.setSqlTypeCode(rs.getInt("DATA_TYPE"));
                column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
                column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
                column.setRemarks(rs.getString("REMARKS"));
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                column.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));

                String javaType = TypeMapper.sqlTypeToJava(column.getSqlType());
                if ("Object".equals(javaType)) {
                    javaType = TypeMapper.jdbcTypeToJava(column.getSqlTypeCode());
                }
                column.setJavaType(javaType);

                table.addColumn(column);
            }
        }
    }

    private void readPrimaryKey(DatabaseMetaData metaData, Table table) throws SQLException {
        PrimaryKey primaryKey = new PrimaryKey();
        try (ResultSet rs = metaData.getPrimaryKeys(null, null, table.getName())) {
            while (rs.next()) {
                if (primaryKey.getName() == null) {
                    primaryKey.setName(rs.getString("PK_NAME"));
                }
                String columnName = rs.getString("COLUMN_NAME");
                Column column = table.findColumnByName(columnName);
                if (column != null) {
                    column.setPrimaryKey(true);
                    primaryKey.addColumn(column);
                }
            }
        }
        if (!primaryKey.getColumns().isEmpty()) {
            table.setPrimaryKey(primaryKey);
        }
    }

    private void readForeignKeys(DatabaseMetaData metaData, Table table) throws SQLException {
        try (ResultSet rs = metaData.getImportedKeys(null, null, table.getName())) {
            while (rs.next()) {
                ForeignKey foreignKey = new ForeignKey();
                foreignKey.setName(rs.getString("FK_NAME"));
                foreignKey.setForeignTableName(rs.getString("PKTABLE_NAME"));
                foreignKey.setForeignColumnName(rs.getString("PKCOLUMN_NAME"));

                String columnName = rs.getString("FKCOLUMN_NAME");
                Column column = table.findColumnByName(columnName);
                if (column != null) {
                    foreignKey.setLocalColumn(column);
                    table.addForeignKey(foreignKey);
                }
            }
        }
    }

    private void readIndexes(DatabaseMetaData metaData, Table table) throws SQLException {
        Map<String, Index> indexMap = new HashMap<>();
        try (ResultSet rs = metaData.getIndexInfo(null, null, table.getName(), false, true)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                if (indexName == null || "PRIMARY".equals(indexName)) {
                    continue;
                }
                Index index = indexMap.get(indexName);
                if (index == null) {
                    index = new Index();
                    index.setName(indexName);
                    index.setUnique(!rs.getBoolean("NON_UNIQUE"));
                    indexMap.put(indexName, index);
                }
                String columnName = rs.getString("COLUMN_NAME");
                Column column = table.findColumnByName(columnName);
                if (column != null) {
                    column.setUnique(index.isUnique());
                    index.addColumn(column);
                }
            }
        }
        table.setIndexes(new ArrayList<>(indexMap.values()));
    }

    private void readTableRemarks(DatabaseMetaData metaData, Table table) throws SQLException {
        try (ResultSet rs = metaData.getTables(null, null, table.getName(), new String[]{"TABLE"})) {
            if (rs.next()) {
                table.setRemarks(rs.getString("REMARKS"));
            }
        }
    }
}
