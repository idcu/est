package ltd.idcu.est.core.api.condition;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.annotation.ConditionalOnClass;

public class OnClassCondition implements Condition {

    @Override
    public boolean matches(Config config, Class<?> beanClass) {
        ConditionalOnClass annotation = beanClass.getAnnotation(ConditionalOnClass.class);
        if (annotation == null) {
            return true;
        }

        for (Class<?> clazz : annotation.value()) {
            if (!isClassPresent(clazz.getName())) {
                return false;
            }
        }

        for (String className : annotation.name()) {
            if (!isClassPresent(className)) {
                return false;
            }
        }

        return true;
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
