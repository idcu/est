package ltd.idcu.est.web.api;

import java.util.List;
import java.util.Map;

public interface FormData {

    String getParameter(String name);

    List<String> getParameterValues(String name);

    Map<String, List<String>> getParameters();

    boolean hasParameter(String name);

    MultipartFile getFile(String name);

    List<MultipartFile> getFiles(String name);

    Map<String, List<MultipartFile>> getFiles();

    boolean hasFile(String name);

    boolean isMultipart();
}
