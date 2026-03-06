package ltd.idcu.est.core.aop.impl;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class PointcutExpression {
    private final Pattern classPattern;
    private final Pattern methodPattern;

    public PointcutExpression(String expression) {
        String[] parts = expression.split("\\.");
        if (parts.length >= 2) {
            String classPart = String.join(".", java.util.Arrays.copyOf(parts, parts.length - 1));
            String methodPart = parts[parts.length - 1];
            this.classPattern = Pattern.compile(classPart.replace("*", ".*"));
            this.methodPattern = Pattern.compile(methodPart.replace("*", ".*"));
        } else {
            this.classPattern = Pattern.compile(".*");
            this.methodPattern = Pattern.compile(expression.replace("*", ".*"));
        }
    }

    public boolean matches(Class<?> clazz, Method method) {
        return classPattern.matcher(clazz.getName()).matches() &&
               methodPattern.matcher(method.getName()).matches();
    }
}
