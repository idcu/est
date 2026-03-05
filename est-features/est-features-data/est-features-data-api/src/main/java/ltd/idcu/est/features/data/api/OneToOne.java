package ltd.idcu.est.features.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToOne {
    String joinColumn() default "";
    
    String mappedBy() default "";
    
    boolean lazy() default true;
}
