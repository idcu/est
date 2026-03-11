package ltd.idcu.est.agent.impl;

import ltd.idcu.est.agent.api.Memory;
import ltd.idcu.est.agent.api.MemoryItem;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;

public class InMemoryMemoryTest {
    
    @Test
    public void testCreateMemory() {
        Memory memory = new InMemoryMemory();
        assertNotNull(memory);
    }
    
    @Test
    public void testAddMemoryItem() {
        Memory memory = new InMemoryMemory();
        
        MemoryItem item = new MemoryItem();
        item.setId("item1");
        item.setType(MemoryItem.MemoryType.CONVERSATION);
        item.setContent("Hello, world!");
        
        memory.add(item);
        assertEquals(1, memory.size());
    }
    
    @Test
    public void testAddMultipleMemoryItems() {
        Memory memory = new InMemoryMemory();
        
        for (int i = 0; i < 5; i++) {
            MemoryItem item = new MemoryItem();
            item.setId("item" + i);
            item.setType(MemoryItem.MemoryType.CONVERSATION);
            item.setContent("Content " + i);
            memory.add(item);
        }
        
        assertEquals(5, memory.size());
    }
    
    @Test
    public void testAddAllMemoryItems() {
        Memory memory = new InMemoryMemory();
        
        MemoryItem item1 = new MemoryItem();
        item1.setId("item1");
        item1.setContent("Content 1");
        
        MemoryItem item2 = new MemoryItem();
        item2.setId("item2");
        item2.setContent("Content 2");
        
        memory.addAll(List.of(item1, item2));
        assertEquals(2, memory.size());
    }
    
    @Test
    public void testGetAllMemoryItems() {
        Memory memory = new InMemoryMemory();
        
        MemoryItem item1 = new MemoryItem();
        item1.setId("item1");
        item1.setContent("Content 1");
        
        MemoryItem item2 = new MemoryItem();
        item2.setId("item2");
        item2.setContent("Content 2");
        
        memory.add(item1);
        memory.add(item2);
        
        List<MemoryItem> items = memory.getAll();
        assertEquals(2, items.size());
    }
    
    @Test
    public void testGetByType() {
        Memory memory = new InMemoryMemory();
        
        MemoryItem convItem = new MemoryItem();
        convItem.setType(MemoryItem.MemoryType.CONVERSATION);
        convItem.setContent("Conversation");
        
        MemoryItem taskItem = new MemoryItem();
        taskItem.setType(MemoryItem.MemoryType.TASK);
        taskItem.setContent("Task");
        
        MemoryItem knowledgeItem = new MemoryItem();
        knowledgeItem.setType(MemoryItem.MemoryType.KNOWLEDGE);
        knowledgeItem.setContent("Knowledge");
        
        memory.add(convItem);
        memory.add(taskItem);
        memory.add(knowledgeItem);
        
        List<MemoryItem> convItems = memory.getByType(MemoryItem.MemoryType.CONVERSATION);
        assertEquals(1, convItems.size());
        
        List<MemoryItem> taskItems = memory.getByType(MemoryItem.MemoryType.TASK);
        assertEquals(1, taskItems.size());
        
        List<MemoryItem> knowledgeItems = memory.getByType(MemoryItem.MemoryType.KNOWLEDGE);
        assertEquals(1, knowledgeItems.size());
    }
    
    @Test
    public void testGetRecent() {
        Memory memory = new InMemoryMemory();
        
        for (int i = 0; i < 10; i++) {
            MemoryItem item = new MemoryItem();
            item.setId("item" + i);
            item.setContent("Content " + i);
            memory.add(item);
        }
        
        List<MemoryItem> recent = memory.getRecent(3);
        assertEquals(3, recent.size());
    }
    
    @Test
    public void testGetRecentMoreThanAvailable() {
        Memory memory = new InMemoryMemory();
        
        for (int i = 0; i < 5; i++) {
            MemoryItem item = new MemoryItem();
            item.setId("item" + i);
            item.setContent("Content " + i);
            memory.add(item);
        }
        
        List<MemoryItem> recent = memory.getRecent(10);
        assertEquals(5, recent.size());
    }
    
    @Test
    public void testRemoveMemoryItem() {
        Memory memory = new InMemoryMemory();
        
        MemoryItem item = new MemoryItem();
        item.setId("item1");
        item.setContent("Content");
        
        memory.add(item);
        assertEquals(1, memory.size());
        
        memory.remove("item1");
        assertEquals(0, memory.size());
    }
    
    @Test
    public void testClearMemory() {
        Memory memory = new InMemoryMemory();
        
        for (int i = 0; i < 10; i++) {
            MemoryItem item = new MemoryItem();
            item.setId("item" + i);
            item.setContent("Content " + i);
            memory.add(item);
        }
        
        assertEquals(10, memory.size());
        
        memory.clear();
        assertEquals(0, memory.size());
    }
    
    @Test
    public void testMemoryItemProperties() {
        MemoryItem item = new MemoryItem();
        
        item.setId("test-id");
        assertEquals("test-id", item.getId());
        
        item.setType(MemoryItem.MemoryType.CONVERSATION);
        assertEquals(MemoryItem.MemoryType.CONVERSATION, item.getType());
        
        item.setContent("Test content");
        assertEquals("Test content", item.getContent());
        
        item.setMetadata(Map.of("key", "value"));
        assertNotNull(item.getMetadata());
        assertEquals("value", item.getMetadata().get("key"));
    }
}
