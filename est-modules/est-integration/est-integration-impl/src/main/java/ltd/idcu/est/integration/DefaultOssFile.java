package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.OssFile;

public class DefaultOssFile implements OssFile {
    
    private final String bucketName;
    private final String fileName;
    private final String filePath;
    private final long size;
    private final String contentType;
    private final long lastModified;
    private final String etag;
    
    public DefaultOssFile(String bucketName, String fileName, String filePath, long size, 
                     String contentType, long lastModified, String etag) {
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.lastModified = lastModified;
        this.etag = etag;
    }
    
    @Override
    public String getBucketName() {
        return bucketName;
    }
    
    @Override
    public String getFileName() {
        return fileName;
    }
    
    @Override
    public String getFilePath() {
        return filePath;
    }
    
    @Override
    public long getSize() {
        return size;
    }
    
    @Override
    public String getContentType() {
        return contentType;
    }
    
    @Override
    public long getLastModified() {
        return lastModified;
    }
    
    @Override
    public String getEtag() {
        return etag;
    }
}
