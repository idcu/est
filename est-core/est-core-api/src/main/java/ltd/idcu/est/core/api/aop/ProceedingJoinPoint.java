package ltd.idcu.est.core.api.aop;

public interface ProceedingJoinPoint {
    Object getTarget();
    Object[] getArgs();
    Object proceed() throws Throwable;
    Object proceed(Object[] args) throws Throwable;
    String getSignature();
}
