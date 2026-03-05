package ltd.idcu.est.scaffold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final Pattern IF_PATTERN = Pattern.compile("\\$\\{if\\s+([^}]+)\\}([\\s\\S]*?)\\$\\{endif\\}");
    
    private final Map<String, String> variables;
    private final ClassLoader classLoader;

    public TemplateEngine() {
        this.variables = new HashMap<>();
        this.classLoader = getClass().getClassLoader();
    }

    public TemplateEngine setVariable(String name, String value) {
        variables.put(name, value);
        return this;
    }

    public TemplateEngine setVariables(Map<String, String> vars) {
        variables.putAll(vars);
        return this;
    }

    public String loadTemplate(String templatePath) throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(templatePath);
        if (inputStream == null) {
            throw new IOException("Template not found: " + templatePath);
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public String render(String template) {
        String result = template;
        
        result = processConditionals(result);
        result = processVariables(result);
        
        return result;
    }

    public String renderFromFile(String templatePath) throws IOException {
        String template = loadTemplate(templatePath);
        return render(template);
    }

    private String processVariables(String template) {
        StringBuffer result = new StringBuffer();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        
        while (matcher.find()) {
            String varName = matcher.group(1);
            String replacement = variables.getOrDefault(varName, "");
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        
        return result.toString();
    }

    private String processConditionals(String template) {
        StringBuffer result = new StringBuffer();
        Matcher matcher = IF_PATTERN.matcher(template);
        
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String content = matcher.group(2);
            
            boolean conditionMet = evaluateCondition(condition);
            String replacement = conditionMet ? content : "";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        
        return result.toString();
    }

    private boolean evaluateCondition(String condition) {
        if (condition.startsWith("!")) {
            return !evaluateCondition(condition.substring(1));
        }
        
        String value = variables.get(condition);
        return value != null && !value.isEmpty() && !"false".equalsIgnoreCase(value);
    }

    public static TemplateEngine fromConfig(ProjectConfig config) {
        TemplateEngine engine = new TemplateEngine();
        engine.setVariable("groupId", config.getGroupId());
        engine.setVariable("artifactId", config.getArtifactId());
        engine.setVariable("version", config.getVersion());
        engine.setVariable("packageName", config.getPackageName());
        engine.setVariable("fullPackageName", config.getFullPackageName());
        engine.setVariable("packagePath", config.getPackagePath());
        engine.setVariable("javaVersion", String.valueOf(config.getJavaVersion()));
        engine.setVariable("estVersion", config.getEstVersion());
        engine.setVariable("description", config.getDescription() != null ? config.getDescription() : "");
        return engine;
    }
}
