package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Document;
import ltd.idcu.est.rag.api.DocumentChunk;
import ltd.idcu.est.rag.api.TextSplitter;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class FixedSizeTextSplitterTest {
    
    @Test
    public void testCreateSplitter() {
        TextSplitter splitter = new FixedSizeTextSplitter();
        assertNotNull(splitter);
    }
    
    @Test
    public void testDefaultChunkSize() {
        FixedSizeTextSplitter splitter = new FixedSizeTextSplitter();
        assertEquals(1024, splitter.getChunkSize());
    }
    
    @Test
    public void testDefaultChunkOverlap() {
        FixedSizeTextSplitter splitter = new FixedSizeTextSplitter();
        assertEquals(200, splitter.getChunkOverlap());
    }
    
    @Test
    public void testSetChunkSize() {
        FixedSizeTextSplitter splitter = new FixedSizeTextSplitter();
        splitter.setChunkSize(512);
        assertEquals(512, splitter.getChunkSize());
    }
    
    @Test
    public void testSetChunkOverlap() {
        FixedSizeTextSplitter splitter = new FixedSizeTextSplitter();
        splitter.setChunkOverlap(100);
        assertEquals(100, splitter.getChunkOverlap());
    }
    
    @Test
    public void testSplitDocument() {
        TextSplitter splitter = new FixedSizeTextSplitter(100, 20);
        
        String content = "EST Framework is a powerful Java development framework. " +
                         "It provides modular design and zero-dependency core architecture. " +
                         "EST Framework supports multiple languages and SDKs.";
        
        Document doc = new Document("doc1", content);
        List<DocumentChunk> chunks = splitter.split(doc);
        
        assertNotNull(chunks);
        assertFalse(chunks.isEmpty());
    }
    
    @Test
    public void testSplitString() {
        TextSplitter splitter = new FixedSizeTextSplitter(50, 10);
        
        String content = "This is a test document for splitting. " +
                         "It has multiple sentences to test the splitter.";
        
        List<DocumentChunk> chunks = splitter.split(content, "doc1");
        
        assertNotNull(chunks);
        assertFalse(chunks.isEmpty());
        
        for (DocumentChunk chunk : chunks) {
            assertEquals("doc1", chunk.getDocumentId());
            assertNotNull(chunk.getContent());
        }
    }
    
    @Test
    public void testChunkOrder() {
        TextSplitter splitter = new FixedSizeTextSplitter(30, 5);
        
        String content = "123456789012345678901234567890";
        List<DocumentChunk> chunks = splitter.split(content, "test");
        
        for (int i = 0; i < chunks.size(); i++) {
            assertEquals(i, chunks.get(i).getIndex());
        }
    }
}
