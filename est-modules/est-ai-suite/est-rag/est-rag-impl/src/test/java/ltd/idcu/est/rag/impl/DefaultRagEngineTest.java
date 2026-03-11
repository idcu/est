package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class DefaultRagEngineTest {
    
    @Test
    public void testCreateRagEngine() {
        RagEngine engine = new DefaultRagEngine();
        assertNotNull(engine);
    }
    
    @Test
    public void testCreateRagEngineWithComponents() {
        TextSplitter splitter = new FixedSizeTextSplitter();
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        VectorStore vectorStore = new InMemoryVectorStore(embeddingModel);
        
        RagEngine engine = new DefaultRagEngine(vectorStore, splitter, embeddingModel);
        assertNotNull(engine);
    }
    
    @Test
    public void testSetAndGetVectorStore() {
        DefaultRagEngine engine = new DefaultRagEngine();
        
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        engine.setVectorStore(store);
        assertEquals(store, engine.getVectorStore());
    }
    
    @Test
    public void testSetAndGetTextSplitter() {
        DefaultRagEngine engine = new DefaultRagEngine();
        
        TextSplitter splitter = new FixedSizeTextSplitter();
        engine.setTextSplitter(splitter);
        assertEquals(splitter, engine.getTextSplitter());
    }
    
    @Test
    public void testSetAndGetEmbeddingModel() {
        DefaultRagEngine engine = new DefaultRagEngine();
        
        EmbeddingModel model = new SimpleEmbeddingModel();
        engine.setEmbeddingModel(model);
        assertEquals(model, engine.getEmbeddingModel());
    }
    
    @Test
    public void testAddDocument() {
        RagEngine engine = new DefaultRagEngine();
        
        Document doc = new Document("doc1", "EST Framework is a powerful Java development framework.");
        engine.addDocument(doc);
        
        VectorStore store = engine.getVectorStore();
        assertTrue(store.size() > 0);
    }
    
    @Test
    public void testAddDocuments() {
        RagEngine engine = new DefaultRagEngine();
        
        Document doc1 = new Document("doc1", "Content 1");
        Document doc2 = new Document("doc2", "Content 2");
        Document doc3 = new Document("doc3", "Content 3");
        
        engine.addDocuments(List.of(doc1, doc2, doc3));
        
        VectorStore store = engine.getVectorStore();
        assertTrue(store.size() >= 3);
    }
    
    @Test
    public void testRetrieve() {
        RagEngine engine = new DefaultRagEngine();
        
        Document doc1 = new Document("doc1", "EST Framework documentation");
        Document doc2 = new Document("doc2", "Java programming guide");
        
        engine.addDocuments(List.of(doc1, doc2));
        
        List<SearchResult> results = engine.retrieve("EST", 5);
        assertNotNull(results);
    }
    
    @Test
    public void testRetrieveTopK() {
        RagEngine engine = new DefaultRagEngine();
        
        for (int i = 0; i < 10; i++) {
            Document doc = new Document("doc" + i, "Content " + i);
            engine.addDocument(doc);
        }
        
        List<SearchResult> results = engine.retrieve("test", 3);
        assertTrue(results.size() <= 3);
    }
}
