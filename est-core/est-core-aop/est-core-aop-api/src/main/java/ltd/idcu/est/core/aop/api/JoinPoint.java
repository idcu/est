package ltd.idcu.est.core.aop.api;

public interface JoinPoint {
    Object getTarget();
    Object[] getArgs();
    String getSignature();
}
