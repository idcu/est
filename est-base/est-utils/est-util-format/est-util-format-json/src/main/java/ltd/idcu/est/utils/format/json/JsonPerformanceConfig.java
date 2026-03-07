package ltd.idcu.est.utils.format.json;

public class JsonPerformanceConfig {
    
    private static boolean useAsmGeneratorEnabled = false;
    private static boolean fieldCacheEnabled = true;
    private static int stringBuilderPoolSize = 256;
    private static int prettyPrintIndent = 2;
    
    private JsonPerformanceConfig() {
    }
    
    public static boolean isAsmGeneratorEnabled() {
        return useAsmGeneratorEnabled;
    }
    
    public static void setAsmGeneratorEnabled(boolean enabled) {
        useAsmGeneratorEnabled = enabled;
    }
    
    public static boolean isFieldCacheEnabled() {
        return fieldCacheEnabled;
    }
    
    public static void setFieldCacheEnabled(boolean enabled) {
        fieldCacheEnabled = enabled;
    }
    
    public static int getStringBuilderPoolSize() {
        return stringBuilderPoolSize;
    }
    
    public static void setStringBuilderPoolSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Pool size must be positive");
        }
        stringBuilderPoolSize = size;
    }
    
    public static int getPrettyPrintIndent() {
        return prettyPrintIndent;
    }
    
    public static void setPrettyPrintIndent(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("Indent must be non-negative");
        }
        prettyPrintIndent = indent;
    }
}
