package ltd.idcu.est.integration.api;

public interface OssFile {
    
    String getBucketName();
    
    String getFileName();
    
    String getFilePath();
    
    long getSize();
    
    String getContentType();
    
    long getLastModified();
    
    String getEtag();
}
