package ltd.idcu.est.integration.api;

import java.util.List;
import java.util.Map;

public interface EmailService {
    
    void sendEmail(String to, String subject, String content);
    
    void sendEmail(List<String> to, String subject, String content);
    
    void sendHtmlEmail(String to, String subject, String htmlContent);
    
    void sendHtmlEmail(List<String> to, String subject, String htmlContent);
    
    void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables);
    
    List<EmailTemplate> getEmailTemplates();
    
    EmailTemplate getEmailTemplate(String name);
    
    void saveEmailTemplate(EmailTemplate template);
    
    void deleteEmailTemplate(String name);
}
