package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.OssService;
import ltd.idcu.est.admin.api.OssFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultOssService implements OssService {
    
    private final Map<String, Map<String, DefaultOssFile>> buckets = new ConcurrentHashMap<>();
    
    public DefaultOssService() {
        initializeDefaultBuckets();
    }
    
    private void initializeDefaultBuckets() {
        buckets.put("public", new ConcurrentHashMap<>());
        buckets.put("private", new ConcurrentHashMap<>());
        buckets.put("temp", new ConcurrentHashMap<>());
    }
    
    @Override
    public String uploadFile(String bucketName, String fileName, InputStream inputStream) {
        try {
            byte[] data = inputStream.readAllBytes();
            return uploadFile(bucketName, fileName, data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public String uploadFile(String bucketName, String fileName, byte[] data) {
        Map<String, DefaultOssFile> bucket = buckets.computeIfAbsent(bucketName, k -> new ConcurrentHashMap<>());
        
        DefaultOssFile file = new DefaultOssFile(
            bucketName,
            fileName,
            "/" + bucketName + "/" + fileName,
            data.length,
            guessContentType(fileName),
            System.currentTimeMillis(),
            generateEtag(data)
        );
        
        file.setData(data);
        bucket.put(fileName, file);
        
        System.out.println("[OSS] Uploaded file: " + bucketName + "/" + fileName + ", size: " + data.length + " bytes");
        return getFileUrl(bucketName, fileName);
    }
    
    @Override
    public void downloadFile(String bucketName, String fileName, OutputStream outputStream) {
        try {
            byte[] data = downloadFile(bucketName, fileName);
            outputStream.write(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
    
    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        Map<String, DefaultOssFile> bucket = buckets.get(bucketName);
        if (bucket == null) {
            throw new IllegalArgumentException("Bucket not found: " + bucketName);
        }
        
        DefaultOssFile file = bucket.get(fileName);
        if (file == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        
        System.out.println("[OSS] Downloaded file: " + bucketName + "/" + fileName);
        return file.getData();
    }
    
    @Override
    public String getFileUrl(String bucketName, String fileName) {
        return "http://localhost:8080/oss/" + bucketName + "/" + fileName;
    }
    
    @Override
    public String getFileUrl(String bucketName, String fileName, long expireSeconds) {
        return getFileUrl(bucketName, fileName) + "?expire=" + (System.currentTimeMillis() + expireSeconds * 1000);
    }
    
    @Override
    public void deleteFile(String bucketName, String fileName) {
        Map<String, DefaultOssFile> bucket = buckets.get(bucketName);
        if (bucket != null) {
            bucket.remove(fileName);
            System.out.println("[OSS] Deleted file: " + bucketName + "/" + fileName);
        }
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
        Map<String, DefaultOssFile> bucket = buckets.get(bucketName);
        if (bucket == null) {
            return Collections.emptyList();
        }
        
        List<OssFile> files = new ArrayList<>();
        for (DefaultOssFile file : bucket.values()) {
            if (prefix == null || file.getFileName().startsWith(prefix)) {
                files.add(file);
            }
        }
        return files;
    }
    
    @Override
    public boolean exists(String bucketName, String fileName) {
        Map<String, DefaultOssFile> bucket = buckets.get(bucketName);
        return bucket != null && bucket.containsKey(fileName);
    }
    
    @Override
    public long getFileSize(String bucketName, String fileName) {
        Map<String, DefaultOssFile> bucket = buckets.get(bucketName);
        if (bucket == null) return -1;
        DefaultOssFile file = bucket.get(fileName);
        return file != null ? file.getSize() : -1;
    }
    
    @Override
    public void createBucket(String bucketName) {
        buckets.computeIfAbsent(bucketName, k -> new ConcurrentHashMap<>());
        System.out.println("[OSS] Created bucket: " + bucketName);
    }
    
    @Override
    public void deleteBucket(String bucketName) {
        buckets.remove(bucketName);
        System.out.println("[OSS] Deleted bucket: " + bucketName);
    }
    
    @Override
    public List<String> listBuckets() {
        return new ArrayList<>(buckets.keySet());
    }
    
    private String guessContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".pdf")) return "application/pdf";
        if (fileName.endsWith(".txt")) return "text/plain";
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
    
    private String generateEtag(byte[] data) {
        return Integer.toHexString(Arrays.hashCode(data));
    }
    
    public static class DefaultOssFile implements OssFile {
        private final String bucketName;
        private final String fileName;
        private final String filePath;
        private final long size;
        private final String contentType;
        private final long lastModified;
        private final String etag;
        private byte[] data;
        
        public DefaultOssFile(String bucketName, String fileName, String filePath, 
                            long size, String contentType, long lastModified, String etag) {
            this.bucketName = bucketName;
            this.fileName = fileName;
            this.filePath = filePath;
            this.size = size;
            this.contentType = contentType;
            this.lastModified = lastModified;
            this.etag = etag;
        }
        
        @Override
        public String getBucketName() { return bucketName; }
        
        @Override
        public String getFileName() { return fileName; }
        
        @Override
        public String getFilePath() { return filePath; }
        
        @Override
        public long getSize() { return size; }
        
        @Override
        public String getContentType() { return contentType; }
        
        @Override
        public long getLastModified() { return lastModified; }
        
        @Override
        public String getEtag() { return etag; }
        
        public byte[] getData() { return data; }
        
        public void setData(byte[] data) { this.data = data; }
    }
}
