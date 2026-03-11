package ltd.idcu.est.rag.api;

public class SearchResult {
    
    private String chunkId;
    private String documentId;
    private String content;
    private double score;
    private DocumentChunk chunk;
    private Document document;
    
    public SearchResult() {
    }
    
    public SearchResult(String chunkId, String content, double score) {
        this.chunkId = chunkId;
        this.content = content;
        this.score = score;
    }
    
    public String getChunkId() {
        return chunkId;
    }
    
    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }
    
    public DocumentChunk getChunk() {
        return chunk;
    }
    
    public void setChunk(DocumentChunk chunk) {
        this.chunk = chunk;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
}
