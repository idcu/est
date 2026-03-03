package ltd.idcu.est.features.data.memory;

import ltd.idcu.est.features.data.api.*;
import java.util.List;

public final class MemoryData {
    
    private MemoryData() {
    }
    
    public static <T, ID> MemoryRepository<T, ID> newRepository() {
        return new MemoryRepository<>();
    }
    
    public static <T, ID> MemoryRepository.MemoryRepositoryBuilder<T, ID> repositoryBuilder() {
        return MemoryRepository.builder();
    }
    
    public static MemoryTransactionManager newTransactionManager() {
        return new MemoryTransactionManager();
    }
    
    public static MemoryOrm newOrm() {
        return new MemoryOrm();
    }
    
    public static <T> MemoryQuery<T> query(List<T> data, Class<T> entityClass) {
        return new MemoryQuery<>(data, entityClass);
    }
}
