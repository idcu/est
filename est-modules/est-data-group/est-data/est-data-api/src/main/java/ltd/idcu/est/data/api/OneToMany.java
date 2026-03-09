package ltd.idcu.est.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
    String mappedBy() default "";
    boolean orphanRemoval() default false;
    FetchType fetch() default FetchType.LAZY;
    CascadeType[] cascade() default {};
    boolean lazy() default true;

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
