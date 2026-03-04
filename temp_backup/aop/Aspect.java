package ltd.idcu.est.core.api.annotation.aop;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    
    int order() default 0;
}
