package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.EmailService;
import ltd.idcu.est.integration.api.EmailTemplate;
import ltd.idcu.est.integration.api.IntegrationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DefaultEmailService implements EmailService {
    
    private static final Logger LOGGER = Logger.getLogger(DefaultEmailService.class.getName());
    private final Map<String, EmailTemplate> emailTemplates;
    
    public DefaultEmailService() {
        this.emailTemplates = new ConcurrentHashMap<>();
    }
    
    @Override
    public void sendEmail(String to, String subject, String content) {
        LOGGER.info("Sending email to: " + to + ", subject: " + subject);
    }
    
    @Override
    public void sendEmail(List<String> to, String subject, String content) {
        LOGGER.info("Sending email to: " + String.join(", ", to) + ", subject: " + subject);
    }
    
    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        LOGGER.info("Sending HTML email to: " + to + ", subject: " + subject);
    }
    
    @Override
    public void sendHtmlEmail(List<String> to, String subject, String htmlContent) {
        LOGGER.info("Sending HTML email to: " + String.join(", ", to) + ", subject: " + subject);
    }
    
    @Override
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables) {
        EmailTemplate template = emailTemplates.get(templateName);
        if (template == null) {
            throw new IntegrationException("Email template not found: " + templateName);
        }
        String content = applyVariables(template.getContent(), variables);
        if (template.isHtml()) {
            sendHtmlEmail(to, template.getSubject(), content);
        } else {
            sendEmail(to, template.getSubject(), content);
        }
    }
    
    private String applyVariables(String content, Map<String, Object> variables) {
        String result = content;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", 
                entry.getValue() != null ? entry.getValue().toString() : "");
        }
        return result;
    }
    
    @Override
    public List<EmailTemplate> getEmailTemplates() {
        return new ArrayList<>(emailTemplates.values());
    }
    
    @Override
    public EmailTemplate getEmailTemplate(String name) {
        return emailTemplates.get(name);
    }
    
    @Override
    public void saveEmailTemplate(EmailTemplate template) {
        emailTemplates.put(template.getName(), template);
    }
    
    @Override
    public void deleteEmailTemplate(String name) {
        emailTemplates.remove(name);
    }
}
