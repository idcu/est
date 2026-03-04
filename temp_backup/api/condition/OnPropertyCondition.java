package ltd.idcu.est.core.api.condition;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.annotation.ConditionalOnProperty;

public class OnPropertyCondition implements Condition {

    @Override
    public boolean matches(Config config, Class<?> beanClass) {
        ConditionalOnProperty annotation = beanClass.getAnnotation(ConditionalOnProperty.class);
        if (annotation == null) {
            return true;
        }

        String propertyName = annotation.name();
        String havingValue = annotation.havingValue();
        boolean matchIfMissing = annotation.matchIfMissing();

        if (!config.contains(propertyName)) {
            return matchIfMissing;
        }

        String actualValue = config.getString(propertyName);
        if (havingValue.isEmpty()) {
            return Boolean.parseBoolean(actualValue);
        }

        return havingValue.equals(actualValue);
    }
}
