package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.FunctionRegistry;
import ltd.idcu.est.ai.api.FunctionTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFunctionRegistry implements FunctionRegistry {
    
    private final Map<String, FunctionTool> tools = new HashMap<>();
    
    @Override
    public void register(FunctionTool tool) {
        tools.put(tool.getName(), tool);
    }
    
    @Override
    public void unregister(String name) {
        tools.remove(name);
    }
    
    @Override
    public FunctionTool getTool(String name) {
        return tools.get(name);
    }
    
    @Override
    public List<FunctionTool> listTools() {
        return new ArrayList<>(tools.values());
    }
}
