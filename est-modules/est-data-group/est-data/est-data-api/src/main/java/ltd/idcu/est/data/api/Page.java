package ltd.idcu.est.data.api;

import java.util.List;

public interface Page<T> {
    long getPageNum();
    long getPageSize();
    long getTotal();
    long getPages();
    long getOffset();
    List<T> getRecords();
    void setTotal(long total);
    void setRecords(List<T> records);
}