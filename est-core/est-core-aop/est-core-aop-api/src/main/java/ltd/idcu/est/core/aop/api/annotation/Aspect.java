package ltd.idcu.est.core.aop.api.annotation;

import ltd.idcu.est.core.container.api.annotation.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Aspect {
    int order() default 0;
}
