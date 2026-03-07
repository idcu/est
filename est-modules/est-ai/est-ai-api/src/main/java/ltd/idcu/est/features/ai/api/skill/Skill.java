package ltd.idcu.est.features.ai.api.skill;

import java.util.Map;

public interface Skill {

    String getName();

    String getDescription();

    String getCategory();

    String getVersion();

    Map<String, String> getInputSchema();

    Map<String, String> getOutputSchema();

    SkillResult execute(Map<String, Object> inputs);

    boolean canExecute(Map<String, Object> inputs);
}
