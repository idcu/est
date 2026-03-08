package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.OperationLogAnnotation;
import ltd.idcu.est.admin.api.OperationLogService;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;
import ltd.idcu.est.core.aop.api.annotation.Around;
import ltd.idcu.est.core.aop.api.annotation.Aspect;
import ltd.idcu.est.security.api.SecurityContext;
import ltd.idcu.est.security.api.User;

import java.util.Arrays;

@Aspect(order = 2)
public class OperationLogAspect {
    
    private final OperationLogService operationLogService;
    private final SecurityContext securityContext;
    
    public OperationLogAspect(OperationLogService operationLogService, SecurityContext securityContext) {
        this.operationLogService = operationLogService;
        this.securityContext = securityContext;
    }
    
    @Around("@annotation(operationLogAnnotation)")
    public Object logOperation(ProceedingJoinPoint joinPoint, OperationLogAnnotation operationLogAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        int status = 1;
        String errorMsg = null;
        Object result = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            status = 0;
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            saveOperationLog(joinPoint, operationLogAnnotation, executionTime, status, errorMsg);
        }
    }
    
    private void saveOperationLog(ProceedingJoinPoint joinPoint, OperationLogAnnotation annotation, 
                                   Long time, int status, String errorMsg) {
        String userId = null;
        String username = "anonymous";
        
        try {
            java.util.Optional<User> userOpt = securityContext.getCurrentUser();
            if (userOpt.isPresent()) {
                User currentUser = userOpt.get();
                if (currentUser instanceof ltd.idcu.est.admin.api.User adminUser) {
                    userId = adminUser.getId();
                    username = adminUser.getUsername();
                }
            }
        } catch (Exception e) {
        }
        
        String module = annotation.module();
        String operation = annotation.operation();
        String methodName = joinPoint.getSignature();
        
        String params = "";
        if (annotation.saveParams()) {
            Object[] args = joinPoint.getArgs();
            params = Arrays.toString(args);
        }
        
        String ip = getClientIp();
        String userAgent = getUserAgent();
        
        operationLogService.createOperationLog(
            userId, username, module, operation, methodName, params, 
            time, ip, userAgent, status, errorMsg
        );
    }
    
    private String getClientIp() {
        return "127.0.0.1";
    }
    
    private String getUserAgent() {
        return "Unknown";
    }
}
