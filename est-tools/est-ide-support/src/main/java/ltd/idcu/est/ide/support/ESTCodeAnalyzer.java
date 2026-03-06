package ltd.idcu.est.ide.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESTCodeAnalyzer {
    private final Map<String, List<Diagnostic>> diagnosticsCache = new HashMap<>();
    private final Map<String, List<CodeCompletionItem>> completionCache = new HashMap<>();

    public List<Diagnostic> analyze(String filePath, String content) {
        List<Diagnostic> diagnostics = new ArrayList<>();
        FileType fileType = FileType.fromFileName(filePath);

        if (fileType == null) {
            return diagnostics;
        }

        switch (fileType) {
            case JAVA:
                diagnostics.addAll(analyzeJavaFile(content));
                break;
            case POM_XML:
                diagnostics.addAll(analyzePomXml(content));
                break;
            case YAML:
            case YML:
                diagnostics.addAll(analyzeYamlFile(filePath, content));
                break;
            default:
                break;
        }

        diagnosticsCache.put(filePath, diagnostics);
        return diagnostics;
    }

    private List<Diagnostic> analyzeJavaFile(String content) {
        List<Diagnostic> diagnostics = new ArrayList<>();
        String[] lines = content.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            if (line.contains("@RestController") && !line.contains("import")) {
                if (!content.contains("import ltd.idcu.est.web.api.RestController")) {
                    diagnostics.add(new Diagnostic(
                            "Missing import for @RestController",
                            Diagnostic.Severity.WARNING,
                            i + 1,
                            line.indexOf("@RestController") + 1
                    ));
                }
            }

            if (line.contains("@Controller") && !line.contains("import")) {
                if (!content.contains("import ltd.idcu.est.web.api.Controller")) {
                    diagnostics.add(new Diagnostic(
                            "Missing import for @Controller",
                            Diagnostic.Severity.WARNING,
                            i + 1,
                            line.indexOf("@Controller") + 1
                    ));
                }
            }

            if (line.contains("@Component") && !line.contains("import")) {
                if (!content.contains("import ltd.idcu.est.core.api.annotation.Component")) {
                    diagnostics.add(new Diagnostic(
                            "Missing import for @Component",
                            Diagnostic.Severity.WARNING,
                            i + 1,
                            line.indexOf("@Component") + 1
                    ));
                }
            }

            if (line.trim().startsWith("// TODO") || line.trim().startsWith("/* TODO")) {
                diagnostics.add(new Diagnostic(
                        "TODO comment found",
                        Diagnostic.Severity.INFO,
                        i + 1,
                        1
                ));
            }

            if (line.trim().startsWith("// FIXME") || line.trim().startsWith("/* FIXME")) {
                diagnostics.add(new Diagnostic(
                        "FIXME comment found",
                        Diagnostic.Severity.HINT,
                        i + 1,
                        1
                ));
            }
        }

        return diagnostics;
    }

    private List<Diagnostic> analyzePomXml(String content) {
        List<Diagnostic> diagnostics = new ArrayList<>();
        
        if (!content.contains("<artifactId>est-core-api</artifactId>")) {
            diagnostics.add(new Diagnostic(
                    "Consider adding est-core-api dependency for EST framework core features",
                    Diagnostic.Severity.INFO,
                    1,
                    1
            ));
        }

        if (!content.contains("<artifactId>est-web-impl</artifactId>") && content.contains("web")) {
            diagnostics.add(new Diagnostic(
                    "Consider adding est-web-impl dependency for web features",
                    Diagnostic.Severity.INFO,
                    1,
                    1
            ));
        }

        return diagnostics;
    }

    private List<Diagnostic> analyzeYamlFile(String filePath, String content) {
        List<Diagnostic> diagnostics = new ArrayList<>();
        
        if (filePath.contains("application") || filePath.contains("est")) {
            if (!content.contains("server:")) {
                diagnostics.add(new Diagnostic(
                        "Consider adding server configuration",
                        Diagnostic.Severity.INFO,
                        1,
                        1
                ));
            }
        }

        return diagnostics;
    }

    public List<CodeCompletionItem> getCompletions(String filePath, int line, int column, String content) {
        List<CodeCompletionItem> completions = new ArrayList<>();
        FileType fileType = FileType.fromFileName(filePath);

        if (fileType == null) {
            return completions;
        }

        switch (fileType) {
            case JAVA:
                completions.addAll(getJavaCompletions(content, line, column));
                break;
            case YAML:
            case YML:
                completions.addAll(getYamlCompletions(content, line, column));
                break;
            case PROPERTIES:
                completions.addAll(getPropertiesCompletions(content, line, column));
                break;
            default:
                break;
        }

        return completions;
    }

    private List<CodeCompletionItem> getJavaCompletions(String content, int line, int column) {
        List<CodeCompletionItem> completions = new ArrayList<>();

        completions.add(new CodeCompletionItem(
                "@Component",
                "@Component",
                "Mark a class as a component for dependency injection",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                1
        ));

        completions.add(new CodeCompletionItem(
                "@Controller",
                "@Controller",
                "Mark a class as a web controller",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                2
        ));

        completions.add(new CodeCompletionItem(
                "@RestController",
                "@RestController",
                "Mark a class as a REST API controller",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                3
        ));

        completions.add(new CodeCompletionItem(
                "@Inject",
                "@Inject",
                "Inject a dependency",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                4
        ));

        completions.add(new CodeCompletionItem(
                "@Singleton",
                "@Singleton",
                "Mark a class as a singleton",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                5
        ));

        completions.add(new CodeCompletionItem(
                "@Transactional",
                "@Transactional",
                "Mark a method as transactional",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                6
        ));

        completions.add(new CodeCompletionItem(
                "@Aspect",
                "@Aspect",
                "Mark a class as an AOP aspect",
                CodeCompletionItem.CompletionKind.ANNOTATION,
                7
        ));

        completions.add(new CodeCompletionItem(
                "EST Web Controller Template",
                "public class ${NAME}Controller implements Controller {\n    @Override\n    public String index() {\n        return \"home\";\n    }\n}",
                "Create a new EST web controller",
                CodeCompletionItem.CompletionKind.TEMPLATE,
                100
        ));

        return completions;
    }

    private List<CodeCompletionItem> getYamlCompletions(String content, int line, int column) {
        List<CodeCompletionItem> completions = new ArrayList<>();

        completions.add(new CodeCompletionItem(
                "server:",
                "server:\n  port: 8080\n  host: 0.0.0.0",
                "EST server configuration",
                CodeCompletionItem.CompletionKind.PROPERTY,
                1
        ));

        completions.add(new CodeCompletionItem(
                "logging:",
                "logging:\n  level: INFO\n  file: logs/application.log",
                "Logging configuration",
                CodeCompletionItem.CompletionKind.PROPERTY,
                2
        ));

        completions.add(new CodeCompletionItem(
                "database:",
                "database:\n  url: jdbc:mysql://localhost:3306/db\n  username: root\n  password: password",
                "Database configuration",
                CodeCompletionItem.CompletionKind.PROPERTY,
                3
        ));

        return completions;
    }

    private List<CodeCompletionItem> getPropertiesCompletions(String content, int line, int column) {
        List<CodeCompletionItem> completions = new ArrayList<>();

        completions.add(new CodeCompletionItem(
                "server.port",
                "server.port=8080",
                "Server port",
                CodeCompletionItem.CompletionKind.PROPERTY,
                1
        ));

        completions.add(new CodeCompletionItem(
                "server.host",
                "server.host=0.0.0.0",
                "Server host",
                CodeCompletionItem.CompletionKind.PROPERTY,
                2
        ));

        completions.add(new CodeCompletionItem(
                "logging.level",
                "logging.level=INFO",
                "Logging level",
                CodeCompletionItem.CompletionKind.PROPERTY,
                3
        ));

        return completions;
    }

    public void clearCache(String filePath) {
        diagnosticsCache.remove(filePath);
        completionCache.remove(filePath);
    }

    public void clearAllCaches() {
        diagnosticsCache.clear();
        completionCache.clear();
    }
}
