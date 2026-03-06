package ltd.idcu.est.core.tx.impl;

import ltd.idcu.est.core.aop.api.JoinPoint;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;
import ltd.idcu.est.core.aop.api.annotation.After;
import ltd.idcu.est.core.aop.api.annotation.AfterThrowing;
import ltd.idcu.est.core.aop.api.annotation.Around;
import ltd.idcu.est.core.aop.api.annotation.Aspect;
import ltd.idcu.est.core.aop.api.annotation.Before;
import ltd.idcu.est.core.tx.api.PlatformTransactionManager;
import ltd.idcu.est.core.tx.api.TransactionDefinition;
import ltd.idcu.est.core.tx.api.TransactionStatus;
import ltd.idcu.est.core.tx.api.annotation.Transactional;
import ltd.idcu.est.utils.common.AssertUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Aspect(order = Integer.MIN_VALUE)
public class TransactionAspect {
    private final PlatformTransactionManager transactionManager;
    private final ThreadLocal<TransactionInfo> transactionInfoHolder = new ThreadLocal<>();

    public TransactionAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("*")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethodFromJoinPoint(joinPoint);
        Transactional transactional = findTransactionalAnnotation(method, joinPoint.getTarget().getClass());
        
        if (transactional == null) {
            return joinPoint.proceed();
        }

        TransactionInfo txInfo = createTransactionInfo(transactional);
        transactionInfoHolder.set(txInfo);

        try {
            Object result = joinPoint.proceed();
            commitTransactionAfterReturning(txInfo);
            return result;
        } catch (Throwable ex) {
            rollbackTransactionOnThrowable(txInfo, ex, transactional);
            throw ex;
        } finally {
            cleanupTransactionInfo(txInfo);
        }
    }

    private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) {
        try {
            String signature = joinPoint.getSignature();
            String className = signature.substring(0, signature.lastIndexOf('#'));
            String methodName = signature.substring(signature.lastIndexOf('#') + 1);
            
            Class<?> targetClass = joinPoint.getTarget().getClass();
            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private Transactional findTransactionalAnnotation(Method method, Class<?> targetClass) {
        if (method != null && method.isAnnotationPresent(Transactional.class)) {
            return method.getAnnotation(Transactional.class);
        }
        if (targetClass.isAnnotationPresent(Transactional.class)) {
            return targetClass.getAnnotation(Transactional.class);
        }
        return null;
    }

    private TransactionInfo createTransactionInfo(Transactional transactional) {
        TransactionDefinition definition = DefaultTransactionDefinition.from(transactional);
        TransactionStatus status = transactionManager.getTransaction(definition);
        return new TransactionInfo(status, transactional);
    }

    private void commitTransactionAfterReturning(TransactionInfo txInfo) {
        if (txInfo != null && txInfo.hasTransaction()) {
            transactionManager.commit(txInfo.getStatus());
        }
    }

    private void rollbackTransactionOnThrowable(TransactionInfo txInfo, Throwable ex, Transactional transactional) {
        if (txInfo != null && txInfo.hasTransaction()) {
            if (shouldRollbackOn(ex, transactional)) {
                transactionManager.rollback(txInfo.getStatus());
            } else {
                transactionManager.commit(txInfo.getStatus());
            }
        }
    }

    private boolean shouldRollbackOn(Throwable ex, Transactional transactional) {
        Set<Class<? extends Throwable>> noRollbackFor = new HashSet<>(Arrays.asList(transactional.noRollbackFor()));
        for (Class<? extends Throwable> nr : noRollbackFor) {
            if (nr.isInstance(ex)) {
                return false;
            }
        }

        Set<Class<? extends Throwable>> rollbackFor = new HashSet<>(Arrays.asList(transactional.rollbackFor()));
        if (!rollbackFor.isEmpty()) {
            for (Class<? extends Throwable> rf : rollbackFor) {
                if (rf.isInstance(ex)) {
                    return true;
                }
            }
            return false;
        }

        return ex instanceof RuntimeException || ex instanceof Error;
    }

    private void cleanupTransactionInfo(TransactionInfo txInfo) {
        transactionInfoHolder.remove();
    }

    private static class TransactionInfo {
        private final TransactionStatus status;
        private final Transactional transactional;

        TransactionInfo(TransactionStatus status, Transactional transactional) {
            this.status = status;
            this.transactional = transactional;
        }

        TransactionStatus getStatus() {
            return status;
        }

        boolean hasTransaction() {
            return status != null;
        }
    }
}
