package ltd.idcu.est.multitenancy;

import ltd.idcu.est.multitenancy.api.TenantContext;
import ltd.idcu.est.multitenancy.api.TenantContextHolder;

public class ThreadLocalTenantContextHolder implements TenantContextHolder {
    
    private static final ThreadLocal<TenantContext> contextHolder = new ThreadLocal<>();
    
    private static final ThreadLocalTenantContextHolder INSTANCE = new ThreadLocalTenantContextHolder();
    
    public static ThreadLocalTenantContextHolder getInstance() {
        return INSTANCE;
    }
    
    private ThreadLocalTenantContextHolder() {
    }
    
    @Override
    public TenantContext getContext() {
        TenantContext context = contextHolder.get();
        if (context == null) {
            context = createEmptyContext();
            contextHolder.set(context);
        }
        return context;
    }
    
    @Override
    public void setContext(TenantContext context) {
        if (context == null) {
            clearContext();
        } else {
            contextHolder.set(context);
        }
    }
    
    @Override
    public void clearContext() {
        contextHolder.remove();
    }
    
    @Override
    public TenantContext createEmptyContext() {
        return new DefaultTenantContext();
    }
}
