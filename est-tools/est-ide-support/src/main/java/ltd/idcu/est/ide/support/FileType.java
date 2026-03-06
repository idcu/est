package ltd.idcu.est.ide.support;

public enum FileType {
    JAVA(".java"),
    XML(".xml"),
    YAML(".yaml"),
    YML(".yml"),
    JSON(".json"),
    PROPERTIES(".properties"),
    MD(".md"),
    POM_XML("pom.xml"),
    EST_CONFIG(".est.yml");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType fromFileName(String fileName) {
        if (fileName.equals("pom.xml")) {
            return POM_XML;
        }
        for (FileType type : values()) {
            if (fileName.toLowerCase().endsWith(type.extension.toLowerCase())) {
                return type;
            }
        }
        return null;
    }
}
