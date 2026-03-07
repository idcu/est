package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.CsrfToken;

import java.security.SecureRandom;
import java.util.Base64;

public class DefaultCsrfToken implements CsrfToken {
    
    private static final String DEFAULT_HEADER_NAME = "X-CSRF-TOKEN";
    private static final String DEFAULT_PARAMETER_NAME = "_csrf";
    private static final int TOKEN_LENGTH = 32;
    
    private final String headerName;
    private final String parameterName;
    private final String token;
    
    public DefaultCsrfToken() {
        this(DEFAULT_HEADER_NAME, DEFAULT_PARAMETER_NAME, generateToken());
    }
    
    public DefaultCsrfToken(String headerName, String parameterName, String token) {
        this.headerName = headerName;
        this.parameterName = parameterName;
        this.token = token;
    }
    
    private static String generateToken() {
        byte[] bytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    @Override
    public String getHeaderName() {
        return headerName;
    }
    
    @Override
    public String getParameterName() {
        return parameterName;
    }
    
    @Override
    public String getToken() {
        return token;
    }
    
    public boolean validate(String token) {
        return this.token.equals(token);
    }
}
