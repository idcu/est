package ltd.idcu.est.web;

import ltd.idcu.est.web.api.View;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DefaultView implements View {

    private String name;
    private String template;
    private Map<String, Object> model;
    private String contentType = "text/html; charset=UTF-8";
    private String characterEncoding = "UTF-8";
    private ViewEngine viewEngine;

    public DefaultView() {
        this.model = new HashMap<>();
    }

    public DefaultView(String name) {
        this();
        this.name = name;
    }

    public DefaultView(String name, String template) {
        this(name);
        this.template = template;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public Map<String, Object> getModel() {
        return model;
    }

    @Override
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    @Override
    public String render() {
        if (viewEngine != null && template != null) {
            return viewEngine.render(template, model);
        }
        return "";
    }

    @Override
    public void render(OutputStream outputStream) {
        try {
            String rendered = render();
            outputStream.write(rendered.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to render view to output stream", e);
        }
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String encoding) {
        this.characterEncoding = encoding;
    }

    public ViewEngine getViewEngine() {
        return viewEngine;
    }

    public void setViewEngine(ViewEngine viewEngine) {
        this.viewEngine = viewEngine;
    }
}
