package ltd.idcu.est.dbgenerator.reader;

import ltd.idcu.est.dbgenerator.metadata.Table;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMetadataReader {

    List<String> getTableNames(Connection connection) throws SQLException;

    Table readTable(Connection connection, String tableName) throws SQLException;

    List<Table> readAllTables(Connection connection) throws SQLException;

    List<Table> readTables(Connection connection, List<String> tableNames) throws SQLException;
}
