package ltd.idcu.est.examples.basic.core;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.impl.DefaultContainer;

public class Core04_AnnotationInjection {
    public static void main(String[] args) {
        System.out.println("=== 进阶篇：@Inject 注解注入 ===\n");
        
        Container container = new DefaultContainer();
        
        container.register(UserService.class, UserServiceImpl.class);
        container.register(EmailService.class, EmailServiceImpl.class);
        container.register(NotificationService.class, NotificationServiceImpl.class);
        
        NotificationService notificationService = container.get(NotificationService.class);
        notificationService.sendNotification("你有新消息！");
    }
}

interface NotificationService {
    void sendNotification(String message);
}

class NotificationServiceImpl implements NotificationService {
    @Inject
    private UserService userService;
    
    private EmailService emailService;
    
    @Inject
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @Override
    public void sendNotification(String message) {
        String userName = userService.getUserName();
        emailService.sendEmail(userName, message);
    }
}

interface EmailService {
    void sendEmail(String to, String message);
}

class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String message) {
        System.out.println("发送邮件给 " + to + "：" + message);
    }
}
