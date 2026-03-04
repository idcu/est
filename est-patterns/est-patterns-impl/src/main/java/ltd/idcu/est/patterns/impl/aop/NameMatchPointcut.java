package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class NameMatchPointcut implements Pointcut {
    
    private final String expression;
    private final Pattern pattern;
    
    public NameMatchPointcut(String expression) {
        this.expression = expression;
        this.pattern = Pattern.compile(expression.replace("*", ".*"));
    }
    
    @Override
    public boolean matches(Class<?> targetClass, Method method) {
        String fullMethodName = targetClass.getName() + "." + method.getName();
        return pattern.matcher(fullMethodName).matches();
    }
    
    @Override
    public String getExpression() {
        return expression;
    }
    
    public static NameMatchPointcut forClass(Class<?> clazz) {
        return new NameMatchPointcut(clazz.getName() + ".*");
    }
    
    public static NameMatchPointcut forMethod(Class<?> clazz, String methodName) {
        return new NameMatchPointcut(clazz.getName() + "." + methodName);
    }
}
