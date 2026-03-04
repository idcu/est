package ltd.idcu.est.web;

import ltd.idcu.est.web.api.View;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTemplateEngine implements View.ViewEngine {

    private static final String NAME = "string-template";
    private static final String[] EXTENSIONS = {"html", "htm", "txt"};
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    @Override
    public String render(String template, Map<String, Object> model) {
        if (template == null || model == null) {
            return template;
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);

        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = model.get(key);
            String replacement = (value != null) ? value.toString() : "";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public void render(String template, Map<String, Object> model, OutputStream outputStream) {
        try {
            String rendered = render(template, model);
            outputStream.write(rendered.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to render template to output stream", e);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getExtensions() {
        return EXTENSIONS;
    }
}
