package ltd.idcu.est.dbgenerator.util;

public class NamingUtils {

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '_' || c == '-') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    public static String toPascalCase(String input) {
        String camelCase = toCamelCase(input);
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
    }

    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String pluralize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String lower = input.toLowerCase();
        if (lower.endsWith("s") || lower.endsWith("x") || 
            lower.endsWith("ch") || lower.endsWith("sh")) {
            return input + "es";
        } else if (lower.endsWith("y")) {
            return input.substring(0, input.length() - 1) + "ies";
        } else {
            return input + "s";
        }
    }
}
