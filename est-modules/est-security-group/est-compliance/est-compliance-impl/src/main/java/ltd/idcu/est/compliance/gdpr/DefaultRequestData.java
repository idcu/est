package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.RequestData;

import java.util.HashMap;
import java.util.Map;

public class DefaultRequestData implements RequestData {

    private final Map<String, Object> data;
    private final String reason;

    public DefaultRequestData(String reason, Map<String, Object> additionalData) {
        this.reason = reason;
        this.data = new HashMap<>();
        if (additionalData != null) {
            this.data.putAll(additionalData);
        }
        this.data.put("reason", reason);
    }

    @Override
    public Map<String, Object> toMap() {
        return new HashMap<>(data);
    }

    @Override
    public String getReason() {
        return reason;
    }
}
