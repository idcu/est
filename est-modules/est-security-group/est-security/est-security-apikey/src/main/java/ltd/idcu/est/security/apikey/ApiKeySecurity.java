package ltd.idcu.est.security.apikey;

import java.util.UUID;

public class ApiKeySecurity {
    
    private ApiKeySecurity() {
    }
    
    public static String generateApiKey() {
        return "ak_" + UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateApiKey(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "");
    }
    
    public static ApiKeyAuthenticationProvider createProvider(
            ApiKeyAuthenticationProvider.ApiKeyValidator validator) {
        return new ApiKeyAuthenticationProvider(validator);
    }
    
    public static ApiKeyAuthenticationProvider.ApiKeyValidator createInMemoryValidator(
            java.util.Map<String, ApiKeyAuthenticationProvider.ApiKeyValidationResult> apiKeys) {
        return apiKey -> {
            ApiKeyAuthenticationProvider.ApiKeyValidationResult result = apiKeys.get(apiKey);
            return result != null ? result : ApiKeyAuthenticationProvider.DefaultApiKeyValidationResult.invalid();
        };
    }
}
