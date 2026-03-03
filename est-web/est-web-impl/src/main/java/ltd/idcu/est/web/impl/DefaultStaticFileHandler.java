package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.StaticFileHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultStaticFileHandler implements StaticFileHandler {

    private String pathPrefix = "/static";
    private String location = "static";
    private List<String> allowedExtensions = new ArrayList<>();
    private List<String> disallowedExtensions = new ArrayList<>();
    private String defaultMimeType = "application/octet-stream";
    private boolean cacheEnabled = true;
    private long cacheMaxAge = 3600; // 1小时

    @Override
    public String getPathPrefix() {
        return pathPrefix;
    }

    @Override
    public void setPathPrefix(String prefix) {
        this.pathPrefix = prefix;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    @Override
    public void setAllowedExtensions(List<String> extensions) {
        this.allowedExtensions = extensions;
    }

    @Override
    public void addAllowedExtension(String extension) {
        allowedExtensions.add(extension);
    }

    @Override
    public List<String> getDisallowedExtensions() {
        return disallowedExtensions;
    }

    @Override
    public void setDisallowedExtensions(List<String> extensions) {
        this.disallowedExtensions = extensions;
    }

    @Override
    public void addDisallowedExtension(String extension) {
        disallowedExtensions.add(extension);
    }

    @Override
    public boolean isAllowed(String fileName) {
        String extension = getFileExtension(fileName);
        if (disallowedExtensions.contains(extension)) {
            return false;
        }
        return allowedExtensions.isEmpty() || allowedExtensions.contains(extension);
    }

    @Override
    public Optional<StaticFile> resolve(String path) {
        String filePath = path.substring(pathPrefix.length());
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        Path fullPath = Paths.get(location, filePath);
        if (!Files.exists(fullPath) || !Files.isRegularFile(fullPath)) {
            return Optional.empty();
        }
        if (!isAllowed(fullPath.getFileName().toString())) {
            return Optional.empty();
        }
        return Optional.of(new DefaultStaticFile(fullPath));
    }

    @Override
    public boolean exists(String path) {
        return resolve(path).isPresent();
    }

    @Override
    public String getMimeType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return switch (extension) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "ico" -> "image/x-icon";
            case "svg" -> "image/svg+xml";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "zip" -> "application/zip";
            case "rar" -> "application/x-rar-compressed";
            case "mp3" -> "audio/mpeg";
            case "mp4" -> "video/mp4";
            case "avi" -> "video/x-msvideo";
            case "mov" -> "video/quicktime";
            default -> defaultMimeType;
        };
    }

    @Override
    public void setDefaultMimeType(String mimeType) {
        this.defaultMimeType = mimeType;
    }

    @Override
    public String getDefaultMimeType() {
        return defaultMimeType;
    }

    @Override
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    @Override
    public void setCacheEnabled(boolean enabled) {
        this.cacheEnabled = enabled;
    }

    @Override
    public long getCacheMaxAge() {
        return cacheMaxAge;
    }

    @Override
    public void setCacheMaxAge(long seconds) {
        this.cacheMaxAge = seconds;
    }

    @Override
    public void serve(String path, Request request, Response response) {
        Optional<StaticFile> staticFile = resolve(path);
        if (staticFile.isEmpty()) {
            response.setStatus(404);
            response.setContentType("text/plain");
            response.text("File not found");
            return;
        }

        StaticFile file = staticFile.get();
        response.setStatus(200);
        response.setContentType(file.getMimeType());
        response.setHeader("Content-Length", String.valueOf(file.getSize()));
        response.setHeader("Last-Modified", String.valueOf(file.getLastModified()));

        if (cacheEnabled) {
            response.setHeader("Cache-Control", "public, max-age=" + cacheMaxAge);
        }

        try (InputStream inputStream = file.getInputStream()) {
            response.setBody(inputStream);
        } catch (IOException e) {
            response.setStatus(500);
            response.setContentType("text/plain");
            response.text("Internal server error");
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }

    private static class DefaultStaticFile implements StaticFile {

        private Path path;

        public DefaultStaticFile(Path path) {
            this.path = path;
        }

        @Override
        public String getName() {
            return path.getFileName().toString();
        }

        @Override
        public String getPath() {
            return path.toString();
        }

        @Override
        public long getSize() {
            try {
                return Files.size(path);
            } catch (IOException e) {
                return 0;
            }
        }

        @Override
        public long getLastModified() {
            try {
                return Files.getLastModifiedTime(path).toMillis();
            } catch (IOException e) {
                return 0;
            }
        }

        @Override
        public String getMimeType() {
            String fileName = getName();
            String extension = getFileExtension(fileName).toLowerCase();
            return switch (extension) {
                case "html" -> "text/html";
                case "css" -> "text/css";
                case "js" -> "application/javascript";
                case "json" -> "application/json";
                case "xml" -> "application/xml";
                case "jpg", "jpeg" -> "image/jpeg";
                case "png" -> "image/png";
                case "gif" -> "image/gif";
                case "ico" -> "image/x-icon";
                case "svg" -> "image/svg+xml";
                case "pdf" -> "application/pdf";
                case "txt" -> "text/plain";
                case "zip" -> "application/zip";
                case "rar" -> "application/x-rar-compressed";
                case "mp3" -> "audio/mpeg";
                case "mp4" -> "video/mp4";
                case "avi" -> "video/x-msvideo";
                case "mov" -> "video/quicktime";
                default -> "application/octet-stream";
            };
        }

        @Override
        public String getExtension() {
            return getFileExtension(getName());
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(path);
        }

        @Override
        public byte[] readAllBytes() throws IOException {
            return Files.readAllBytes(path);
        }

        @Override
        public Path toPath() {
            return path;
        }

        private String getFileExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf('.');
            return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
        }
    }
}
