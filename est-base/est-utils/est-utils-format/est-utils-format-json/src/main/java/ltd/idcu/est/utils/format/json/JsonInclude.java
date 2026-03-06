package ltd.idcu.est.utils.format.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonInclude {
    Include value() default Include.ALWAYS;

    enum Include {
        ALWAYS,
        NON_NULL,
        NON_EMPTY,
        NON_DEFAULT
    }
}
