package ltd.idcu.est.integration;

import ltd.idcu.est.integration.api.IntegrationException;
import ltd.idcu.est.integration.api.SmsService;
import ltd.idcu.est.integration.api.SmsTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DefaultSmsService implements SmsService {
    
    private static final Logger LOGGER = Logger.getLogger(DefaultSmsService.class.getName());
    private final Map<String, SmsTemplate> smsTemplates;
    
    public DefaultSmsService() {
        this.smsTemplates = new ConcurrentHashMap<>();
    }
    
    @Override
    public void sendSms(String phone, String content) {
        LOGGER.info("Sending SMS to: " + phone + ", content: " + content);
    }
    
    @Override
    public void sendSms(List<String> phones, String content) {
        LOGGER.info("Sending SMS to: " + String.join(", ", phones) + ", content: " + content);
    }
    
    @Override
    public void sendSmsWithTemplate(String phone, String templateCode, Map<String, String> variables) {
        SmsTemplate template = smsTemplates.get(templateCode);
        if (template == null) {
            throw new IntegrationException("SMS template not found: " + templateCode);
        }
        String content = applyVariables(template.getContent(), variables);
        sendSms(phone, content);
    }
    
    @Override
    public void sendSmsWithTemplate(List<String> phones, String templateCode, Map<String, String> variables) {
        SmsTemplate template = smsTemplates.get(templateCode);
        if (template == null) {
            throw new IntegrationException("SMS template not found: " + templateCode);
        }
        String content = applyVariables(template.getContent(), variables);
        sendSms(phones, content);
    }
    
    private String applyVariables(String content, Map<String, String> variables) {
        String result = content;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", 
                entry.getValue() != null ? entry.getValue() : "");
        }
        return result;
    }
    
    @Override
    public List<SmsTemplate> getSmsTemplates() {
        return new ArrayList<>(smsTemplates.values());
    }
    
    @Override
    public SmsTemplate getSmsTemplate(String code) {
        return smsTemplates.get(code);
    }
    
    @Override
    public void saveSmsTemplate(SmsTemplate template) {
        smsTemplates.put(template.getCode(), template);
    }
    
    @Override
    public void deleteSmsTemplate(String code) {
        smsTemplates.remove(code);
    }
}
