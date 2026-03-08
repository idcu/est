package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.SmsService;
import ltd.idcu.est.admin.api.SmsTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSmsService implements SmsService {
    
    private final Map<String, DefaultSmsTemplate> templates = new ConcurrentHashMap<>();
    
    public DefaultSmsService() {
        initializeDefaultTemplates();
    }
    
    private void initializeDefaultTemplates() {
        DefaultSmsTemplate verifyCodeTemplate = new DefaultSmsTemplate(
            "VERIFY_CODE",
            "验证�?,
            "您的验证码是�?{code}�?分钟内有效�?,
            "mock"
        );
        templates.put("VERIFY_CODE", verifyCodeTemplate);
        
        DefaultSmsTemplate loginTemplate = new DefaultSmsTemplate(
            "LOGIN_NOTICE",
            "登录通知",
            "您的账号�?{time}�?{ip}登录，如非本人操作请及时修改密码�?,
            "mock"
        );
        templates.put("LOGIN_NOTICE", loginTemplate);
    }
    
    @Override
    public void sendSms(String phone, String content) {
        System.out.println("[SMS] Sending SMS to: " + phone + ", content: " + content);
    }
    
    @Override
    public void sendSms(List<String> phones, String content) {
        System.out.println("[SMS] Sending SMS to: " + phones + ", content: " + content);
    }
    
    @Override
    public void sendSmsWithTemplate(String phone, String templateCode, Map<String, String> variables) {
        SmsTemplate template = getSmsTemplate(templateCode);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateCode);
        }
        
        String content = template.getContent();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        
        sendSms(phone, content);
    }
    
    @Override
    public void sendSmsWithTemplate(List<String> phones, String templateCode, Map<String, String> variables) {
        SmsTemplate template = getSmsTemplate(templateCode);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateCode);
        }
        
        String content = template.getContent();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        
        sendSms(phones, content);
    }
    
    @Override
    public List<SmsTemplate> getSmsTemplates() {
        return new ArrayList<>(templates.values());
    }
    
    @Override
    public SmsTemplate getSmsTemplate(String code) {
        return templates.get(code);
    }
    
    @Override
    public void saveSmsTemplate(SmsTemplate template) {
        if (template instanceof DefaultSmsTemplate defaultTemplate) {
            templates.put(defaultTemplate.getCode(), defaultTemplate);
        } else {
            DefaultSmsTemplate newTemplate = new DefaultSmsTemplate(
                template.getCode(),
                template.getName(),
                template.getContent(),
                template.getProvider()
            );
            templates.put(newTemplate.getCode(), newTemplate);
        }
    }
    
    @Override
    public void deleteSmsTemplate(String code) {
        templates.remove(code);
    }
    
    public static class DefaultSmsTemplate implements SmsTemplate {
        private final String code;
        private final String name;
        private final String content;
        private final String provider;
        private final long createdAt;
        private long updatedAt;
        
        public DefaultSmsTemplate(String code, String name, String content, String provider) {
            this.code = code;
            this.name = name;
            this.content = content;
            this.provider = provider;
            this.createdAt = System.currentTimeMillis();
            this.updatedAt = this.createdAt;
        }
        
        @Override
        public String getCode() { return code; }
        
        @Override
        public String getName() { return name; }
        
        @Override
        public String getContent() { return content; }
        
        @Override
        public String getProvider() { return provider; }
        
        @Override
        public long getCreatedAt() { return createdAt; }
        
        @Override
        public long getUpdatedAt() { return updatedAt; }
        
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }
}
