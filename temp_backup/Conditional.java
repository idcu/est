package ltd.idcu.est.core.api.annotation;

import ltd.idcu.est.core.api.condition.Condition;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
    Class<? extends Condition>[] value();
}
