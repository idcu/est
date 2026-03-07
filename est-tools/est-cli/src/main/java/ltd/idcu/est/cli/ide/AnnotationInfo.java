package ltd.idcu.est.cli.ide;

import java.util.ArrayList;
import java.util.List;

public class AnnotationInfo {
    private final String name;
    private final String qualifiedName;
    private final String description;
    private final List<AnnotationAttribute> attributes;

    public static class AnnotationAttribute {
        private final String name;
        private final String type;
        private final String defaultValue;
        private final boolean required;
        private final String description;

        public AnnotationAttribute(String name, String type, String defaultValue, boolean required, String description) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.required = required;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public boolean isRequired() {
            return required;
        }

        public String getDescription() {
            return description;
        }
    }

    public AnnotationInfo(String name, String qualifiedName, String description) {
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.description = description;
        this.attributes = new ArrayList<>();
    }

    public AnnotationInfo addAttribute(String name, String type, String defaultValue, boolean required, String description) {
        attributes.add(new AnnotationAttribute(name, type, defaultValue, required, description));
        return this;
    }

    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getDescription() {
        return description;
    }

    public List<AnnotationAttribute> getAttributes() {
        return new ArrayList<>(attributes);
    }
}
