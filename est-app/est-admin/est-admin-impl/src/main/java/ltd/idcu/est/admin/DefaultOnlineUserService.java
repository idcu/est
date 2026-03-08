package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.OnlineUser;
import ltd.idcu.est.admin.api.OnlineUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultOnlineUserService implements OnlineUserService {
    
    private final Map<String, DefaultOnlineUser> onlineUsers = new ConcurrentHashMap<>();
    
    @Override
    public List<OnlineUser> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.values());
    }
    
    @Override
    public void forceLogout(String sessionId) {
        onlineUsers.remove(sessionId);
    }
    
    @Override
    public int getOnlineUserCount() {
        return onlineUsers.size();
    }
    
    public String addOnlineUser(String userId, String username, String ip, String browser) {
        String sessionId = UUID.randomUUID().toString();
        DefaultOnlineUser user = new DefaultOnlineUser(sessionId, userId, username, ip, browser);
        onlineUsers.put(sessionId, user);
        return sessionId;
    }
    
    public void updateUserActivity(String sessionId) {
        DefaultOnlineUser user = onlineUsers.get(sessionId);
        if (user != null) {
            user.updateLastActivityTime();
        }
    }
    
    public void removeOnlineUser(String sessionId) {
        onlineUsers.remove(sessionId);
    }
}
