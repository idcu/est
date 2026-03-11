package ltd.idcu.est.agent.impl;

import ltd.idcu.est.agent.api.Memory;
import ltd.idcu.est.agent.api.MemoryItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryMemory implements Memory {
    
    private final Map<String, MemoryItem> items = new ConcurrentHashMap<>();
    
    @Override
    public void add(MemoryItem item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID().toString());
        }
        items.put(item.getId(), item);
    }
    
    @Override
    public void addAll(List<MemoryItem> memoryItems) {
        for (MemoryItem item : memoryItems) {
            add(item);
        }
    }
    
    @Override
    public List<MemoryItem> retrieve(String query, int topK) {
        return items.values().stream()
            .filter(item -> item.getContent().toLowerCase().contains(query.toLowerCase()))
            .sorted(Comparator.comparing(MemoryItem::getTimestamp).reversed())
            .limit(topK)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MemoryItem> getRecent(int count) {
        return items.values().stream()
            .sorted(Comparator.comparing(MemoryItem::getTimestamp).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MemoryItem> getAll() {
        return new ArrayList<>(items.values());
    }
    
    @Override
    public void clear() {
        items.clear();
    }
    
    @Override
    public void remove(String id) {
        items.remove(id);
    }
    
    @Override
    public int size() {
        return items.size();
    }
}
