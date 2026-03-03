package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcRepository<T, ID> implements Repository<T, ID> {
    
    private final ConnectionPool connectionPool;
    private final EntityMapper<T> mapper;
    private final String tableName;
    private final String idColumnName;
    private final Class<T> entityClass;
    
    public JdbcRepository(ConnectionPool connectionPool, Class<T> entityClass, 
                         EntityMapper<T> mapper, String tableName, String idColumnName) {
        this.connectionPool = connectionPool;
        this.entityClass = entityClass;
        this.mapper = mapper;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
    }
    
    @Override
    public T save(T entity) {
        throw new UnsupportedOperationException("Use JdbcOrm for save operations");
    }
    
    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        throw new UnsupportedOperationException("Use JdbcOrm for save operations");
    }
    
    @Override
    public Optional<T> findById(ID id) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataException("Failed to find entity by id: " + id, e);
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DataException("Failed to check existence for id: " + id, e);
        }
    }
    
    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to find all entities", e);
        }
    }
    
    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<ID> idList = new ArrayList<>();
        ids.forEach(idList::add);
        
        if (idList.isEmpty()) {
            return Collections.emptyList();
        }
        
        String placeholders = idList.stream().map(i -> "?").collect(Collectors.joining(", "));
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " IN (" + placeholders + ")";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < idList.size(); i++) {
                stmt.setObject(i + 1, idList.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to find entities by ids", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataException("Failed to count entities", e);
        }
    }
    
    @Override
    public void deleteById(ID id) {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to delete entity by id: " + id, e);
        }
    }
    
    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException("Use JdbcOrm for delete operations");
    }
    
    @Override
    public void deleteAllById(Iterable<ID> ids) {
        List<ID> idList = new ArrayList<>();
        ids.forEach(idList::add);
        
        if (idList.isEmpty()) {
            return;
        }
        
        String placeholders = idList.stream().map(i -> "?").collect(Collectors.joining(", "));
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " IN (" + placeholders + ")";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < idList.size(); i++) {
                stmt.setObject(i + 1, idList.get(i));
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to delete entities by ids", e);
        }
    }
    
    @Override
    public void deleteAll(Iterable<T> entities) {
        throw new UnsupportedOperationException("Use JdbcOrm for delete operations");
    }
    
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM " + tableName;
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to delete all entities", e);
        }
    }
    
    public Query<T> query() {
        try {
            Connection conn = connectionPool.getConnection();
            return new JdbcQuery<>(conn, entityClass, mapper, tableName);
        } catch (Exception e) {
            throw new DataException("Failed to create query", e);
        }
    }
}
