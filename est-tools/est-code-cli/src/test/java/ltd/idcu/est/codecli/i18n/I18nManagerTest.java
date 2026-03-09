package ltd.idcu.est.codecli.i18n;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static ltd.idcu.est.test.Assertions.*;

public class I18nManagerTest {

    private I18nManager i18nManager;

    @BeforeEach
    void beforeEach() {
        i18nManager = new I18nManager();
    }

    @Test
    void testDefaultLanguage() {
        assertEquals("zh", i18nManager.getLanguage());
    }

    @Test
    void testGetSupportedLanguages() {
        Set<String> languages = i18nManager.getSupportedLanguages();
        assertNotNull(languages);
        assertTrue(languages.contains("zh"));
        assertTrue(languages.contains("en"));
        assertEquals(2, languages.size());
    }

    @Test
    void testSetLanguage() {
        i18nManager.setLanguage("en");
        assertEquals("en", i18nManager.getLanguage());
        
        i18nManager.setLanguage("zh");
        assertEquals("zh", i18nManager.getLanguage());
    }

    @Test
    void testSetLanguageInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            i18nManager.setLanguage("invalid");
        });
    }

    @Test
    void testGetMessageWithKey() {
        String message = i18nManager.get("nonexistent.key");
        assertNotNull(message);
        assertTrue(message.startsWith("!") && message.endsWith("!"));
    }

    @Test
    void testGetMessageWithArgs() {
        String message = i18nManager.get("nonexistent.key", "arg1", "arg2");
        assertNotNull(message);
        assertTrue(message.startsWith("!") && message.endsWith("!"));
    }

    @Test
    void testHasKeyNonexistent() {
        assertFalse(i18nManager.hasKey("nonexistent.key"));
    }

    @Test
    void testLanguageChangeListener() {
        AtomicReference<String> oldLangRef = new AtomicReference<>();
        AtomicReference<String> newLangRef = new AtomicReference<>();
        
        I18nManager.LanguageChangeListener listener = (oldLang, newLang) -> {
            oldLangRef.set(oldLang);
            newLangRef.set(newLang);
        };
        
        i18nManager.addLanguageChangeListener(listener);
        i18nManager.setLanguage("en");
        
        assertEquals("zh", oldLangRef.get());
        assertEquals("en", newLangRef.get());
    }

    @Test
    void testRemoveLanguageChangeListener() {
        AtomicReference<String> oldLangRef = new AtomicReference<>();
        AtomicReference<String> newLangRef = new AtomicReference<>();
        
        I18nManager.LanguageChangeListener listener = (oldLang, newLang) -> {
            oldLangRef.set(oldLang);
            newLangRef.set(newLang);
        };
        
        i18nManager.addLanguageChangeListener(listener);
        i18nManager.removeLanguageChangeListener(listener);
        
        oldLangRef.set(null);
        newLangRef.set(null);
        
        i18nManager.setLanguage("en");
        
        assertNull(oldLangRef.get());
        assertNull(newLangRef.get());
    }

    @Test
    void testGetAllMessages() {
        Map<String, String> messages = i18nManager.getAllMessages("zh");
        assertNotNull(messages);
    }

    @Test
    void testGetAllMessagesNonexistentLanguage() {
        Map<String, String> messages = i18nManager.getAllMessages("nonexistent");
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void testLanguageChangeNotification() {
        AtomicReference<Boolean> notified = new AtomicReference<>(false);
        
        I18nManager.LanguageChangeListener listener = (oldLang, newLang) -> {
            notified.set(true);
        };
        
        i18nManager.addLanguageChangeListener(listener);
        i18nManager.setLanguage("en");
        
        assertTrue(notified.get());
    }

    @Test
    void testMultipleLanguageChangeListeners() {
        AtomicReference<Boolean> listener1Notified = new AtomicReference<>(false);
        AtomicReference<Boolean> listener2Notified = new AtomicReference<>(false);
        
        I18nManager.LanguageChangeListener listener1 = (oldLang, newLang) -> {
            listener1Notified.set(true);
        };
        
        I18nManager.LanguageChangeListener listener2 = (oldLang, newLang) -> {
            listener2Notified.set(true);
        };
        
        i18nManager.addLanguageChangeListener(listener1);
        i18nManager.addLanguageChangeListener(listener2);
        
        i18nManager.setLanguage("en");
        
        assertTrue(listener1Notified.get());
        assertTrue(listener2Notified.get());
    }
}
