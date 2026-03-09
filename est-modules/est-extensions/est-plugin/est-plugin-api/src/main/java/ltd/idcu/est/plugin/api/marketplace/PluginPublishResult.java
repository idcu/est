package ltd.idcu.est.plugin.api.marketplace;

import java.util.ArrayList;
import java.util.List;

public class PluginPublishResult {
    
    private boolean success;
    private String pluginId;
    private String version;
    private String message;
    private List<String> warnings;
    private List<String> errors;
    private String downloadUrl;
    
    public PluginPublishResult() {
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getPluginId() {
        return pluginId;
    }
    
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
    
    public static Builder success(String pluginId, String version) {
        Builder builder = new Builder();
        builder.result.success = true;
        builder.result.pluginId = pluginId;
        builder.result.version = version;
        return builder;
    }
    
    public static Builder failure(String pluginId, String version) {
        Builder builder = new Builder();
        builder.result.success = false;
        builder.result.pluginId = pluginId;
        builder.result.version = version;
        return builder;
    }
    
    public static class Builder {
        private PluginPublishResult result = new PluginPublishResult();
        
        public Builder message(String message) {
            result.message = message;
            return this;
        }
        
        public Builder warning(String warning) {
            result.addWarning(warning);
            return this;
        }
        
        public Builder error(String error) {
            result.addError(error);
            return this;
        }
        
        public Builder downloadUrl(String downloadUrl) {
            result.downloadUrl = downloadUrl;
            return this;
        }
        
        public PluginPublishResult build() {
            return result;
        }
    }
}
