package ltd.idcu.est.discovery.api;

import java.util.List;
import java.util.Map;

public interface ServiceRouter {
    ServiceInstance choose(String serviceId, Map<String, String> requestContext);

    List<ServiceInstance> filter(String serviceId, List<ServiceInstance> instances, Map<String, String> requestContext);
}
