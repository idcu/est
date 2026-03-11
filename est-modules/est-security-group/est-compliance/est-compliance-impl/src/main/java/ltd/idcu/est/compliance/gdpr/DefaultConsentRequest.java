package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.ConsentRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DefaultConsentRequest implements ConsentRequest {

    private final LocalDateTime expiresAt;
    private final Map<String, Object> metadata;
    private final String description;

    private DefaultConsentRequest(Builder builder) {
        this.expiresAt = builder.expiresAt;
        this.metadata = builder.metadata != null ? new HashMap<>(builder.metadata) : new HashMap<>();
        this.description = builder.description;
    }

    @Override
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime expiresAt;
        private Map<String, Object> metadata;
        private String description;

        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public DefaultConsentRequest build() {
            return new DefaultConsentRequest(this);
        }
    }
}
