package ltd.idcu.est.web.api;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface Request {

    HttpMethod getMethod();

    String getPath();

    String getQueryString();

    String getParameter(String name);

    List<String> getParameterValues(String name);

    Map<String, List<String>> getParameters();

    String getHeader(String name);

    List<String> getHeaders(String name);

    Map<String, List<String>> getHeaders();

    String getContentType();

    String getBody();

    InputStream getBodyStream();

    byte[] getBodyBytes();

    Session getSession();

    Session getSession(boolean create);

    String getRemoteAddress();

    String getRemoteHost();

    int getRemotePort();

    String getProtocol();

    String getScheme();

    String getHost();

    int getPort();

    URI getURI();

    String getUrl();

    String getFullUrl();

    List<String> getPathSegments();

    String getPathSegment(int index);

    String getCookie(String name);

    Map<String, String> getCookies();

    boolean isSecure();

    boolean isAjax();

    boolean isMultipart();

    default String getParameterOrDefault(String name, String defaultValue) {
        String value = getParameter(name);
        return value != null ? value : defaultValue;
    }

    default int getIntParameter(String name, int defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    default long getLongParameter(String name, long defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    default boolean getBooleanParameter(String name, boolean defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    default double getDoubleParameter(String name, double defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    default String getHeaderOrDefault(String name, String defaultValue) {
        String value = getHeader(name);
        return value != null ? value : defaultValue;
    }

    default boolean hasParameter(String name) {
        return getParameter(name) != null;
    }

    default boolean hasHeader(String name) {
        return getHeader(name) != null;
    }

    String getPathVariable(String name);

    void addPathVariables(Map<String, String> variables);

    FormData getFormData();

    MultipartFile getFile(String name);

    List<MultipartFile> getFiles(String name);

    AsyncContext startAsync();

    boolean isAsyncStarted();

    AsyncContext getAsyncContext();
}
