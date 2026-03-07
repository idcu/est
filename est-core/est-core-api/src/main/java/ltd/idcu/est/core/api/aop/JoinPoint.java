package ltd.idcu.est.core.api.aop;

public interface JoinPoint {
    Object getTarget();
    Object[] getArgs();
    String getSignature();
}
