package ltd.idcu.est.web.api;

import java.util.HashMap;
import java.util.Map;

public final class MimeType {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("htm", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("xml", "application/xml");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("csv", "text/csv");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("doc", "application/msword");
        MIME_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_TYPES.put("xls", "application/vnd.ms-excel");
        MIME_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MIME_TYPES.put("zip", "application/zip");
        MIME_TYPES.put("rar", "application/x-rar-compressed");
        MIME_TYPES.put("7z", "application/x-7z-compressed");
        MIME_TYPES.put("tar", "application/x-tar");
        MIME_TYPES.put("gz", "application/gzip");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("ico", "image/x-icon");
        MIME_TYPES.put("bmp", "image/bmp");
        MIME_TYPES.put("webp", "image/webp");
        MIME_TYPES.put("mp3", "audio/mpeg");
        MIME_TYPES.put("wav", "audio/wav");
        MIME_TYPES.put("ogg", "audio/ogg");
        MIME_TYPES.put("mp4", "video/mp4");
        MIME_TYPES.put("avi", "video/x-msvideo");
        MIME_TYPES.put("mov", "video/quicktime");
        MIME_TYPES.put("wmv", "video/x-ms-wmv");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("webm", "video/webm");
        MIME_TYPES.put("woff", "font/woff");
        MIME_TYPES.put("woff2", "font/woff2");
        MIME_TYPES.put("ttf", "font/ttf");
        MIME_TYPES.put("otf", "font/otf");
        MIME_TYPES.put("eot", "application/vnd.ms-fontobject");
        MIME_TYPES.put("swf", "application/x-shockwave-flash");
        MIME_TYPES.put("manifest", "text/cache-manifest");
        MIME_TYPES.put("appcache", "text/cache-manifest");
        MIME_TYPES.put("wasm", "application/wasm");
        MIME_TYPES.put("map", "application/json");
        MIME_TYPES.put("yaml", "application/x-yaml");
        MIME_TYPES.put("yml", "application/x-yaml");
        MIME_TYPES.put("md", "text/markdown");
        MIME_TYPES.put("markdown", "text/markdown");
    }

    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    private MimeType() {
    }

    public static String get(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return DEFAULT_MIME_TYPE;
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0 || lastDot >= fileName.length() - 1) {
            return DEFAULT_MIME_TYPE;
        }
        String extension = fileName.substring(lastDot + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(extension, DEFAULT_MIME_TYPE);
    }

    public static String getByExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return DEFAULT_MIME_TYPE;
        }
        String ext = extension.toLowerCase();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }
        return MIME_TYPES.getOrDefault(ext, DEFAULT_MIME_TYPE);
    }

    public static void register(String extension, String mimeType) {
        if (extension != null && mimeType != null) {
            String ext = extension.toLowerCase();
            if (ext.startsWith(".")) {
                ext = ext.substring(1);
            }
            MIME_TYPES.put(ext, mimeType);
        }
    }

    public static void unregister(String extension) {
        if (extension != null) {
            String ext = extension.toLowerCase();
            if (ext.startsWith(".")) {
                ext = ext.substring(1);
            }
            MIME_TYPES.remove(ext);
        }
    }

    public static boolean isText(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("text/") ||
               mimeType.contains("json") ||
               mimeType.contains("xml") ||
               mimeType.contains("javascript");
    }

    public static boolean isImage(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("image/");
    }

    public static boolean isAudio(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("audio/");
    }

    public static boolean isVideo(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("video/");
    }

    public static boolean isBinary(String mimeType) {
        return !isText(mimeType);
    }

    public static boolean isCompressed(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.contains("zip") ||
               mimeType.contains("rar") ||
               mimeType.contains("7z") ||
               mimeType.contains("tar") ||
               mimeType.contains("gzip") ||
               mimeType.contains("compressed");
    }

    public static String getDefaultMimeType() {
        return DEFAULT_MIME_TYPE;
    }

    public static Map<String, String> getAllMimeTypes() {
        return new HashMap<>(MIME_TYPES);
    }
}
