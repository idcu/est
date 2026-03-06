package ltd.idcu.est.dbgenerator.metadata;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKey {
    private String name;
    private List<Column> columns = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Column getFirstColumn() {
        return columns.isEmpty() ? null : columns.get(0);
    }

    public boolean isComposite() {
        return columns.size() > 1;
    }

    @Override
    public String toString() {
        return "PrimaryKey{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                '}';
    }
}
