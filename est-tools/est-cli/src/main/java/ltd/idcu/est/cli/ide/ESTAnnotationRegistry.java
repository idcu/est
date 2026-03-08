package ltd.idcu.est.cli.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESTAnnotationRegistry {
    private static final Map<String, AnnotationInfo> annotations = new HashMap<>();

    static {
        registerCoreAnnotations();
        registerWebAnnotations();
        registerDataAnnotations();
        registerFeatureAnnotations();
    }

    private static void registerCoreAnnotations() {
        annotations.put("Component", new AnnotationInfo(
                "Component",
                "ltd.idcu.est.core.api.annotation.Component",
                "Mark a class as a component for dependency injection"
        ).addAttribute("value", "String", "", false, "Component name"));

        annotations.put("Singleton", new AnnotationInfo(
                "Singleton",
                "ltd.idcu.est.core.api.annotation.Singleton",
                "Mark a class as a singleton"
        ));

        annotations.put("Inject", new AnnotationInfo(
                "Inject",
                "ltd.idcu.est.core.api.annotation.Inject",
                "Inject a dependency"
        ).addAttribute("value", "String", "", false, "Qualifier name"));

        annotations.put("Configuration", new AnnotationInfo(
                "Configuration",
                "ltd.idcu.est.core.api.annotation.Configuration",
                "Mark a class as a configuration class"
        ));

        annotations.put("Value", new AnnotationInfo(
                "Value",
                "ltd.idcu.est.core.api.annotation.Value",
                "Inject a configuration value"
        ).addAttribute("value", "String", "", true, "Configuration property key"));
    }

    private static void registerWebAnnotations() {
        annotations.put("Controller", new AnnotationInfo(
                "Controller",
                "ltd.idcu.est.web.api.Controller",
                "Mark a class as a web controller"
        ));

        annotations.put("RestController", new AnnotationInfo(
                "RestController",
                "ltd.idcu.est.web.api.RestController",
                "Mark a class as a REST API controller"
        ));

        annotations.put("GetMapping", new AnnotationInfo(
                "GetMapping",
                "ltd.idcu.est.web.api.GetMapping",
                "Map HTTP GET requests to a method"
        ).addAttribute("path", "String", "/", false, "Request path"));

        annotations.put("PostMapping", new AnnotationInfo(
                "PostMapping",
                "ltd.idcu.est.web.api.PostMapping",
                "Map HTTP POST requests to a method"
        ).addAttribute("path", "String", "/", false, "Request path"));

        annotations.put("PutMapping", new AnnotationInfo(
                "PutMapping",
                "ltd.idcu.est.web.api.PutMapping",
                "Map HTTP PUT requests to a method"
        ).addAttribute("path", "String", "/", false, "Request path"));

        annotations.put("DeleteMapping", new AnnotationInfo(
                "DeleteMapping",
                "ltd.idcu.est.web.api.DeleteMapping",
                "Map HTTP DELETE requests to a method"
        ).addAttribute("path", "String", "/", false, "Request path"));

        annotations.put("PathVariable", new AnnotationInfo(
                "PathVariable",
                "ltd.idcu.est.web.api.PathVariable",
                "Bind a method parameter to a URI template variable"
        ).addAttribute("value", "String", "", false, "Variable name"));

        annotations.put("RequestParam", new AnnotationInfo(
                "RequestParam",
                "ltd.idcu.est.web.api.RequestParam",
                "Bind a method parameter to a web request parameter"
        ).addAttribute("value", "String", "", false, "Parameter name")
         .addAttribute("required", "boolean", "true", false, "Whether the parameter is required"));

        annotations.put("RequestBody", new AnnotationInfo(
                "RequestBody",
                "ltd.idcu.est.web.api.RequestBody",
                "Bind a method parameter to the body of the web request"
        ));
    }

    private static void registerDataAnnotations() {
        annotations.put("Repository", new AnnotationInfo(
                "Repository",
                "ltd.idcu.est.data.api.Repository",
                "Mark a class as a data repository"
        ));

        annotations.put("Entity", new AnnotationInfo(
                "Entity",
                "ltd.idcu.est.data.api.Entity",
                "Mark a class as a database entity"
        ).addAttribute("table", "String", "", false, "Database table name"));

        annotations.put("Id", new AnnotationInfo(
                "Id",
                "ltd.idcu.est.data.api.Id",
                "Mark a field as the primary key"
        ));

        annotations.put("Column", new AnnotationInfo(
                "Column",
                "ltd.idcu.est.data.api.Column",
                "Map a field to a database column"
        ).addAttribute("name", "String", "", false, "Column name")
         .addAttribute("nullable", "boolean", "true", false, "Whether the column is nullable"));

        annotations.put("Transactional", new AnnotationInfo(
                "Transactional",
                "ltd.idcu.est.data.api.Transactional",
                "Mark a method as transactional"
        ).addAttribute("readOnly", "boolean", "false", false, "Whether the transaction is read-only"));
    }

    private static void registerFeatureAnnotations() {
        annotations.put("Cacheable", new AnnotationInfo(
                "Cacheable",
                "ltd.idcu.est.cache.api.Cacheable",
                "Cache the result of a method invocation"
        ).addAttribute("key", "String", "", false, "Cache key")
         .addAttribute("ttl", "long", "3600", false, "Time to live in seconds"));

        annotations.put("Scheduled", new AnnotationInfo(
                "Scheduled",
                "ltd.idcu.est.scheduler.api.Scheduled",
                "Mark a method to be scheduled"
        ).addAttribute("cron", "String", "", false, "Cron expression")
         .addAttribute("fixedRate", "long", "-1", false, "Fixed rate in milliseconds"));

        annotations.put("EventListener", new AnnotationInfo(
                "EventListener",
                "ltd.idcu.est.event.api.EventListener",
                "Mark a method as an event listener"
        ).addAttribute("async", "boolean", "false", false, "Whether to execute asynchronously"));

        annotations.put("Aspect", new AnnotationInfo(
                "Aspect",
                "ltd.idcu.est.patterns.api.Aspect",
                "Mark a class as an AOP aspect"
        ));

        annotations.put("Before", new AnnotationInfo(
                "Before",
                "ltd.idcu.est.patterns.api.Before",
                "Execute advice before the method"
        ).addAttribute("pointcut", "String", "", true, "Pointcut expression"));

        annotations.put("After", new AnnotationInfo(
                "After",
                "ltd.idcu.est.patterns.api.After",
                "Execute advice after the method"
        ).addAttribute("pointcut", "String", "", true, "Pointcut expression"));

        annotations.put("Around", new AnnotationInfo(
                "Around",
                "ltd.idcu.est.patterns.api.Around",
                "Execute advice around the method"
        ).addAttribute("pointcut", "String", "", true, "Pointcut expression"));
    }

    public static AnnotationInfo getAnnotation(String name) {
        return annotations.get(name);
    }

    public static List<AnnotationInfo> getAllAnnotations() {
        return new ArrayList<>(annotations.values());
    }

    public static List<String> getAnnotationNames() {
        return new ArrayList<>(annotations.keySet());
    }
}
