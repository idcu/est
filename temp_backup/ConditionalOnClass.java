package ltd.idcu.est.core.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ltd.idcu.est.core.api.condition.OnClassCondition.class)
public @interface ConditionalOnClass {
    String[] name() default {};

    Class<?>[] value() default {};
}
