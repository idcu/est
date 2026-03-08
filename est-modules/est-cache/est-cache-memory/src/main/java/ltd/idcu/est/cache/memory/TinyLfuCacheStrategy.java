package ltd.idcu.est.cache.memory;

import ltd.idcu.est.cache.api.CacheEntry;
import ltd.idcu.est.cache.api.CacheStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class TinyLfuCacheStrategy<K, V> implements CacheStrategy<K, V> {
    
    private static final int DEFAULT_WINDOW_SIZE = 100;
    private static final int SAMPLE_SIZE = 100;
    
    private final int capacity;
    private final int windowSize;
    
    private final Map<K, CacheEntry<V>> mainCache;
    private final Map<K, CacheEntry<V>> windowCache;
    
    private final Map<K, LongAdder> frequencySketch;
    private final AtomicLong totalHits = new AtomicLong(0);
    private final AtomicLong resetCounter = new AtomicLong(0);
    
    public TinyLfuCacheStrategy(int capacity) {
        this(capacity, Math.min(DEFAULT_WINDOW_SIZE, capacity / 10));
    }
    
    public TinyLfuCacheStrategy(int capacity, int windowSize) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (windowSize <= 0 || windowSize >= capacity) {
            throw new IllegalArgumentException("Window size must be between 1 and " + (capacity - 1));
        }
        this.capacity = capacity;
        this.windowSize = windowSize;
        this.mainCache = new ConcurrentHashMap<>(capacity - windowSize);
        this.windowCache = new LinkedHashMap<>(windowSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                if (size() > windowSize) {
                    promoteToMain(eldest.getKey(), eldest.getValue());
                    return true;
                }
                return false;
            }
        };
        this.frequencySketch = new ConcurrentHashMap<>();
    }
    
    private void promoteToMain(K key, CacheEntry<V> entry) {
        while (mainCache.size() >= capacity - windowSize) {
            evictFromMain();
        }
        mainCache.put(key, entry);
    }
    
    private void evictFromMain() {
        if (mainCache.isEmpty()) {
            return;
        }
        
        K candidate1 = null;
        K candidate2 = null;
        long minFreq1 = Long.MAX_VALUE;
        long minFreq2 = Long.MAX_VALUE;
        
        int count = 0;
        for (K key : mainCache.keySet()) {
            long freq = getFrequency(key);
            if (freq < minFreq1) {
                minFreq2 = minFreq1;
                candidate2 = candidate1;
                minFreq1 = freq;
                candidate1 = key;
            } else if (freq < minFreq2) {
                minFreq2 = freq;
                candidate2 = key;
            }
            count++;
            if (count >= SAMPLE_SIZE) {
                break;
            }
        }
        
        K toEvict = (candidate2 != null && Math.random() < 0.5) ? candidate2 : candidate1;
        if (toEvict != null) {
            mainCache.remove(toEvict);
            frequencySketch.remove(toEvict);
        }
    }
    
    private long getFrequency(K key) {
        LongAdder adder = frequencySketch.get(key);
        return adder != null ? adder.sum() : 0;
    }
    
    private void incrementFrequency(K key) {
        frequencySketch.computeIfAbsent(key, k -> new LongAdder()).increment();
        
        if (resetCounter.incrementAndGet() >= capacity * 10) {
            resetFrequencies();
            resetCounter.set(0);
        }
    }
    
    private void resetFrequencies() {
        for (LongAdder adder : frequencySketch.values()) {
            long sum = adder.sum();
            adder.reset();
            if (sum > 1) {
                adder.add(sum / 2);
            }
        }
    }
    
    @Override
    public void onAccess(K key, CacheEntry<V> entry) {
        totalHits.incrementAndGet();
        incrementFrequency(key);
    }
    
    @Override
    public void onPut(K key, CacheEntry<V> entry) {
        if (mainCache.containsKey(key)) {
            mainCache.put(key, entry);
            incrementFrequency(key);
        } else if (windowCache.containsKey(key)) {
            windowCache.put(key, entry);
            incrementFrequency(key);
        } else {
            windowCache.put(key, entry);
            incrementFrequency(key);
        }
    }
    
    @Override
    public Optional<K> evict() {
        if (!windowCache.isEmpty()) {
            K eldestKey = windowCache.keySet().iterator().next();
            windowCache.remove(eldestKey);
            frequencySketch.remove(eldestKey);
            return Optional.of(eldestKey);
        }
        if (!mainCache.isEmpty()) {
            evictFromMain();
            return mainCache.keySet().stream().findFirst();
        }
        return Optional.empty();
    }
    
    @Override
    public int size() {
        return windowCache.size() + mainCache.size();
    }
    
    @Override
    public int capacity() {
        return capacity;
    }
    
    @Override
    public void clear() {
        windowCache.clear();
        mainCache.clear();
        frequencySketch.clear();
        totalHits.set(0);
        resetCounter.set(0);
    }
    
    @Override
    public void setCapacity(int capacity) {
        throw new UnsupportedOperationException("Cannot change capacity of TinyLfuCacheStrategy");
    }
    
    public boolean containsKey(K key) {
        return windowCache.containsKey(key) || mainCache.containsKey(key);
    }
}
