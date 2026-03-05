package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BatchHelper {

    private BatchHelper() {
    }

    public static final int DEFAULT_BATCH_SIZE = 1000;

    public static <T> void executeBatch(Connection conn, String sql, List<T> entities,
                                       BiConsumer<PreparedStatement, T> parameterSetter) {
        executeBatch(conn, sql, entities, parameterSetter, DEFAULT_BATCH_SIZE);
    }

    public static <T> void executeBatch(Connection conn, String sql, List<T> entities,
                                       BiConsumer<PreparedStatement, T> parameterSetter, int batchSize) {
        if (conn == null || sql == null || entities == null || entities.isEmpty()) {
            return;
        }

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            int count = 0;
            for (T entity : entities) {
                parameterSetter.accept(stmt, entity);
                stmt.addBatch();
                count++;
                if (count % batchSize == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count % batchSize != 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new DataException("Failed to execute batch", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt, null);
        }
    }

    public static <T> int[] executeBatchWithReturn(Connection conn, String sql, List<T> entities,
                                                     BiConsumer<PreparedStatement, T> parameterSetter) {
        return executeBatchWithReturn(conn, sql, entities, parameterSetter, DEFAULT_BATCH_SIZE);
    }

    public static <T> int[] executeBatchWithReturn(Connection conn, String sql, List<T> entities,
                                                     BiConsumer<PreparedStatement, T> parameterSetter, int batchSize) {
        if (conn == null || sql == null || entities == null || entities.isEmpty()) {
            return new int[0];
        }

        List<Integer> allResults = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            int count = 0;
            for (T entity : entities) {
                parameterSetter.accept(stmt, entity);
                stmt.addBatch();
                count++;
                if (count % batchSize == 0) {
                    int[] results = stmt.executeBatch();
                    for (int result : results) {
                        allResults.add(result);
                    }
                    stmt.clearBatch();
                }
            }
            if (count % batchSize != 0) {
                int[] results = stmt.executeBatch();
                for (int result : results) {
                    allResults.add(result);
                }
            }
            int[] finalResults = new int[allResults.size()];
            for (int i = 0; i < allResults.size(); i++) {
                finalResults[i] = allResults.get(i);
            }
            return finalResults;
        } catch (SQLException e) {
            throw new DataException("Failed to execute batch", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt, null);
        }
    }

    public static <T> void executeInTransaction(Connection conn, Consumer<Connection> action) {
        if (conn == null || action == null) {
            return;
        }

        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            action.accept(conn);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DataException("Failed to rollback transaction", ex);
            }
            throw new DataException("Failed to execute in transaction", e);
        } finally {
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException ignored) {
            }
        }
    }

    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<List<T>> partitions = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i += batchSize) {
            int end = Math.min(i + batchSize, size);
            partitions.add(new ArrayList<>(list.subList(i, end)));
        }
        return partitions;
    }

    public static <T> void forEachBatch(List<T> list, int batchSize, Consumer<List<T>> batchAction) {
        if (list == null || list.isEmpty() || batchAction == null) {
            return;
        }

        List<List<T>> partitions = partition(list, batchSize);
        for (List<T> partition : partitions) {
            batchAction.accept(partition);
        }
    }
}
