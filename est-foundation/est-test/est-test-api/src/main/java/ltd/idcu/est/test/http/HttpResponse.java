package ltd.idcu.est.test.http;

import java.util.List;
import java.util.Map;

public interface HttpResponse {

    int getStatusCode();

    String getStatusMessage();

    String getBody();

    byte[] getBodyAsBytes();

    String getHeader(String name);

    List<String> getHeaders(String name);

    Map<String, List<String>> getAllHeaders();

    String getContentType();

    long getContentLength();

    boolean isSuccess();

    boolean isRedirect();

    boolean isClientError();

    boolean isServerError();
}
