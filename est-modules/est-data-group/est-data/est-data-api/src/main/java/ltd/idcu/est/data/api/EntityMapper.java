package ltd.idcu.est.data.api;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T> {

    T map(ResultSet rs) throws SQLException;
}
