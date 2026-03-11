package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.EmailService;
import ltd.idcu.est.admin.api.EmailTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEmailService implements EmailService {
    
    private final Map<String, DefaultEmailTemplate> templates = new ConcurrentHashMap<>();
    
    public DefaultEmailService() {
        initializeDefaultTemplates();
    }
    
    private void initializeDefaultTemplates() {
        DefaultEmailTemplate welcomeTemplate = new DefaultEmailTemplate(
            "welcome",
            "Welcome",
            "<html><body><h1>Welcome {{username}}!</h1><p>Thank you for registering!</p></body></html>",
            true
        );
        templates.put("welcome", welcomeTemplate);
        
        DefaultEmailTemplate resetPasswordTemplate = new DefaultEmailTemplate(
            "reset-password",
            "Reset Password",
            "Your verification code is: {{code}}, please use it within 10 minutes.",
            false
        );
        templates.put("reset-password", resetPasswordTemplate);
    }
    
    @Override
    public void sendEmail(String to, String subject, String content) {
        System.out.println("[Email] Sending email to: " + to + ", subject: " + subject);
        System.out.println("[Email] Content: " + content);
    }
    
    @Override
    public void sendEmail(List<String> to, String subject, String content) {
        System.out.println("[Email] Sending email to: " + to + ", subject: " + subject);
        System.out.println("[Email] Content: " + content);
    }
    
    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        System.out.println("[Email] Sending HTML email to: " + to + ", subject: " + subject);
        System.out.println("[Email] HTML Content: " + htmlContent);
    }
    
    @Override
    public void sendHtmlEmail(List<String> to, String subject, String htmlContent) {
        System.out.println("[Email] Sending HTML email to: " + to + ", subject: " + subject);
        System.out.println("[Email] HTML Content: " + htmlContent);
    }
    
    @Override
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables) {
        EmailTemplate template = getEmailTemplate(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        
        String content = template.getContent();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            content = content.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
        }
        
        if (template.isHtml()) {
            sendHtmlEmail(to, subject, content);
        } else {
            sendEmail(to, subject, content);
        }
    }
    
    @Override
    public List<EmailTemplate> getEmailTemplates() {
        return new ArrayList<>(templates.values());
    }
    
    @Override
    public EmailTemplate getEmailTemplate(String name) {
        return templates.get(name);
    }
    
    @Override
    public void saveEmailTemplate(EmailTemplate template) {
        if (template instanceof DefaultEmailTemplate defaultTemplate) {
            templates.put(defaultTemplate.getName(), defaultTemplate);
        } else {
            DefaultEmailTemplate newTemplate = new DefaultEmailTemplate(
                template.getName(),
                template.getSubject(),
                template.getContent(),
                template.isHtml()
            );
            templates.put(newTemplate.getName(), newTemplate);
        }
    }
    
    @Override
    public void deleteEmailTemplate(String name) {
        templates.remove(name);
    }
    
    public static class DefaultEmailTemplate implements EmailTemplate {
        private final String name;
        private final String subject;
        private final String content;
        private final boolean html;
        private final long createdAt;
        private long updatedAt;
        
        public DefaultEmailTemplate(String name, String subject, String content, boolean html) {
            this.name = name;
            this.subject = subject;
            this.content = content;
            this.html = html;
            this.createdAt = System.currentTimeMillis();
            this.updatedAt = this.createdAt;
        }
        
        @Override
        public String getName() { return name; }
        
        @Override
        public String getSubject() { return subject; }
        
        @Override
        public String getContent() { return content; }
        
        @Override
        public boolean isHtml() { return html; }
        
        @Override
        public long getCreatedAt() { return createdAt; }
        
        @Override
        public long getUpdatedAt() { return updatedAt; }
        
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }
}
