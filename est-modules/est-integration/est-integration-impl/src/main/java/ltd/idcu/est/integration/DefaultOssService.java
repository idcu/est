package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.IntegrationException;
import ltd.idcu.est.integration.api.OssFile;
import ltd.idcu.est.integration.api.OssService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DefaultOssService implements OssService {
    
    private static final Logger LOGGER = Logger.getLogger(DefaultOssService.class.getName());
    private final Map<String, Map<String, byte[]>> buckets;
    private final Map<String, Map<String, OssFile>> fileMetadata;
    
    public DefaultOssService() {
        this.buckets = new ConcurrentHashMap<>();
        this.fileMetadata = new ConcurrentHashMap<>();
    }
    
    @Override
    public String uploadFile(String bucketName, String fileName, InputStream inputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return uploadFile(bucketName, fileName, outputStream.toByteArray());
        } catch (Exception e) {
            throw new IntegrationException("Failed to upload file: " + fileName, e);
        }
    }
    
    @Override
    public String uploadFile(String bucketName, String fileName, byte[] data) {
        ensureBucketExists(bucketName);
        
        Map<String, byte[]> bucket = buckets.get(bucketName);
        Map<String, OssFile> metadata = fileMetadata.get(bucketName);
        
        bucket.put(fileName, data);
        OssFile file = new DefaultOssFile(
            bucketName,
            fileName,
            bucketName + "/" + fileName,
            data.length,
            guessContentType(fileName),
            System.currentTimeMillis(),
            generateEtag(data)
        );
        metadata.put(fileName, file);
        
        LOGGER.info("Uploaded file: " + bucketName + "/" + fileName);
        return file.getFilePath();
    }
    
    @Override
    public void downloadFile(String bucketName, String fileName, OutputStream outputStream) {
        byte[] data = downloadFile(bucketName, fileName);
        try {
            outputStream.write(data);
        } catch (Exception e) {
            throw new IntegrationException("Failed to download file: " + fileName, e);
        }
    }
    
    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        ensureBucketExists(bucketName);
        ensureFileExists(bucketName, fileName);
        
        return buckets.get(bucketName).get(fileName);
    }
    
    @Override
    public String getFileUrl(String bucketName, String fileName) {
        ensureBucketExists(bucketName);
        ensureFileExists(bucketName, fileName);
        
        return "https://oss.example.com/" + bucketName + "/" + fileName;
    }
    
    @Override
    public String getFileUrl(String bucketName, String fileName, long expireSeconds) {
        ensureBucketExists(bucketName);
        ensureFileExists(bucketName, fileName);
        
        long expiresAt = System.currentTimeMillis() / 1000 + expireSeconds;
        return "https://oss.example.com/" + bucketName + "/" + fileName + "?expires=" + expiresAt + "&signature=" + UUID.randomUUID().toString();
    }
    
    @Override
    public void deleteFile(String bucketName, String fileName) {
        ensureBucketExists(bucketName);
        
        Map<String, byte[]> bucket = buckets.get(bucketName);
        Map<String, OssFile> metadata = fileMetadata.get(bucketName);
        
        bucket.remove(fileName);
        metadata.remove(fileName);
        
        LOGGER.info("Deleted file: " + bucketName + "/" + fileName);
    }
    
    @Override
    public void deleteFiles(String bucketName, List<String> fileNames) {
        for (String fileName : fileNames) {
            deleteFile(bucketName, fileName);
        }
    }
    
    @Override
    public List<OssFile> listFiles(String bucketName) {
        return listFiles(bucketName, null);
    }
    
    @Override
    public List<OssFile> listFiles(String bucketName, String prefix) {
        ensureBucketExists(bucketName);
        List<OssFile> files = new ArrayList<>(fileMetadata.get(bucketName).values());
        
        if (prefix != null && !prefix.isEmpty()) {
            files.removeIf(file -> !file.getFileName().startsWith(prefix));
        }
        
        return files;
    }
    
    @Override
    public boolean exists(String bucketName, String fileName) {
        return buckets.containsKey(bucketName) && buckets.get(bucketName).containsKey(fileName);
    }
    
    @Override
    public long getFileSize(String bucketName, String fileName) {
        ensureBucketExists(bucketName);
        ensureFileExists(bucketName, fileName);
        
        return fileMetadata.get(bucketName).get(fileName).getSize();
    }
    
    @Override
    public void createBucket(String bucketName) {
        if (buckets.containsKey(bucketName)) {
            throw new IntegrationException("Bucket already exists: " + bucketName);
        }
        
        buckets.put(bucketName, new ConcurrentHashMap<>());
        fileMetadata.put(bucketName, new ConcurrentHashMap<>());
        
        LOGGER.info("Created bucket: " + bucketName);
    }
    
    @Override
    public void deleteBucket(String bucketName) {
        ensureBucketExists(bucketName);
        
        buckets.remove(bucketName);
        fileMetadata.remove(bucketName);
        
        LOGGER.info("Deleted bucket: " + bucketName);
    }
    
    @Override
    public List<String> listBuckets() {
        return new ArrayList<>(buckets.keySet());
    }
    
    private void ensureBucketExists(String bucketName) {
        if (!buckets.containsKey(bucketName)) {
            throw new IntegrationException("Bucket not found: " + bucketName);
        }
    }
    
    private void ensureFileExists(String bucketName, String fileName) {
        if (!exists(bucketName, fileName)) {
            throw new IntegrationException("File not found: " + bucketName + "/" + fileName);
        }
    }
    
    private String guessContentType(String fileName) {
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".pdf")) return "application/pdf";
        if (fileName.endsWith(".txt")) return "text/plain";
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
    
    private String generateEtag(byte[] data) {
        return Integer.toHexString(data.hashCode());
    }
}
