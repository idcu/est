package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.User;

import java.util.Set;

public class DefaultAuthentication implements Authentication {
    
    private String token;
    private User user;
    private boolean authenticated;
    private Object credentials;
    private Object details;
    private Set<String> roles;
    private Set<String> permissions;
    
    public DefaultAuthentication() {
        this.authenticated = false;
    }
    
    public DefaultAuthentication(User user) {
        this.user = user;
        this.authenticated = false;
    }
    
    public DefaultAuthentication(User user, Object credentials) {
        this.user = user;
        this.credentials = credentials;
        this.authenticated = false;
    }
    
    @Override
    public String getToken() {
        return token;
    }
    
    public DefaultAuthentication setToken(String token) {
        this.token = token;
        return this;
    }
    
    @Override
    public User getUser() {
        return user;
    }
    
    public DefaultAuthentication setUser(User user) {
        this.user = user;
        return this;
    }
    
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    @Override
    public Object getCredentials() {
        return credentials;
    }
    
    public DefaultAuthentication setCredentials(Object credentials) {
        this.credentials = credentials;
        return this;
    }
    
    @Override
    public Object getDetails() {
        return details;
    }
    
    public DefaultAuthentication setDetails(Object details) {
        this.details = details;
        return this;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public DefaultAuthentication setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public DefaultAuthentication setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
    
    public static DefaultAuthentication of(User user) {
        return new DefaultAuthentication(user);
    }
    
    public static DefaultAuthentication of(User user, Object credentials) {
        return new DefaultAuthentication(user, credentials);
    }
    
    public static DefaultAuthentication unauthenticated() {
        return new DefaultAuthentication();
    }
    
    @Override
    public String toString() {
        return "DefaultAuthentication{" +
                "token='" + token + '\'' +
                ", username='" + (user != null ? user.getUsername() : "null") + '\'' +
                ", authenticated=" + authenticated +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
