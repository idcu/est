package ltd.idcu.est.codecli.i18n;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class I18nManager {
    
    private static final String DEFAULT_LANGUAGE = "zh";
    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("zh", "en");
    
    private final Map<String, ResourceBundle> bundles;
    private String currentLanguage;
    private final List<LanguageChangeListener> listeners;
    
    @FunctionalInterface
    public interface LanguageChangeListener {
        void onLanguageChanged(String oldLanguage, String newLanguage);
    }
    
    public I18nManager() {
        this.bundles = new ConcurrentHashMap<>();
        this.currentLanguage = DEFAULT_LANGUAGE;
        this.listeners = new ArrayList<>();
        loadDefaultBundles();
    }
    
    private void loadDefaultBundles() {
        for (String lang : SUPPORTED_LANGUAGES) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(
                    "messages",
                    new Locale(lang),
                    getClass().getClassLoader()
                );
                bundles.put(lang, bundle);
            } catch (MissingResourceException e) {
                System.err.println("未找到语言包: " + lang);
            }
        }
        
        if (bundles.isEmpty()) {
            loadFallbackBundle();
        }
    }
    
    private void loadFallbackBundle() {
        ResourceBundle fallback = new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                return new Object[][]{
                    {"error.unknown", "未知错误"},
                    {"help.title", "帮助"},
                    {"config.title", "配置"}
                };
            }
        };
        bundles.put(DEFAULT_LANGUAGE, fallback);
    }
    
    public void setLanguage(String language) {
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException("不支持的语言: " + language);
        }
        
        String oldLanguage = this.currentLanguage;
        this.currentLanguage = language;
        
        listeners.forEach(listener -> listener.onLanguageChanged(oldLanguage, language));
    }
    
    public String getLanguage() {
        return currentLanguage;
    }
    
    public Set<String> getSupportedLanguages() {
        return Collections.unmodifiableSet(SUPPORTED_LANGUAGES);
    }
    
    public String get(String key) {
        ResourceBundle bundle = bundles.get(currentLanguage);
        if (bundle == null) {
            bundle = bundles.get(DEFAULT_LANGUAGE);
        }
        
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        
        return "!" + key + "!";
    }
    
    public String get(String key, Object... args) {
        String pattern = get(key);
        if (pattern.startsWith("!") && pattern.endsWith("!")) {
            return pattern;
        }
        return MessageFormat.format(pattern, args);
    }
    
    public boolean hasKey(String key) {
        ResourceBundle bundle = bundles.get(currentLanguage);
        if (bundle != null && bundle.containsKey(key)) {
            return true;
        }
        bundle = bundles.get(DEFAULT_LANGUAGE);
        return bundle != null && bundle.containsKey(key);
    }
    
    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeLanguageChangeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }
    
    public void registerBundle(String language, ResourceBundle bundle) {
        bundles.put(language, bundle);
    }
    
    public Map<String, String> getAllMessages(String language) {
        ResourceBundle bundle = bundles.get(language);
        if (bundle == null) {
            return Collections.emptyMap();
        }
        
        Map<String, String> messages = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            messages.put(key, bundle.getString(key));
        }
        return messages;
    }
}
