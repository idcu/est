package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.EmailTemplate;

public class DefaultEmailTemplate implements EmailTemplate {
    
    private final String name;
    private final String subject;
    private final String content;
    private final boolean html;
    private final long createdAt;
    private final long updatedAt;
    
    public DefaultEmailTemplate(String name, String subject, String content, boolean html) {
        this(name, subject, content, html, System.currentTimeMillis(), System.currentTimeMillis());
    }
    
    public DefaultEmailTemplate(String name, String subject, String content, boolean html, long createdAt, long updatedAt) {
        this.name = name;
        this.subject = subject;
        this.content = content;
        this.html = html;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getSubject() {
        return subject;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public boolean isHtml() {
        return html;
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
