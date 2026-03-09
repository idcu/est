package ltd.idcu.est.codecli.skills;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class SkillManagerTest {

    private SkillManager skillManager;

    @BeforeEach
    void beforeEach() {
        skillManager = new SkillManager();
    }

    @Test
    void testDefaultSkillsRegistered() {
        List<EstSkill> skills = skillManager.getAllSkills();
        assertEquals(5, skills.size());
    }

    @Test
    void testHasDefaultSkills() {
        assertTrue(skillManager.hasSkill("code_review"));
        assertTrue(skillManager.hasSkill("refactor"));
        assertTrue(skillManager.hasSkill("architecture"));
        assertTrue(skillManager.hasSkill("performance_optimization"));
        assertTrue(skillManager.hasSkill("security_audit"));
    }

    @Test
    void testGetSkill() {
        EstSkill skill = skillManager.getSkill("code_review");
        assertNotNull(skill);
        assertEquals("code_review", skill.getName());
    }

    @Test
    void testGetNonExistentSkill() {
        EstSkill skill = skillManager.getSkill("nonexistent_skill");
        assertNull(skill);
    }

    @Test
    void testRegisterSkill() {
        EstSkill newSkill = new EstSkill() {
            @Override
            public String getName() {
                return "custom_skill";
            }

            @Override
            public String getDescription() {
                return "Custom skill description";
            }

            @Override
            public boolean canHandle(String userInput) {
                return userInput.contains("custom");
            }

            @Override
            public String execute(String userInput) {
                return "Custom skill executed";
            }
        };

        skillManager.registerSkill(newSkill);

        assertTrue(skillManager.hasSkill("custom_skill"));
        EstSkill retrieved = skillManager.getSkill("custom_skill");
        assertEquals("custom_skill", retrieved.getName());
    }

    @Test
    void testUnregisterSkill() {
        assertTrue(skillManager.hasSkill("code_review"));

        skillManager.unregisterSkill("code_review");

        assertFalse(skillManager.hasSkill("code_review"));
    }

    @Test
    void testUnregisterNonExistentSkill() {
        skillManager.unregisterSkill("nonexistent");
        List<EstSkill> skills = skillManager.getAllSkills();
        assertEquals(5, skills.size());
    }

    @Test
    void testGetAllSkills() {
        List<EstSkill> skills = skillManager.getAllSkills();
        assertEquals(5, skills.size());
    }

    @Test
    void testFindMatchingSkill() {
        EstSkill matched = skillManager.findMatchingSkill("请审查这段代�?);
        assertNotNull(matched);
        assertEquals("code_review", matched.getName());
    }

    @Test
    void testFindMatchingSkillPerformance() {
        EstSkill matched = skillManager.findMatchingSkill("优化这段代码的性能");
        assertNotNull(matched);
        assertEquals("performance_optimization", matched.getName());
    }

    @Test
    void testFindMatchingSkillSecurity() {
        EstSkill matched = skillManager.findMatchingSkill("检查这段代码的安全�?);
        assertNotNull(matched);
        assertEquals("security_audit", matched.getName());
    }

    @Test
    void testFindNoMatchingSkill() {
        EstSkill matched = skillManager.findMatchingSkill("这是一段无关的文本");
        assertNull(matched);
    }

    @Test
    void testListSkills() {
        String list = skillManager.listSkills();
        assertNotNull(list);
        assertTrue(list.contains("Available EST Skills"));
        assertTrue(list.contains("code_review"));
        assertTrue(list.contains("refactor"));
        assertTrue(list.contains("architecture"));
        assertTrue(list.contains("performance_optimization"));
        assertTrue(list.contains("security_audit"));
    }

    @Test
    void testRegisterOverwritesExisting() {
        EstSkill original = skillManager.getSkill("code_review");
        assertNotNull(original);

        EstSkill replacement = new EstSkill() {
            @Override
            public String getName() {
                return "code_review";
            }

            @Override
            public String getDescription() {
                return "Replaced description";
            }

            @Override
            public boolean canHandle(String userInput) {
                return false;
            }

            @Override
            public String execute(String userInput) {
                return "Replaced";
            }
        };

        skillManager.registerSkill(replacement);

        EstSkill retrieved = skillManager.getSkill("code_review");
        assertEquals("Replaced description", retrieved.getDescription());
    }
}
