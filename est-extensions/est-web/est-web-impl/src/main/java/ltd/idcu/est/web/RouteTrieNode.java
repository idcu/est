package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Route;

import java.util.HashMap;
import java.util.Map;

class RouteTrieNode {
    
    private final Map<String, RouteTrieNode> children = new HashMap<>();
    private RouteTrieNode wildcardChild;
    private RouteTrieNode paramChild;
    private String paramName;
    private Route route;
    
    RouteTrieNode() {
    }
    
    Map<String, RouteTrieNode> getChildren() {
        return children;
    }
    
    RouteTrieNode getWildcardChild() {
        return wildcardChild;
    }
    
    void setWildcardChild(RouteTrieNode wildcardChild) {
        this.wildcardChild = wildcardChild;
    }
    
    RouteTrieNode getParamChild() {
        return paramChild;
    }
    
    void setParamChild(RouteTrieNode paramChild) {
        this.paramChild = paramChild;
    }
    
    String getParamName() {
        return paramName;
    }
    
    void setParamName(String paramName) {
        this.paramName = paramName;
    }
    
    Route getRoute() {
        return route;
    }
    
    void setRoute(Route route) {
        this.route = route;
    }
}
