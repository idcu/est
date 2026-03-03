package ltd.idcu.est.web.api;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface StaticFileHandler {

    String getPathPrefix();

    void setPathPrefix(String prefix);

    String getLocation();

    void setLocation(String location);

    List<String> getAllowedExtensions();

    void setAllowedExtensions(List<String> extensions);

    void addAllowedExtension(String extension);

    List<String> getDisallowedExtensions();

    void setDisallowedExtensions(List<String> extensions);

    void addDisallowedExtension(String extension);

    boolean isAllowed(String fileName);

    Optional<StaticFile> resolve(String path);

    boolean exists(String path);

    String getMimeType(String fileName);

    void setDefaultMimeType(String mimeType);

    String getDefaultMimeType();

    boolean isCacheEnabled();

    void setCacheEnabled(boolean enabled);

    long getCacheMaxAge();

    void setCacheMaxAge(long seconds);

    void serve(String path, Request request, Response response);

    interface StaticFile {
        String getName();
        String getPath();
        long getSize();
        long getLastModified();
        String getMimeType();
        String getExtension();
        InputStream getInputStream();
        byte[] readAllBytes();
        Path toPath();
    }
}
