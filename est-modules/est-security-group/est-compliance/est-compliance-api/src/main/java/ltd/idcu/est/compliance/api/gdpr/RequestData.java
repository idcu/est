package ltd.idcu.est.compliance.api.gdpr;

import java.util.Map;

public interface RequestData {

    Map<String, Object> toMap();

    String getReason();
}
