package ltd.idcu.est.admin.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface OssService {
    
    String uploadFile(String bucketName, String fileName, InputStream inputStream);
    
    String uploadFile(String bucketName, String fileName, byte[] data);
    
    void downloadFile(String bucketName, String fileName, OutputStream outputStream);
    
    byte[] downloadFile(String bucketName, String fileName);
    
    String getFileUrl(String bucketName, String fileName);
    
    String getFileUrl(String bucketName, String fileName, long expireSeconds);
    
    void deleteFile(String bucketName, String fileName);
    
    void deleteFiles(String bucketName, List<String> fileNames);
    
    List<OssFile> listFiles(String bucketName);
    
    List<OssFile> listFiles(String bucketName, String prefix);
    
    boolean exists(String bucketName, String fileName);
    
    long getFileSize(String bucketName, String fileName);
    
    void createBucket(String bucketName);
    
    void deleteBucket(String bucketName);
    
    List<String> listBuckets();
}
