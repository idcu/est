package ltd.idcu.est.codecli.agent;

import ltd.idcu.est.agent.api.Agent;
import ltd.idcu.est.agent.api.Memory;
import ltd.idcu.est.agent.api.Skill;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class AgentManagerTest {
    
    @Test
    public void testGetInstance() {
        AgentManager manager = AgentManager.getInstance();
        assertNotNull(manager);
    }
    
    @Test
    public void testGetAgent() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        assertNotNull(agent);
    }
    
    @Test
    public void testGetMemory() {
        AgentManager manager = AgentManager.getInstance();
        Memory memory = manager.getMemory();
        assertNotNull(memory);
    }
    
    @Test
    public void testProcessQuery() {
        AgentManager manager = AgentManager.getInstance();
        String result = manager.processQuery("Test query");
        assertNotNull(result);
    }
    
    @Test
    public void testProcessQueryWithMaxSteps() {
        AgentManager manager = AgentManager.getInstance();
        String result = manager.processQuery("Test query", 5);
        assertNotNull(result);
    }
    
    @Test
    public void testRegisterSkill() {
        AgentManager manager = AgentManager.getInstance();
        
        Skill testSkill = new Skill() {
            @Override
            public String getName() {
                return "test_skill";
            }
            
            @Override
            public String getDescription() {
                return "Test skill";
            }
            
            @Override
            public void initialize(ltd.idcu.est.agent.api.SkillContext context) {
            }
            
            @Override
            public ltd.idcu.est.agent.api.SkillResult execute(
                ltd.idcu.est.agent.api.SkillInput input, 
                ltd.idcu.est.agent.api.SkillContext context
            ) {
                return ltd.idcu.est.agent.api.SkillResult.success("Success");
            }
            
            @Override
            public void cleanup(ltd.idcu.est.agent.api.SkillContext context) {
            }
        };
        
        manager.registerSkill(testSkill);
        
        List<Skill> skills = manager.getAgent().getAllSkills();
        assertTrue(skills.size() >= 1);
    }
    
    @Test
    public void testClearMemory() {
        AgentManager manager = AgentManager.getInstance();
        
        manager.clearMemory();
        
        Memory memory = manager.getMemory();
        assertEquals(0, memory.size());
    }
    
    @Test
    public void testHasDefaultSkills() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        List<Skill> skills = agent.getAllSkills();
        assertNotNull(skills);
        assertTrue(skills.size() >= 2);
    }
    
    @Test
    public void testHasCodeGenerationSkill() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        Skill skill = agent.getSkill("code_generation");
        assertNotNull(skill);
        assertEquals("code_generation", skill.getName());
    }
    
    @Test
    public void testHasCodeExplanationSkill() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        Skill skill = agent.getSkill("code_explanation");
        assertNotNull(skill);
        assertEquals("code_explanation", skill.getName());
    }
    
    @Test
    public void testHasCodeOptimizationSkill() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        Skill skill = agent.getSkill("code_optimization");
        assertNotNull(skill);
        assertEquals("code_optimization", skill.getName());
    }
    
    @Test
    public void testHasBugFixSkill() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        Skill skill = agent.getSkill("bug_fix");
        assertNotNull(skill);
        assertEquals("bug_fix", skill.getName());
    }
    
    @Test
    public void testHasDocumentationSkill() {
        AgentManager manager = AgentManager.getInstance();
        Agent agent = manager.getAgent();
        
        Skill skill = agent.getSkill("documentation");
        assertNotNull(skill);
        assertEquals("documentation", skill.getName());
    }
}
