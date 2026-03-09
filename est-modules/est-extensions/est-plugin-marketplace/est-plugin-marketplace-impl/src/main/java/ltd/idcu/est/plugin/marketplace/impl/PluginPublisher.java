package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.PluginRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class PluginPublisher {
    
    private final PluginRepository repository;
    private final String stagingDirectory;
    
    public PluginPublisher(PluginRepository repository, String stagingDirectory) {
        this.repository = repository;
        this.stagingDirectory = stagingDirectory;
        ensureStagingDirectory();
    }
    
    public PluginPublisher(PluginRepository repository) {
        this(repository, System.getProperty("java.io.tmpdir") + "/est-plugin-staging");
    }
    
    private void ensureStagingDirectory() {
        File dir = new File(stagingDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public PublishResult publishPlugin(PluginInfo pluginInfo, Path pluginFile) throws IOException {
        return publishPlugin(pluginInfo, pluginFile.toFile());
    }
    
    public PublishResult publishPlugin(PluginInfo pluginInfo, File pluginFile) throws IOException {
        PublishResult result = new PublishResult();
        result.setPluginId(pluginInfo.getName());
        result.setVersion(pluginInfo.getVersion());
        
        try {
            List<String> validationErrors = validatePlugin(pluginFile, pluginInfo);
            if (!validationErrors.isEmpty()) {
                result.setSuccess(false);
                result.setErrors(validationErrors);
                return result;
            }
            
            Path stagedFile = stagePlugin(pluginFile, pluginInfo);
            
            byte[] pluginData = Files.readAllBytes(stagedFile);
            boolean uploaded = repository.uploadPlugin(pluginInfo, pluginData);
            
            result.setSuccess(uploaded);
            if (uploaded) {
                result.setMessage("Plugin published successfully");
            } else {
                result.setMessage("Failed to upload plugin to repository");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Error publishing plugin: " + e.getMessage());
            result.setException(e);
        }
        
        return result;
    }
    
    public PublishResult publishNewVersion(String pluginId, String newVersion, Path pluginFile, String changelog) throws IOException {
        Optional<PluginInfo> existingOpt = repository.getPlugin(pluginId);
        if (existingOpt.isEmpty()) {
            PublishResult result = new PublishResult();
            result.setSuccess(false);
            result.setMessage("Plugin not found: " + pluginId);
            return result;
        }
        
        PluginInfo existing = existingOpt.get();
        PluginInfo newPluginInfo = PluginInfo.builder()
            .name(existing.getName())
            .version(newVersion)
            .description(existing.getDescription())
            .author(existing.getAuthor())
            .mainClass(existing.getMainClass())
            .dependencies(existing.getDependencies())
            .softDependencies(existing.getSoftDependencies())
            .category(existing.getCategory())
            .tags(existing.getTags())
            .icon(existing.getIcon())
            .homepage(existing.getHomepage())
            .repository(existing.getRepository())
            .license(existing.getLicense())
            .rating(existing.getRating())
            .downloadCount(existing.getDownloadCount())
            .screenshots(existing.getScreenshots())
            .changelog(changelog)
            .publishTime(System.currentTimeMillis())
            .lastUpdateTime(System.currentTimeMillis())
            .certified(existing.isCertified())
            .compatibleVersions(existing.getCompatibleVersions())
            .minFrameworkVersion(existing.getMinFrameworkVersion())
            .build();
        
        return publishPlugin(newPluginInfo, pluginFile);
    }
    
    public boolean withdrawPlugin(String pluginId) {
        return repository.deletePlugin(pluginId);
    }
    
    private List<String> validatePlugin(File pluginFile, PluginInfo pluginInfo) {
        List<String> errors = new ArrayList<>();
        
        if (!pluginFile.exists()) {
            errors.add("Plugin file does not exist");
            return errors;
        }
        
        if (!pluginFile.getName().endsWith(".jar")) {
            errors.add("Plugin must be a JAR file");
        }
        
        if (pluginInfo.getName() == null || pluginInfo.getName().trim().isEmpty()) {
            errors.add("Plugin name is required");
        }
        
        if (pluginInfo.getVersion() == null || pluginInfo.getVersion().trim().isEmpty()) {
            errors.add("Plugin version is required");
        }
        
        if (pluginInfo.getMainClass() == null || pluginInfo.getMainClass().trim().isEmpty()) {
            errors.add("Plugin main class is required");
        }
        
        try {
            validateJarStructure(pluginFile);
        } catch (IOException e) {
            errors.add("Invalid JAR file: " + e.getMessage());
        }
        
        return errors;
    }
    
    private void validateJarStructure(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            boolean hasManifest = jar.getJarEntry("META-INF/MANIFEST.MF") != null;
            if (!hasManifest) {
                throw new IOException("JAR file missing MANIFEST.MF");
            }
        }
    }
    
    private Path stagePlugin(File pluginFile, PluginInfo pluginInfo) throws IOException {
        String fileName = pluginInfo.getName() + "-" + pluginInfo.getVersion() + ".jar";
        Path targetPath = Path.of(stagingDirectory, fileName);
        Files.copy(pluginFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath;
    }
    
    public static class PublishResult {
        private String pluginId;
        private String version;
        private boolean success;
        private String message;
        private List<String> errors = new ArrayList<>();
        private Exception exception;
        
        public String getPluginId() {
            return pluginId;
        }
        
        public void setPluginId(String pluginId) {
            this.pluginId = pluginId;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
        
        public Exception getException() {
            return exception;
        }
        
        public void setException(Exception exception) {
            this.exception = exception;
        }
    }
    
}
