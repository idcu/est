package ltd.idcu.est.admin.api;

import java.util.List;

public interface OnlineUserService {
    
    List<OnlineUser> getOnlineUsers();
    
    void forceLogout(String sessionId);
    
    int getOnlineUserCount();
}
