package ltd.idcu.est.web;

import ltd.idcu.est.utils.io.FileUtils;
import ltd.idcu.est.utils.io.IOUtils;
import ltd.idcu.est.web.api.MimeType;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.StaticFileHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DefaultStaticFileHandler implements StaticFileHandler {

    private static final DateTimeFormatter HTTP_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
                        .withZone(ZoneId.of("GMT"));

    private String pathPrefix;
    private String location;
    private List<String> allowedExtensions;
    private List<String> disallowedExtensions;
    private String defaultMimeType;
    private boolean cacheEnabled;
    private long cacheMaxAge;
    private boolean etagEnabled;
    private static final int CHUNK_SIZE = 8192;

    public DefaultStaticFileHandler() {
        this.pathPrefix = "/static";
        this.location = "public";
        this.allowedExtensions = new ArrayList<>();
        this.disallowedExtensions = new ArrayList<>();
        this.defaultMimeType = MimeType.getDefaultMimeType();
        this.cacheEnabled = true;
        this.cacheMaxAge = 3600;
        this.etagEnabled = true;
    }

    public DefaultStaticFileHandler(String pathPrefix, String location) {
        this();
        this.pathPrefix = pathPrefix;
        this.location = location;
    }

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
        return new ArrayList<>(allowedExtensions);
    }

    @Override
    public void setAllowedExtensions(List<String> extensions) {
        this.allowedExtensions = extensions != null ? new ArrayList<>(extensions) : new ArrayList<>();
    }

    @Override
    public void addAllowedExtension(String extension) {
        if (extension != null && !allowedExtensions.contains(extension)) {
            allowedExtensions.add(extension);
        }
    }

    @Override
    public List<String> getDisallowedExtensions() {
        return new ArrayList<>(disallowedExtensions);
    }

    @Override
    public void setDisallowedExtensions(List<String> extensions) {
        this.disallowedExtensions = extensions != null ? new ArrayList<>(extensions) : new ArrayList<>();
    }

    @Override
    public void addDisallowedExtension(String extension) {
        if (extension != null && !disallowedExtensions.contains(extension)) {
            disallowedExtensions.add(extension);
        }
    }

    @Override
    public boolean isAllowed(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        String extension = FileUtils.getExtension(fileName);
        if (!disallowedExtensions.isEmpty() && disallowedExtensions.contains(extension)) {
            return false;
        }
        if (!allowedExtensions.isEmpty() && !allowedExtensions.contains(extension)) {
            return false;
        }
        return true;
    }

    @Override
    public Optional<StaticFile> resolve(String path) {
        if (path == null || path.isEmpty()) {
            return Optional.empty();
        }
        String relativePath = path;
        if (pathPrefix != null && path.startsWith(pathPrefix)) {
            relativePath = path.substring(pathPrefix.length());
        }
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        Path filePath = Paths.get(location, relativePath);
        File file = filePath.toFile();
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            return Optional.empty();
        }
        if (!isAllowed(file.getName())) {
            return Optional.empty();
        }
        return Optional.of(new DefaultStaticFile(file));
    }

    @Override
    public boolean exists(String path) {
        return resolve(path).isPresent();
    }

    @Override
    public String getMimeType(String fileName) {
        return MimeType.get(fileName);
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

    public boolean isEtagEnabled() {
        return etagEnabled;
    }

    public void setEtagEnabled(boolean etagEnabled) {
        this.etagEnabled = etagEnabled;
    }

    @Override
    public void serve(String path, Request request, Response response) {
        Optional<StaticFile> staticFileOpt = resolve(path);
        if (staticFileOpt.isEmpty()) {
            response.sendError(404, "File not found");
            return;
        }
        
        StaticFile staticFile = staticFileOpt.get();
        File file = new File(staticFile.getPath());
        
        try {
            String etag = generateETag(file);
            String lastModified = formatHttpDate(file.lastModified());
            
            String ifNoneMatch = request.getHeader("If-None-Match");
            String ifModifiedSince = request.getHeader("If-Modified-Since");
            
            if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
                response.setStatus(304);
                return;
            }
            
            if (ifModifiedSince != null) {
                try {
                    long ifModifiedTime = parseHttpDate(ifModifiedSince);
                    if (file.lastModified() <= ifModifiedTime) {
                        response.setStatus(304);
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
            
            response.setContentType(staticFile.getMimeType());
            response.setHeader("Content-Length", String.valueOf(staticFile.getSize()));
            response.setHeader("Last-Modified", lastModified);
            
            if (cacheEnabled) {
                response.setHeader("Cache-Control", "max-age=" + cacheMaxAge);
            }
            
            if (etagEnabled) {
                response.setHeader("ETag", etag);
            }
            
            try (InputStream is = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[CHUNK_SIZE];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
            
        } catch (IOException e) {
            response.sendError(500, "Failed to serve file: " + e.getMessage());
        }
    }

    private String generateETag(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String content = file.getPath() + file.lastModified() + file.length();
            byte[] hash = md.digest(content.getBytes());
            StringBuilder sb = new StringBuilder("\"");
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            sb.append("\"");
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "\"" + file.lastModified() + "-" + file.length() + "\"";
        }
    }

    private String formatHttpDate(long timestamp) {
        return HTTP_DATE_FORMATTER.format(Instant.ofEpochMilli(timestamp));
    }

    private long parseHttpDate(String dateString) {
        try {
            return HTTP_DATE_FORMATTER.parse(dateString, Instant::from).toEpochMilli();
        } catch (Exception e) {
            return 0;
        }
    }

    private static class DefaultStaticFile implements StaticFile {
        private final File file;
        private final String mimeType;

        DefaultStaticFile(File file) {
            this.file = file;
            this.mimeType = MimeType.get(file.getName());
        }

        @Override
        public String getName() {
            return file.getName();
        }

        @Override
        public String getPath() {
            return file.getPath();
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public long getLastModified() {
            return file.lastModified();
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }

        @Override
        public String getExtension() {
            return FileUtils.getExtension(file);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(file.toPath());
        }

        @Override
        public byte[] readAllBytes() throws IOException {
            return Files.readAllBytes(file.toPath());
        }

        @Override
        public Path toPath() {
            return file.toPath();
        }
    }
}
