package ltd.idcu.est.grpc.core;

import io.grpc.BindableService;
import ltd.idcu.est.grpc.api.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcServiceRegistry {
    
    private static final GrpcServiceRegistry INSTANCE = new GrpcServiceRegistry();
    
    private final Map<String, ServiceRegistration> services = new ConcurrentHashMap<>();
    
    private GrpcServiceRegistry() {
    }
    
    public static GrpcServiceRegistry getInstance() {
        return INSTANCE;
    }
    
    public void register(BindableService service) {
        Class<?> serviceClass = service.getClass();
        GrpcService annotation = serviceClass.getAnnotation(GrpcService.class);
        
        String serviceName = annotation != null && !annotation.name().isEmpty() 
            ? annotation.name() 
            : serviceClass.getSimpleName();
        
        String version = annotation != null ? annotation.version() : "1.0.0";
        String description = annotation != null ? annotation.description() : "";
        
        ServiceRegistration registration = new ServiceRegistration(
            serviceName, version, description, service
        );
        
        services.put(serviceName, registration);
    }
    
    public void unregister(String serviceName) {
        services.remove(serviceName);
    }
    
    public ServiceRegistration getService(String serviceName) {
        return services.get(serviceName);
    }
    
    public List<ServiceRegistration> getAllServices() {
        return new ArrayList<>(services.values());
    }
    
    public List<BindableService> getAllBindableServices() {
        List<BindableService> bindableServices = new ArrayList<>();
        for (ServiceRegistration registration : services.values()) {
            bindableServices.add(registration.getService());
        }
        return bindableServices;
    }
    
    public void clear() {
        services.clear();
    }
    
    public static class ServiceRegistration {
        private final String name;
        private final String version;
        private final String description;
        private final BindableService service;
        
        ServiceRegistration(String name, String version, String description, BindableService service) {
            this.name = name;
            this.version = version;
            this.description = description;
            this.service = service;
        }
        
        public String getName() {
            return name;
        }
        
        public String getVersion() {
            return version;
        }
        
        public String getDescription() {
            return description;
        }
        
        public BindableService getService() {
            return service;
        }
    }
    
}
