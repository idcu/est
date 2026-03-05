package ltd.idcu.est.web.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface Response {

    HttpStatus getStatus();

    void setStatus(HttpStatus status);

    void setStatus(int code);

    String getHeader(String name);

    void setHeader(String name, String value);

    void addHeader(String name, String value);

    void removeHeader(String name);

    Map<String, String> getHeaders();

    String getContentType();

    void setContentType(String contentType);

    long getContentLength();

    void setContentLength(long length);

    String getBody();

    void setBody(String body);

    void setBody(byte[] body);

    void setBody(InputStream inputStream);

    String getCharacterEncoding();

    void setCharacterEncoding(String encoding);

    void json(Object object);

    void json(String json);

    void xml(String xml);

    void html(String html);

    void text(String text);

    void redirect(String url);

    void redirect(String url, HttpStatus status);

    void sendError(HttpStatus status);

    void sendError(HttpStatus status, String message);

    void sendError(int code);

    void sendError(int code, String message);

    OutputStream getOutputStream();

    boolean isCommitted();

    void flush();

    void reset();

    void setCookie(String name, String value);

    void setCookie(String name, String value, int maxAge);

    void setCookie(String name, String value, int maxAge, String path, String domain, boolean secure, boolean httpOnly);

    void removeCookie(String name);

    default void ok() {
        setStatus(HttpStatus.OK);
    }

    default void created() {
        setStatus(HttpStatus.CREATED);
    }

    default void noContent() {
        setStatus(HttpStatus.NO_CONTENT);
    }

    default void badRequest() {
        setStatus(HttpStatus.BAD_REQUEST);
    }

    default void unauthorized() {
        setStatus(HttpStatus.UNAUTHORIZED);
    }

    default void forbidden() {
        setStatus(HttpStatus.FORBIDDEN);
    }

    default void notFound() {
        setStatus(HttpStatus.NOT_FOUND);
    }

    default void internalServerError() {
        setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    void render(View view);

    void render(String viewName);

    void render(String viewName, Map<String, Object> model);
}
