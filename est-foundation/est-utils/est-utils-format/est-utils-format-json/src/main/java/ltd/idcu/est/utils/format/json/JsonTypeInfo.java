package ltd.idcu.est.utils.format.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTypeInfo {
    Id use() default Id.CLASS;
    As include() default As.PROPERTY;
    String property() default "@class";
    boolean visible() default false;
    
    enum Id {
        CLASS,
        MINIMAL_CLASS,
        NAME,
        CUSTOM,
        NONE
    }
    
    enum As {
        PROPERTY,
        WRAPPER_OBJECT,
        WRAPPER_ARRAY,
        EXTERNAL_PROPERTY,
        EXISTING_PROPERTY
    }
}
