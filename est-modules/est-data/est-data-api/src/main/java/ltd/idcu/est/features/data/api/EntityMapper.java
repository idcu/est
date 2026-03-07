package ltd.idcu.est.features.data.api;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T> {
    
    T map(ResultSet rs) throws SQLException;
    
    T map(ResultSet rs, int rowNum) throws SQLException;
}
