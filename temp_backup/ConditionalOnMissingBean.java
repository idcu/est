package ltd.idcu.est.core.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ltd.idcu.est.core.api.condition.OnMissingBeanCondition.class)
public @interface ConditionalOnMissingBean {
    Class<?>[] value() default {};

    String[] name() default {};
}
