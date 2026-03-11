package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Embedding;
import ltd.idcu.est.rag.api.EmbeddingModel;
import ltd.idcu.est.rag.api.SearchResult;
import ltd.idcu.est.rag.api.VectorStore;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class InMemoryVectorStoreTest {
    
    @Test
    public void testCreateVectorStore() {
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(embeddingModel);
        assertNotNull(store);
    }
    
    @Test
    public void testSetAndGetEmbeddingModel() {
        EmbeddingModel model1 = new SimpleEmbeddingModel();
        InMemoryVectorStore store = new InMemoryVectorStore(model1);
        
        assertEquals(model1, store.getEmbeddingModel());
        
        EmbeddingModel model2 = new SimpleEmbeddingModel();
        store.setEmbeddingModel(model2);
        assertEquals(model2, store.getEmbeddingModel());
    }
    
    @Test
    public void testAddEmbedding() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding embedding = new Embedding("emb1", "doc1", "Test content", new float[1536]);
        store.addEmbedding(embedding);
        
        assertEquals(1, store.size());
    }
    
    @Test
    public void testAddEmbeddings() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding emb1 = new Embedding("emb1", "doc1", "Content 1", new float[1536]);
        Embedding emb2 = new Embedding("emb2", "doc2", "Content 2", new float[1536]);
        
        store.addEmbeddings(List.of(emb1, emb2));
        assertEquals(2, store.size());
    }
    
    @Test
    public void testDeleteEmbedding() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding embedding = new Embedding("emb1", "doc1", "Test content", new float[1536]);
        store.addEmbedding(embedding);
        assertEquals(1, store.size());
        
        store.delete("emb1");
        assertEquals(0, store.size());
    }
    
    @Test
    public void testDeleteByDocumentId() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding emb1 = new Embedding("emb1", "doc1", "Content 1", new float[1536]);
        Embedding emb2 = new Embedding("emb2", "doc1", "Content 2", new float[1536]);
        Embedding emb3 = new Embedding("emb3", "doc2", "Content 3", new float[1536]);
        
        store.addEmbeddings(List.of(emb1, emb2, emb3));
        assertEquals(3, store.size());
        
        store.deleteByDocumentId("doc1");
        assertEquals(1, store.size());
    }
    
    @Test
    public void testClear() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding emb1 = new Embedding("emb1", "doc1", "Content 1", new float[1536]);
        Embedding emb2 = new Embedding("emb2", "doc2", "Content 2", new float[1536]);
        
        store.addEmbeddings(List.of(emb1, emb2));
        assertEquals(2, store.size());
        
        store.clear();
        assertEquals(0, store.size());
    }
    
    @Test
    public void testSearchByQuery() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding emb1 = new Embedding("emb1", "doc1", "EST Framework documentation", new float[1536]);
        Embedding emb2 = new Embedding("emb2", "doc2", "Java programming guide", new float[1536]);
        
        store.addEmbeddings(List.of(emb1, emb2));
        
        List<SearchResult> results = store.search("EST", 5);
        assertNotNull(results);
    }
    
    @Test
    public void testSearchByVector() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        Embedding emb1 = new Embedding("emb1", "doc1", "Test content", new float[1536]);
        store.addEmbedding(emb1);
        
        float[] queryVector = new float[1536];
        List<SearchResult> results = store.search(queryVector, 5);
        
        assertNotNull(results);
    }
    
    @Test
    public void testSearchTopK() {
        EmbeddingModel model = new SimpleEmbeddingModel();
        VectorStore store = new InMemoryVectorStore(model);
        
        for (int i = 0; i < 10; i++) {
            Embedding emb = new Embedding("emb" + i, "doc" + i, "Content " + i, new float[1536]);
            store.addEmbedding(emb);
        }
        
        List<SearchResult> results = store.search("test", 3);
        assertTrue(results.size() <= 3);
    }
}
