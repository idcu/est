package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Map;

public interface SmsService {
    
    void sendSms(String phone, String content);
    
    void sendSms(List<String> phones, String content);
    
    void sendSmsWithTemplate(String phone, String templateCode, Map<String, String> variables);
    
    void sendSmsWithTemplate(List<String> phones, String templateCode, Map<String, String> variables);
    
    List<SmsTemplate> getSmsTemplates();
    
    SmsTemplate getSmsTemplate(String code);
    
    void saveSmsTemplate(SmsTemplate template);
    
    void deleteSmsTemplate(String code);
}
