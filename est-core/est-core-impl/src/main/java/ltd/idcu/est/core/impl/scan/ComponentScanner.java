package ltd.idcu.est.core.impl.scan;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Primary;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.annotation.Repository;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.api.scope.Scope;

import java.util.List;

public class ComponentScanner {

    public static void scanAndRegister(Container container, String... basePackages) {
        for (String basePackage : basePackages) {
            List<Class<?>> classes = ClassPathScanner.scan(basePackage);
            for (Class<?> clazz : classes) {
                registerComponent(container, clazz);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerComponent(Container container, Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        Service service = clazz.getAnnotation(Service.class);
        Repository repository = clazz.getAnnotation(Repository.class);

        String qualifier = null;
        Scope scope = Scope.SINGLETON;

        if (component != null) {
            qualifier = component.value().isEmpty() ? null : component.value();
            scope = component.scope();
        } else if (service != null) {
            qualifier = service.value().isEmpty() ? null : service.value();
        } else if (repository != null) {
            qualifier = repository.value().isEmpty() ? null : repository.value();
        } else {
            return;
        }

        if (clazz.isAnnotationPresent(Primary.class) && qualifier == null) {
            qualifier = "primary";
        }

        if (clazz.isAnnotationPresent(Qualifier.class) && qualifier == null) {
            qualifier = clazz.getAnnotation(Qualifier.class).value();
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> iface : interfaces) {
                registerWithInterface(container, iface, clazz, scope, qualifier);
            }
        } else {
            registerWithType(container, clazz, scope, qualifier);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerWithInterface(Container container, Class iface, Class impl, Scope scope, String qualifier) {
        if (qualifier != null) {
            container.register(iface, impl, scope, qualifier);
        } else {
            container.register(iface, impl, scope);
        }
        container.register(impl, impl, scope);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerWithType(Container container, Class type, Scope scope, String qualifier) {
        if (qualifier != null) {
            container.register(type, type, scope, qualifier);
        } else {
            container.register(type, type, scope);
        }
    }
}
