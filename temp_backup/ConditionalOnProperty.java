package ltd.idcu.est.core.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ltd.idcu.est.core.api.condition.OnPropertyCondition.class)
public @interface ConditionalOnProperty {
    String name();

    String havingValue() default "";

    boolean matchIfMissing() default false;
}
