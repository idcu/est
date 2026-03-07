package ltd.idcu.est.circuitbreaker.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CircuitBreakerAnnotation {
    String name() default "";

    int failureThreshold() default 5;

    int timeout() default 1000;

    int waitDuration() default 5000;

    int successThreshold() default 2;

    String fallbackMethod() default "";
}
