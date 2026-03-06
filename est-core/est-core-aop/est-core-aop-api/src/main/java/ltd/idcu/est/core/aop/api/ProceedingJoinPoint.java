package ltd.idcu.est.core.aop.api;

public interface ProceedingJoinPoint {
    Object getTarget();
    Object[] getArgs();
    Object proceed() throws Throwable;
    Object proceed(Object[] args) throws Throwable;
    String getSignature();
}
