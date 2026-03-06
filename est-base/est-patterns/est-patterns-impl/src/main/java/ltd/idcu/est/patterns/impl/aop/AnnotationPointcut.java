package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.Pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationPointcut implements Pointcut {
    
    private final Class<? extends Annotation> classAnnotation;
    private final Class<? extends Annotation> methodAnnotation;
    private final String expression;
    
    public AnnotationPointcut(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation) {
        this.classAnnotation = classAnnotation;
        this.methodAnnotation = methodAnnotation;
        this.expression = buildExpression();
    }
    
    public AnnotationPointcut(Class<? extends Annotation> methodAnnotation) {
        this(null, methodAnnotation);
    }
    
    private String buildExpression() {
        StringBuilder sb = new StringBuilder("@");
        if (classAnnotation != null) {
            sb.append(classAnnotation.getSimpleName());
        }
        if (methodAnnotation != null) {
            if (classAnnotation != null) {
                sb.append(" + ");
            }
            sb.append("@").append(methodAnnotation.getSimpleName());
        }
        return sb.toString();
    }
    
    @Override
    public boolean matches(Class<?> targetClass, Method method) {
        boolean classMatch = classAnnotation == null || targetClass.isAnnotationPresent(classAnnotation);
        boolean methodMatch = methodAnnotation == null || method.isAnnotationPresent(methodAnnotation);
        return classMatch && methodMatch;
    }
    
    @Override
    public String getExpression() {
        return expression;
    }
}
