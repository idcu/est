package ltd.idcu.est.features.ai.api.skill;

import java.util.Map;

public class SkillResult {

    private final boolean success;
    private final String message;
    private final Map<String, Object> outputs;
    private final long executionTimeMs;

    public SkillResult(boolean success, String message, Map<String, Object> outputs, long executionTimeMs) {
        this.success = success;
        this.message = message;
        this.outputs = outputs;
        this.executionTimeMs = executionTimeMs;
    }

    public static SkillResult success(Map<String, Object> outputs) {
        return new SkillResult(true, "Success", outputs, 0);
    }

    public static SkillResult success(String message, Map<String, Object> outputs) {
        return new SkillResult(true, message, outputs, 0);
    }

    public static SkillResult failure(String message) {
        return new SkillResult(false, message, null, 0);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getOutputs() {
        return outputs;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}
