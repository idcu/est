package ltd.idcu.est.plugin.api.marketplace;

public class PluginReview {
    
    private final String id;
    private final String pluginId;
    private final String userId;
    private final String username;
    private final int rating;
    private final String title;
    private final String content;
    private final long createTime;
    private final long updateTime;
    private final int helpfulCount;
    private final boolean verified;
    
    public PluginReview(String id, String pluginId, String userId, String username, 
                       int rating, String title, String content, long createTime, 
                       long updateTime, int helpfulCount, boolean verified) {
        this.id = id;
        this.pluginId = pluginId;
        this.userId = userId;
        this.username = username;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.helpfulCount = helpfulCount;
        this.verified = verified;
    }
    
    public String getId() {
        return id;
    }
    
    public String getPluginId() {
        return pluginId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getRating() {
        return rating;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public int getHelpfulCount() {
        return helpfulCount;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String pluginId;
        private String userId;
        private String username;
        private int rating;
        private String title;
        private String content;
        private long createTime;
        private long updateTime;
        private int helpfulCount;
        private boolean verified;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder pluginId(String pluginId) {
            this.pluginId = pluginId;
            return this;
        }
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder content(String content) {
            this.content = content;
            return this;
        }
        
        public Builder createTime(long createTime) {
            this.createTime = createTime;
            return this;
        }
        
        public Builder updateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }
        
        public Builder helpfulCount(int helpfulCount) {
            this.helpfulCount = helpfulCount;
            return this;
        }
        
        public Builder verified(boolean verified) {
            this.verified = verified;
            return this;
        }
        
        public PluginReview build() {
            return new PluginReview(id, pluginId, userId, username, rating, 
                                   title, content, createTime, updateTime, 
                                   helpfulCount, verified);
        }
    }
}
