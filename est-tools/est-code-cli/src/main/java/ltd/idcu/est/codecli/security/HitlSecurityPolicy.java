package ltd.idcu.est.codecli.security;

import java.util.*;
import java.util.regex.Pattern;

public class HitlSecurityPolicy {
    
    private final Set<String> allowedToolsRequiringApproval;
    private final Set<String> pathPatternsRequiringApproval;
    private final Set<String> sensitiveFilePatterns;
    private final Map<String, Pattern> compiledPatterns;
    
    public HitlSecurityPolicy() {
        this.allowedToolsRequiringApproval = new HashSet<>();
        this.pathPatternsRequiringApproval = new HashSet<>();
        this.sensitiveFilePatterns = new HashSet<>();
        this.compiledPatterns = new HashMap<>();
        
        allowedToolsRequiringApproval.add("est_write_file");
        allowedToolsRequiringApproval.add("est_scaffold");
        allowedToolsRequiringApproval.add("est_codegen");
        
        sensitiveFilePatterns.add(".*\\.key$");
        sensitiveFilePatterns.add(".*\\.pem$");
        sensitiveFilePatterns.add(".*\\.env$");
        sensitiveFilePatterns.add(".*\\.properties$");
        sensitiveFilePatterns.add(".*\\.yml$");
        sensitiveFilePatterns.add(".*\\.yaml$");
        sensitiveFilePatterns.add(".*id_rsa.*");
        sensitiveFilePatterns.add(".*id_dsa.*");
        sensitiveFilePatterns.add(".*id_ecdsa.*");
        sensitiveFilePatterns.add(".*id_ed25519.*");
        
        compilePatterns();
    }
    
    private void compilePatterns() {
        for (String pattern : pathPatternsRequiringApproval) {
            compiledPatterns.put(pattern, Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        }
        for (String pattern : sensitiveFilePatterns) {
            compiledPatterns.put(pattern, Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        }
    }
    
    public boolean requiresApproval(String toolName, Map<String, Object> args) {
        if (allowedToolsRequiringApproval.contains(toolName)) {
            return true;
        }
        
        if ("est_write_file".equals(toolName) || "est_read_file".equals(toolName)) {
            String path = (String) args.get("path");
            if (path != null) {
                return isSensitiveFile(path) || matchesPathPattern(path);
            }
        }
        
        return false;
    }
    
    private boolean isSensitiveFile(String path) {
        String lowerPath = path.toLowerCase();
        for (String pattern : sensitiveFilePatterns) {
            Pattern compiled = compiledPatterns.get(pattern);
            if (compiled.matcher(lowerPath).matches()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean matchesPathPattern(String path) {
        String lowerPath = path.toLowerCase();
        for (String pattern : pathPatternsRequiringApproval) {
            Pattern compiled = compiledPatterns.get(pattern);
            if (compiled.matcher(lowerPath).matches()) {
                return true;
            }
        }
        return false;
    }
    
    public void addToolRequiringApproval(String toolName) {
        allowedToolsRequiringApproval.add(toolName);
    }
    
    public void removeToolRequiringApproval(String toolName) {
        allowedToolsRequiringApproval.remove(toolName);
    }
    
    public void addPathPattern(String pattern) {
        pathPatternsRequiringApproval.add(pattern);
        compiledPatterns.put(pattern, Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
    }
    
    public void removePathPattern(String pattern) {
        pathPatternsRequiringApproval.remove(pattern);
        compiledPatterns.remove(pattern);
    }
    
    public void addSensitiveFilePattern(String pattern) {
        sensitiveFilePatterns.add(pattern);
        compiledPatterns.put(pattern, Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
    }
    
    public void removeSensitiveFilePattern(String pattern) {
        sensitiveFilePatterns.remove(pattern);
        compiledPatterns.remove(pattern);
    }
    
    public Set<String> getToolsRequiringApproval() {
        return new HashSet<>(allowedToolsRequiringApproval);
    }
    
    public Set<String> getPathPatterns() {
        return new HashSet<>(pathPatternsRequiringApproval);
    }
    
    public Set<String> getSensitiveFilePatterns() {
        return new HashSet<>(sensitiveFilePatterns);
    }
}
