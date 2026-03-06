package ltd.idcu.est.core.api.aop;

@Deprecated
public interface JoinPoint {
    Object getTarget();
    Object[] getArgs();
    String getSignature();
}
