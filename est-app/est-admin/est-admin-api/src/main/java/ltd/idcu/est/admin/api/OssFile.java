package ltd.idcu.est.admin.api;

public interface OssFile {
    
    String getBucketName();
    
    String getFileName();
    
    String getFilePath();
    
    long getSize();
    
    String getContentType();
    
    long getLastModified();
    
    String getEtag();
}
