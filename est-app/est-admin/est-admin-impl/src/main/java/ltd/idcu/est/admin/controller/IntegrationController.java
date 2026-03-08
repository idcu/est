package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.*;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntegrationController {
    
    private final EmailService emailService;
    private final SmsService smsService;
    private final OssService ossService;
    
    public IntegrationController() {
        this.emailService = Admin.createEmailService();
        this.smsService = Admin.createSmsService();
        this.ossService = Admin.createOssService();
    }
    
    @RequirePermission("integration:email:send")
    public void sendEmail(Request req, Response res) {
        try {
            String to = req.getParameter("to");
            String subject = req.getParameter("subject");
            String content = req.getParameter("content");
            boolean isHtml = Boolean.parseBoolean(req.getParameter("isHtml"));
            
            if (isHtml) {
                emailService.sendHtmlEmail(to, subject, content);
            } else {
                emailService.sendEmail(to, subject, content);
            }
            
            res.json(ApiResponse.success("Email sent successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:email:template:list")
    public void listEmailTemplates(Request req, Response res) {
        try {
            List<EmailTemplate> templates = emailService.getEmailTemplates();
            List<Map<String, Object>> templateList = templates.stream()
                .map(this::toEmailTemplateMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(templateList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:sms:send")
    public void sendSms(Request req, Response res) {
        try {
            String phone = req.getParameter("phone");
            String content = req.getParameter("content");
            smsService.sendSms(phone, content);
            res.json(ApiResponse.success("SMS sent successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:sms:template:list")
    public void listSmsTemplates(Request req, Response res) {
        try {
            List<SmsTemplate> templates = smsService.getSmsTemplates();
            List<Map<String, Object>> templateList = templates.stream()
                .map(this::toSmsTemplateMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(templateList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:oss:list")
    public void listBuckets(Request req, Response res) {
        try {
            List<String> buckets = ossService.listBuckets();
            res.json(ApiResponse.success(buckets));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:oss:list")
    public void listFiles(Request req, Response res) {
        try {
            String bucketName = req.getParameter("bucketName");
            String prefix = req.getParameter("prefix");
            List<OssFile> files = prefix != null ? 
                ossService.listFiles(bucketName, prefix) : 
                ossService.listFiles(bucketName);
            List<Map<String, Object>> fileList = files.stream()
                .map(this::toOssFileMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(fileList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:oss:delete")
    public void deleteFile(Request req, Response res) {
        try {
            String bucketName = req.getParameter("bucketName");
            String fileName = req.getParameter("fileName");
            ossService.deleteFile(bucketName, fileName);
            res.json(ApiResponse.success("File deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("integration:oss:upload")
    public void uploadFile(Request req, Response res) {
        try {
            String bucketName = req.getParameter("bucketName");
            String fileName = req.getParameter("fileName");
            var file = req.getFile("file");
            
            if (file == null) {
                res.setStatus(400);
                res.json(ApiResponse.badRequest("No file uploaded"));
                return;
            }
            
            ossService.uploadFile(bucketName, fileName, file.getInputStream());
            
            List<OssFile> files = ossService.listFiles(bucketName);
            OssFile ossFile = files.stream()
                .filter(f -> f.getFileName().equals(fileName))
                .findFirst()
                .orElse(null);
            
            res.json(ApiResponse.success("File uploaded successfully", ossFile != null ? toOssFileMap(ossFile) : null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toEmailTemplateMap(EmailTemplate template) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", template.getName());
        map.put("subject", template.getSubject());
        map.put("content", template.getContent());
        map.put("html", template.isHtml());
        map.put("createdAt", template.getCreatedAt());
        map.put("updatedAt", template.getUpdatedAt());
        return map;
    }
    
    private Map<String, Object> toSmsTemplateMap(SmsTemplate template) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", template.getCode());
        map.put("name", template.getName());
        map.put("content", template.getContent());
        map.put("provider", template.getProvider());
        map.put("createdAt", template.getCreatedAt());
        map.put("updatedAt", template.getUpdatedAt());
        return map;
    }
    
    private Map<String, Object> toOssFileMap(OssFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("bucketName", file.getBucketName());
        map.put("fileName", file.getFileName());
        map.put("filePath", file.getFilePath());
        map.put("size", file.getSize());
        map.put("contentType", file.getContentType());
        map.put("lastModified", file.getLastModified());
        map.put("etag", file.getEtag());
        map.put("url", ossService.getFileUrl(file.getBucketName(), file.getFileName()));
        return map;
    }
}
