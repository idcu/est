package ltd.idcu.est.codecli.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigValidator {

    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;

        public ValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        public void addError(String error) {
            this.errors.add(error);
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }

        public List<String> getWarnings() {
            return new ArrayList<>(warnings);
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        public String getSummary() {
            StringBuilder sb = new StringBuilder();
            if (isValid()) {
                sb.append("Configuration is valid");
            } else {
                sb.append("Configuration has ").append(errors.size()).append(" errors");
            }
            if (hasWarnings()) {
                sb.append(" and ").append(warnings.size()).append(" warnings");
            }
            return sb.toString();
        }
    }

    public static ValidationResult validate(CliConfig config) {
        ValidationResult result = new ValidationResult();

        validateNickname(config.getNickname(), result);
        validateWorkDir(config.getWorkDir(), result);
        validateChatModel(config, result);

        return result;
    }

    private static void validateNickname(String nickname, ValidationResult result) {
        if (nickname == null || nickname.trim().isEmpty()) {
            result.addError("Nickname cannot be empty");
        } else if (nickname.length() > 50) {
            result.addError("Nickname cannot exceed 50 characters");
        } else if (!nickname.matches("^[a-zA-Z0-9_\\-]+$")) {
            result.addWarning("Nickname contains special characters, which may cause issues");
        }
    }

    private static void validateWorkDir(String workDir, ValidationResult result) {
        if (workDir == null || workDir.trim().isEmpty()) {
            result.addError("Work directory cannot be empty");
        }
    }

    private static void validateChatModel(CliConfig config, ValidationResult result) {
        String apiUrl = config.getChatModelApiUrl();
        String apiKey = config.getChatModelApiKey();
        String modelName = config.getChatModelName();

        boolean hasAnyConfig = apiUrl != null || apiKey != null || modelName != null;

        if (hasAnyConfig) {
            if (apiUrl != null && !apiUrl.isEmpty()) {
                if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
                    result.addError("Chat model API URL must start with http:// or https://");
                }
            }

            if (apiKey != null && apiKey.isEmpty()) {
                result.addWarning("Chat model API key is set but empty");
            }

            if (modelName != null && modelName.isEmpty()) {
                result.addWarning("Chat model name is set but empty");
            }

            if (apiKey == null || apiKey.isEmpty()) {
                if (apiUrl != null && !apiUrl.isEmpty()) {
                    result.addWarning("API URL is configured but API key is missing");
                }
            }
        }
    }
}
