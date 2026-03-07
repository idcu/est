package ltd.idcu.est.features.data.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

    private List<T> records = new ArrayList<>();
    private long total = 0;
    private long pageNum = 1;
    private long pageSize = 10;
    private long pages = 0;

    public Page() {
    }

    public Page(long pageNum, long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Page(long pageNum, long pageSize, long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = calculatePages();
    }

    private long calculatePages() {
        if (pageSize <= 0) {
            return 0;
        }
        return total / pageSize + (total % pageSize == 0 ? 0 : 1);
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pages = calculatePages();
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
        this.pages = calculatePages();
    }

    public long getPages() {
        return pages;
    }

    public boolean hasPrevious() {
        return pageNum > 1;
    }

    public boolean hasNext() {
        return pageNum < pages;
    }

    public long getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
