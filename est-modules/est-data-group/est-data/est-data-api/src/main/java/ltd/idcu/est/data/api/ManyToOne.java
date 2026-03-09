package ltd.idcu.est.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    boolean optional() default true;
    FetchType fetch() default FetchType.EAGER;
    CascadeType[] cascade() default {};
    String joinColumn() default "";
    boolean lazy() default false;

    enum FetchType {
        EAGER,
        LAZY
    }

    enum CascadeType {
        ALL,
        PERSIST,
        MERGE,
        REMOVE,
        REFRESH,
        DETACH
    }
}
