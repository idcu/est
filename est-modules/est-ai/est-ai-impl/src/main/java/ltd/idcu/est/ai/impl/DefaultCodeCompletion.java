package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.*;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultCodeCompletion implements CodeCompletion {
    
    private final List<CompletionSuggestion> builtInSnippets;
    
    public DefaultCodeCompletion() {
        this.builtInSnippets = initializeBuiltInSnippets();
    }
    
    private List<CompletionSuggestion> initializeBuiltInSnippets() {
        List<CompletionSuggestion> snippets = new ArrayList<>();
        
        snippets.add(new CompletionSuggestion(
            "est-web-app",
            """
            WebApplication app = Web.create("${1:MyApp}", "1.0.0");
            app.get("/", (req, res) -> res.send("Hello, EST!"));
            app.run(8080);
            """,
            "创建EST Web应用",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.95
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-controller",
            """
            @RestController
            @RequestMapping("/api/${1:users}")
            public class ${2:User}Controller {
                
                private final ${2:User}Service ${2:user}Service;
                
                @Inject
                public ${2:User}Controller(${2:User}Service ${2:user}Service) {
                    this.${2:user}Service = ${2:user}Service;
                }
                
                @GetMapping
                public List<${2:User}> list() {
                    return ${2:user}Service.findAll();
                }
            }
            """,
            "创建REST控制�?,
            CompletionSuggestion.CompletionType.SNIPPET,
            0.9
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-service",
            """
            public interface ${1:User}Service {
                ${1:User} findById(Long id);
                List<${1:User}> findAll();
                ${1:User} save(${1:User} ${1:user});
                void delete(Long id);
            }
            """,
            "创建服务接口",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.85
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-entity",
            """
            public class ${1:User} {
                private Long id;
                private String name;
                private String email;
                
                public Long getId() { return id; }
                public void setId(Long id) { this.id = id; }
                public String getName() { return name; }
                public void setName(String name) { this.name = name; }
                public String getEmail() { return email; }
                public void setEmail(String email) { this.email = email; }
            }
            """,
            "创建实体�?,
            CompletionSuggestion.CompletionType.SNIPPET,
            0.85
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-seq",
            """
            List<${1:Integer}> result = Seqs.of(1, 2, 3, 4, 5)
                .map(n -> n * 2)
                .filter(n -> n > 3)
                .toList();
            """,
            "使用EST集合操作",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.8
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-di",
            """
            Container container = new DefaultContainer();
            container.register(${1:Service}.class, ${1:ServiceImpl}.class);
            ${1:Service} service = container.get(${1:Service}.class);
            """,
            "使用依赖注入容器",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.8
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-config",
            """
            Config config = app.getConfig();
            config.set("${1:app.name}", "${2:MyApp}");
            String value = config.getString("${1:app.name}", "${3:Default}");
            """,
            "使用配置管理",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.75
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-route-get",
            """
            app.get("${1:/api/users}", (req, res) -> {
                res.json(${2:data});
            });
            """,
            "添加GET路由",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.9
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-route-post",
            """
            app.post("${1:/api/users}", (req, res) -> {
                ${2:Entity} entity = req.bodyAs(${2:Entity}.class);
                res.status(201).json(entity);
            });
            """,
            "添加POST路由",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.9
        ));
        
        snippets.add(new CompletionSuggestion(
            "est-try-catch",
            """
            try {
                ${1:// business logic}
            } catch (IllegalArgumentException e) {
                res.status(400).json(Map.of("error", e.getMessage()));
            } catch (Exception e) {
                res.status(500).json(Map.of("error", "Internal error"));
            }
            """,
            "EST错误处理模式",
            CompletionSuggestion.CompletionType.SNIPPET,
            0.7
        ));
        
        return snippets;
    }
    
    @Override
    public List<CompletionSuggestion> complete(String context, CompletionOptions options) {
        List<CompletionSuggestion> suggestions = new ArrayList<>();
        String lowerContext = context.toLowerCase();
        
        for (CompletionSuggestion snippet : builtInSnippets) {
            if (!options.getAllowedTypes().contains(snippet.getType())) {
                continue;
            }
            
            double confidence = calculateConfidence(snippet, lowerContext);
            if (confidence >= options.getMinConfidence()) {
                snippet.setConfidence(confidence);
                suggestions.add(snippet);
            }
        }
        
        suggestions.sort((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));
        
        if (suggestions.size() > options.getMaxSuggestions()) {
            suggestions = suggestions.subList(0, options.getMaxSuggestions());
        }
        
        return suggestions;
    }
    
    @Override
    public CompletionSuggestion getBestSuggestion(String context, CompletionOptions options) {
        List<CompletionSuggestion> suggestions = complete(context, options);
        return suggestions.isEmpty() ? null : suggestions.get(0);
    }
    
    private double calculateConfidence(CompletionSuggestion suggestion, String context) {
        double confidence = suggestion.getConfidence();
        
        String text = suggestion.getText().toLowerCase();
        String desc = suggestion.getDescription().toLowerCase();
        
        if (context.contains("web") && (text.contains("web") || desc.contains("web"))) {
            confidence += 0.1;
        }
        if (context.contains("controller") && (text.contains("controller") || desc.contains("controller"))) {
            confidence += 0.15;
        }
        if (context.contains("service") && (text.contains("service") || desc.contains("service"))) {
            confidence += 0.15;
        }
        if (context.contains("entity") && (text.contains("entity") || desc.contains("entity"))) {
            confidence += 0.15;
        }
        if (context.contains("route") && (text.contains("app.get") || text.contains("app.post"))) {
            confidence += 0.1;
        }
        if (context.contains("seq") && (text.contains("seqs") || desc.contains("集合"))) {
            confidence += 0.1;
        }
        
        return Math.min(confidence, 1.0);
    }
}
