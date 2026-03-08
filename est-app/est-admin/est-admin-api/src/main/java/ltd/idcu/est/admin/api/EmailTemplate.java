package ltd.idcu.est.admin.api;

public interface EmailTemplate {
    
    String getName();
    
    String getSubject();
    
    String getContent();
    
    boolean isHtml();
    
    long getCreatedAt();
    
    long getUpdatedAt();
}
