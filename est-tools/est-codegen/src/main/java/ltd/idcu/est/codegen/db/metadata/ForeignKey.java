package ltd.idcu.est.codegen.db.metadata;

public class ForeignKey {
    private String name;
    private Column localColumn;
    private String foreignTableName;
    private String foreignColumnName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Column getLocalColumn() {
        return localColumn;
    }

    public void setLocalColumn(Column localColumn) {
        this.localColumn = localColumn;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    @Override
    public String toString() {
        return "ForeignKey{" +
                "name='" + name + '\'' +
                ", localColumn=" + localColumn +
                ", foreignTableName='" + foreignTableName + '\'' +
                ", foreignColumnName='" + foreignColumnName + '\'' +
                '}';
    }
}
