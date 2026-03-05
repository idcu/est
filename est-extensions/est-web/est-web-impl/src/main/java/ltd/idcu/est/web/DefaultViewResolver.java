package ltd.idcu.est.web;

import ltd.idcu.est.web.api.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultViewResolver implements View.ViewResolver {

    private final String templatePath;
    private final String templatePrefix;
    private final String templateSuffix;
    private final View.ViewEngine viewEngine;
    private final Map<String, String> templateCache;
    private boolean cacheEnabled = true;
    private final ClassLoader classLoader;

    public DefaultViewResolver(View.ViewEngine viewEngine) {
        this(viewEngine, "templates", "", ".html");
    }

    public DefaultViewResolver(View.ViewEngine viewEngine, String templatePath) {
        this(viewEngine, templatePath, "", ".html");
    }

    public DefaultViewResolver(View.ViewEngine viewEngine, String templatePath, String templatePrefix, String templateSuffix) {
        this.templatePath = templatePath;
        this.templatePrefix = templatePrefix;
        this.templateSuffix = templateSuffix;
        this.viewEngine = viewEngine;
        this.templateCache = new HashMap<>();
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public View resolve(String viewName) {
        DefaultView view = new DefaultView(viewName);
        view.setViewEngine(viewEngine);
        
        if (exists(viewName)) {
            String template = resolveTemplate(viewName);
            view.setTemplate(template);
        }
        
        return view;
    }

    @Override
    public boolean exists(String viewName) {
        return resolveTemplate(viewName) != null;
    }

    @Override
    public String resolveTemplate(String viewName) {
        String cacheKey = viewName;
        
        if (cacheEnabled && templateCache.containsKey(cacheKey)) {
            return templateCache.get(cacheKey);
        }

        String template = loadTemplate(viewName);
        
        if (template != null && cacheEnabled) {
            templateCache.put(cacheKey, template);
        }

        return template;
    }

    private String loadTemplate(String viewName) {
        String resourcePath = templatePath + "/" + templatePrefix + viewName + templateSuffix;
        
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        if (inputStream != null) {
            return readInputStream(inputStream);
        }

        File file = new File(resourcePath);
        if (file.exists()) {
            try {
                return Files.readString(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }

    private String readInputStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        if (!cacheEnabled) {
            templateCache.clear();
        }
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void clearCache() {
        templateCache.clear();
    }
}
