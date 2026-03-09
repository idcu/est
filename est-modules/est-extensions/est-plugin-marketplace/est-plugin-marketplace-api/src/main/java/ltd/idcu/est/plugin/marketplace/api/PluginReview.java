package ltd.idcu.est.plugin.marketplace.api;

public final class PluginReview {
    
    private final String id;
    private final String pluginId;
    private final String userId;
    private final String userName;
    private final int rating;
    private final String title;
    private final String content;
    private final long createdAt;
    private final long updatedAt;
    private final int helpfulCount;
    
    public PluginReview(String id, String pluginId, String userId, String userName,
                       int rating, String title, String content,
                       long createdAt, long updatedAt, int helpfulCount) {
        this.id = id;
        this.pluginId = pluginId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.helpfulCount = helpfulCount;
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
    
    public String getUserName() {
        return userName;
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
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public int getHelpfulCount() {
        return helpfulCount;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String pluginId;
        private String userId;
        private String userName;
        private int rating;
        private String title;
        private String content;
        private long createdAt;
        private long updatedAt;
        private int helpfulCount;
        
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
        
        public Builder userName(String userName) {
            this.userName = userName;
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
        
        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Builder helpfulCount(int helpfulCount) {
            this.helpfulCount = helpfulCount;
            return this;
        }
        
        public PluginReview build() {
            return new PluginReview(id, pluginId, userId, userName, rating,
                                   title, content, createdAt, updatedAt, helpfulCount);
        }
    }
}
