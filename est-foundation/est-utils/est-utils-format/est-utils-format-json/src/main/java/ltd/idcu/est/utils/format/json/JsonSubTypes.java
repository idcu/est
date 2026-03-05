package ltd.idcu.est.utils.format.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSubTypes {
    Type[] value();
    
    @interface Type {
        Class<?> value();
        String name() default "";
    }
}
