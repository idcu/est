package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeReviewSkill implements Skill {

    @Override
    public String getName() {
        return "code-review";
    }

    @Override
    public String getDescription() {
        return "Performs a basic code review and provides suggestions";
    }

    @Override
    public String getCategory() {
        return "code-quality";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public Map<String, String> getInputSchema() {
        return Map.of(
                "code", "String - The Java code to review"
        );
    }

    @Override
    public Map<String, String> getOutputSchema() {
        return Map.of(
                "issues", "List<String> - List of issues found",
                "suggestions", "List<String> - List of improvement suggestions",
                "score", "Integer - Overall quality score (0-100)"
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> inputs) {
        String code = (String) inputs.get("code");
        List<String> issues = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        int score = 100;

        if (!code.contains("/**") && !code.contains("/*")) {
            issues.add("Missing Javadoc comments");
            score -= 15;
            suggestions.add("Add Javadoc comments for classes and public methods");
        }

        if (code.contains("System.out.println")) {
            issues.add("Using System.out.println instead of proper logging");
            score -= 10;
            suggestions.add("Use a logging framework instead of System.out.println");
        }

        if (code.contains("catch (Exception e) {}")) {
            issues.add("Empty catch block");
            score -= 20;
            suggestions.add("Never leave catch blocks empty - at least log the exception");
        }

        if (code.contains("public void set")) {
            suggestions.add("Consider using immutable objects where appropriate");
        }

        if (code.contains("new ArrayList()")) {
            suggestions.add("Consider specifying initial capacity for collections");
        }

        if (score < 50) {
            suggestions.add("Consider refactoring this code - it has significant issues");
        }

        return SkillResult.success(Map.of(
                "issues", issues,
                "suggestions", suggestions,
                "score", score
        ));
    }

    @Override
    public boolean canExecute(Map<String, Object> inputs) {
        return inputs.containsKey("code");
    }
}
