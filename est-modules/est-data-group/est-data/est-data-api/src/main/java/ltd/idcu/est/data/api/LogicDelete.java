package ltd.idcu.est.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicDelete {
    String value() default "";
    String deletedValue() default "1";
    String notDeletedValue() default "0";
}
