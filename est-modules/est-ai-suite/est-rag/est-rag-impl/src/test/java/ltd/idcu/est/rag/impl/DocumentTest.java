package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Document;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

public class DocumentTest {
    
    @Test
    public void testCreateDocument() {
        Document doc = new Document();
        assertNotNull(doc);
    }
    
    @Test
    public void testDocumentWithId() {
        Document doc = new Document("doc1", "Test content");
        assertEquals("doc1", doc.getId());
        assertEquals("Test content", doc.getContent());
    }
    
    @Test
    public void testSetAndGetId() {
        Document doc = new Document();
        doc.setId("test-id");
        assertEquals("test-id", doc.getId());
    }
    
    @Test
    public void testSetAndGetContent() {
        Document doc = new Document();
        doc.setContent("Hello, world!");
        assertEquals("Hello, world!", doc.getContent());
    }
    
    @Test
    public void testSetAndGetTitle() {
        Document doc = new Document();
        doc.setTitle("Test Title");
        assertEquals("Test Title", doc.getTitle());
    }
    
    @Test
    public void testSetAndGetSource() {
        Document doc = new Document();
        doc.setSource("https://example.com");
        assertEquals("https://example.com", doc.getSource());
    }
    
    @Test
    public void testSetAndGetContentType() {
        Document doc = new Document();
        doc.setContentType("text/markdown");
        assertEquals("text/markdown", doc.getContentType());
    }
    
    @Test
    public void testFullDocument() {
        Document doc = new Document();
        doc.setId("doc-001");
        doc.setTitle("EST Framework Guide");
        doc.setContent("EST is a powerful framework...");
        doc.setSource("https://est.example.com/docs");
        doc.setContentType("text/markdown");
        
        assertEquals("doc-001", doc.getId());
        assertEquals("EST Framework Guide", doc.getTitle());
        assertEquals("EST is a powerful framework...", doc.getContent());
        assertEquals("https://est.example.com/docs", doc.getSource());
        assertEquals("text/markdown", doc.getContentType());
    }
}
