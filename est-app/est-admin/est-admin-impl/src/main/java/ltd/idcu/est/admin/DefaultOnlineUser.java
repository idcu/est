package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.OnlineUser;

public class DefaultOnlineUser implements OnlineUser {
    
    private final String sessionId;
    private final String userId;
    private final String username;
    private final String ip;
    private final String browser;
    private final long loginTime;
    private long lastActivityTime;
    
    public DefaultOnlineUser(String sessionId, String userId, String username, String ip, String browser) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.ip = ip;
        this.browser = browser;
        this.loginTime = System.currentTimeMillis();
        this.lastActivityTime = this.loginTime;
    }
    
    @Override
    public String getSessionId() {
        return sessionId;
    }
    
    @Override
    public String getUserId() {
        return userId;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public String getIp() {
        return ip;
    }
    
    @Override
    public String getBrowser() {
        return browser;
    }
    
    @Override
    public long getLoginTime() {
        return loginTime;
    }
    
    @Override
    public long getLastActivityTime() {
        return lastActivityTime;
    }
    
    public void updateLastActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }
}
