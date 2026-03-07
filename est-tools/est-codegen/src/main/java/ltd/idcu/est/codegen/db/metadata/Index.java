package ltd.idcu.est.codegen.db.metadata;

import java.util.ArrayList;
import java.util.List;

public class Index {
    private String name;
    private boolean unique;
    private List<Column> columns = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    @Override
    public String toString() {
        return "Index{" +
                "name='" + name + '\'' +
                ", unique=" + unique +
                ", columns=" + columns +
                '}';
    }
}
