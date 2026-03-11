package ltd.idcu.est.agent.skill;

import ltd.idcu.est.agent.api.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

public class CalculatorSkill implements Skill {
    
    @Override
    public String getName() {
        return "calculator";
    }
    
    @Override
    public String getDescription() {
        return "计算器技能，支持数学表达式计算";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input) {
        String expression = input.getTask();
        
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            
            String cleanExpr = expression.replaceAll("[^0-9+\\-*/().]", "");
            Object result = engine.eval(cleanExpr);
            
            return SkillResult.success("计算结果: " + expression + " = " + result);
        } catch (Exception e) {
            return SkillResult.failure("计算失败: " + e.getMessage());
        }
    }
    
    @Override
    public void cleanup() {
    }
    
    @Override
    public boolean canHandle(String task) {
        return task.matches(".*[0-9].*[+\\-*/].*[0-9].*") ||
               task.toLowerCase().contains("计算") ||
               task.toLowerCase().contains("calc") ||
               task.toLowerCase().contains("等于");
    }
    
    @Override
    public int getPriority() {
        return 20;
    }
}
