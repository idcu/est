package ltd.idcu.est.admin.api;

import ltd.idcu.est.web.api.WebApplication;

public interface AdminApplication {
    
    void run(int port);
    
    void stop();
    
    boolean isRunning();
    
    String getName();
    
    void setName(String name);
    
    WebApplication getWebApplication();
    
    void registerController(Object controller);
}
