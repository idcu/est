package ltd.idcu.est.utils.format.json;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ObjectPool<T> {
    
    private final BlockingQueue<T> pool;
    private final Supplier<T> factory;
    private final int maxSize;
    
    public ObjectPool(Supplier<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.pool = new ArrayBlockingQueue<>(maxSize);
    }
    
    public T borrow() {
        T obj = pool.poll();
        return obj != null ? obj : factory.get();
    }
    
    public void release(T obj) {
        if (obj != null) {
            pool.offer(obj);
        }
    }
    
    public void clear() {
        pool.clear();
    }
    
    public int size() {
        return pool.size();
    }
}

class PoolManager {
    
    private static final Map<Class<?>, ObjectPool<?>> pools = new ConcurrentHashMap<>();
    
    public static <T> ObjectPool<T> getOrCreatePool(Class<T> type, Supplier<T> factory, int maxSize) {
        @SuppressWarnings("unchecked")
        ObjectPool<T> pool = (ObjectPool<T>) pools.get(type);
        if (pool == null) {
            synchronized (PoolManager.class) {
                @SuppressWarnings("unchecked")
                ObjectPool<T> existingPool = (ObjectPool<T>) pools.get(type);
                if (existingPool != null) {
                    return existingPool;
                }
                pool = new ObjectPool<>(factory, maxSize);
                pools.put(type, pool);
            }
        }
        return pool;
    }
    
    public static void clearAll() {
        pools.values().forEach(ObjectPool::clear);
        pools.clear();
    }
}
