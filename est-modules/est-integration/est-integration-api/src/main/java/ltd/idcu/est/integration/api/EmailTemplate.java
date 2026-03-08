package ltd.idcu.est.integration.api;

public interface EmailTemplate {
    
    String getName();
    
    String getSubject();
    
    String getContent();
    
    boolean isHtml();
    
    long getCreatedAt();
    
    long getUpdatedAt();
}
