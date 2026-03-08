package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.SmsTemplate;

public class DefaultSmsTemplate implements SmsTemplate {
    
    private final String code;
    private final String name;
    private final String content;
    private final String provider;
    private final long createdAt;
    private final long updatedAt;
    
    public DefaultSmsTemplate(String code, String name, String content, String provider) {
        this(code, name, content, provider, System.currentTimeMillis(), System.currentTimeMillis());
    }
    
    public DefaultSmsTemplate(String code, String name, String content, String provider, long createdAt, long updatedAt) {
        this.code = code;
        this.name = name;
        this.content = content;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public String getProvider() {
        return provider;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public long getUpdatedAt() {
        return updatedAt;
    }
}
