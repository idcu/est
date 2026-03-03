package ltd.idcu.est.web.api;

import java.io.OutputStream;
import java.util.Map;

public interface View {

    String getName();

    void setName(String name);

    String getTemplate();

    void setTemplate(String template);

    Map<String, Object> getModel();

    void setModel(Map<String, Object> model);

    default void addAttribute(String name, Object value) {
        getModel().put(name, value);
    }

    default Object getAttribute(String name) {
        return getModel().get(name);
    }

    default boolean hasAttribute(String name) {
        return getModel().containsKey(name);
    }

    String render();

    void render(OutputStream outputStream);

    String getContentType();

    void setContentType(String contentType);

    String getCharacterEncoding();

    void setCharacterEncoding(String encoding);

    interface ViewResolver {
        View resolve(String viewName);
        boolean exists(String viewName);
        String resolveTemplate(String viewName);
    }

    interface ViewEngine {
        String render(String template, Map<String, Object> model);
        void render(String template, Map<String, Object> model, OutputStream outputStream);
        String getName();
        String[] getExtensions();
    }
}
