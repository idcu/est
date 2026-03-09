package ltd.idcu.est.codecli.plugin;

public class PluginException extends Exception {
    
    public PluginException(String message) {
        super(message);
    }
    
    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
