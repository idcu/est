package ltd.idcu.est.agent.impl;

import ltd.idcu.est.agent.api.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultAgent implements Agent {
    
    private final List<Skill> skills = new CopyOnWriteArrayList<>();
    private Memory memory;
    private String systemPrompt;
    
    public DefaultAgent() {
        this.memory = new InMemoryMemory();
        this.systemPrompt = "你是一个有用的 AI 助手。请尽力帮助用户完成任务。";
    }
    
    public DefaultAgent(Memory memory) {
        this.memory = memory;
        this.systemPrompt = "你是一个有用的 AI 助手。请尽力帮助用户完成任务。";
    }
    
    @Override
    public AgentResponse execute(String task) {
        return execute(new AgentRequest(task));
    }
    
    @Override
    public AgentResponse execute(AgentRequest request) {
        AgentResponse response = new AgentResponse();
        List<AgentResponse.AgentStep> steps = new ArrayList<>();
        
        try {
            MemoryItem taskItem = new MemoryItem();
            taskItem.setContent(request.getTask());
            taskItem.setType(MemoryItem.MemoryType.TASK);
            memory.add(taskItem);
            
            int currentStep = 0;
            boolean taskComplete = false;
            StringBuilder finalResult = new StringBuilder();
            
            while (currentStep < request.getMaxSteps() && !taskComplete) {
                AgentResponse.AgentStep step = new AgentResponse.AgentStep();
                step.setStepNumber(currentStep + 1);
                long stepStart = System.currentTimeMillis();
                
                Skill selectedSkill = selectSkill(request.getTask());
                if (selectedSkill != null) {
                    step.setAction("使用技能: " + selectedSkill.getName());
                    step.setThought("选择使用技能 " + selectedSkill.getName() + " 来完成任务");
                    
                    SkillInput input = new SkillInput(request.getTask());
                    input.setContext(request.getContext());
                    
                    SkillContext skillContext = new SkillContext();
                    skillContext.setAgent(this);
                    selectedSkill.initialize(skillContext);
                    
                    SkillResult skillResult = selectedSkill.execute(input);
                    selectedSkill.cleanup();
                    
                    step.setToolName(selectedSkill.getName());
                    step.setToolInput(input);
                    step.setToolOutput(skillResult);
                    
                    if (skillResult.isSuccess()) {
                        step.setObservation("技能执行成功: " + skillResult.getOutput());
                        finalResult.append(skillResult.getOutput());
                        taskComplete = true;
                    } else {
                        step.setObservation("技能执行失败: " + skillResult.getError());
                    }
                    
                    MemoryItem actionItem = new MemoryItem();
                    actionItem.setContent("使用技能 " + selectedSkill.getName() + ": " + (skillResult.isSuccess() ? "成功" : "失败"));
                    actionItem.setType(MemoryItem.MemoryType.ACTION);
                    memory.add(actionItem);
                } else {
                    step.setAction("直接回答");
                    step.setThought("没有找到合适的技能，直接给出回答");
                    step.setObservation("基于系统提示词回答");
                    finalResult.append("这是一个模拟回答。在实际使用中，这里会集成 LLM 来生成回答。");
                    taskComplete = true;
                }
                
                step.setDuration(System.currentTimeMillis() - stepStart);
                steps.add(step);
                currentStep++;
            }
            
            response.setSuccess(true);
            response.setResult(finalResult.toString());
            response.setSteps(steps);
            response.setTotalSteps(currentStep);
            
            if (!taskComplete) {
                response.setResult(finalResult.append("\n\n（任务未完成，已达到最大步数）").toString());
            }
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("执行失败: " + e.getMessage());
            response.setSteps(steps);
        }
        
        return response;
    }
    
    private Skill selectSkill(String task) {
        return skills.stream()
            .filter(skill -> skill.canHandle(task))
            .max(Comparator.comparingInt(Skill::getPriority))
            .orElse(null);
    }
    
    @Override
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    
    @Override
    public void removeSkill(String skillName) {
        skills.removeIf(skill -> skill.getName().equals(skillName));
    }
    
    @Override
    public List<Skill> getSkills() {
        return new ArrayList<>(skills);
    }
    
    @Override
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    
    @Override
    public Memory getMemory() {
        return memory;
    }
    
    @Override
    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
    
    @Override
    public String getSystemPrompt() {
        return systemPrompt;
    }
    
    @Override
    public void reset() {
        memory.clear();
    }
}
