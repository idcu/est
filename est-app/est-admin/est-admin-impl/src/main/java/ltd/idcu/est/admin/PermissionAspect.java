package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;
import ltd.idcu.est.core.aop.api.annotation.Around;
import ltd.idcu.est.core.aop.api.annotation.Aspect;
import ltd.idcu.est.security.api.SecurityContext;
import ltd.idcu.est.security.api.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect(order = 1)
public class PermissionAspect {

    private final SecurityContext securityContext;

    public PermissionAspect(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Optional<User> userOpt = securityContext.getCurrentUser();
        
        if (userOpt.isEmpty()) {
            throw new AdminException("User not logged in");
        }

        User currentUser = userOpt.get();
        String[] requiredPermissions = requirePermission.value();
        RequirePermission.Logical logical = requirePermission.logical();

        List<String> userPermissions = getUserPermissions(currentUser);

        boolean hasPermission = checkPermissions(userPermissions, requiredPermissions, logical);

        if (!hasPermission) {
            throw new AdminException("Insufficient permissions, required: " + Arrays.toString(requiredPermissions));
        }

        return joinPoint.proceed();
    }

    private List<String> getUserPermissions(User user) {
        if (user instanceof ltd.idcu.est.admin.api.User adminUser) {
            return adminUser.getPermissions().stream().toList();
        }
        return List.of();
    }

    private boolean checkPermissions(List<String> userPermissions, String[] requiredPermissions, RequirePermission.Logical logical) {
        if (requiredPermissions.length == 0) {
            return true;
        }

        if (logical == RequirePermission.Logical.AND) {
            return userPermissions.containsAll(Arrays.asList(requiredPermissions));
        } else {
            return Arrays.stream(requiredPermissions).anyMatch(userPermissions::contains);
        }
    }
}
