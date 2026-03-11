package ltd.idcu.est.audit.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogAnnotation {
    String module() default "";
    String operation() default "";
    String resource() default "";
    String resourceId() default "";
    String description() default "";
    boolean saveParams() default true;
    boolean saveResult() default false;
    boolean async() default true;
    String[] ignoreParams() default {};
}
