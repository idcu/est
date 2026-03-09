package ltd.idcu.est.codecli.config;

import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;
import ltd.idcu.est.test.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ltd.idcu.est.test.Assertions.*;

public class CliConfigTest {

    private Path tempConfigPath;

    @BeforeEach
    void beforeEach() throws IOException {
        tempConfigPath = Files.createTempFile("est-code-cli-test", ".yml");
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(tempConfigPath)) {
            Files.delete(tempConfigPath);
        }
    }

    @Test
    void testDefaultConfig() {
        CliConfig config = new CliConfig();
        assertEquals("EST", config.getNickname());
        assertNotNull(config.getWorkDir());
        assertTrue(config.isPlanningMode());
        assertTrue(config.isHitlEnabled());
    }

    @Test
    void testSettersAndGetters() {
        CliConfig config = new CliConfig();
        
        config.setNickname("TestBot");
        assertEquals("TestBot", config.getNickname());
        
        config.setWorkDir("/test/path");
        assertEquals("/test/path", config.getWorkDir());
        
        config.setPlanningMode(false);
        assertFalse(config.isPlanningMode());
        
        config.setHitlEnabled(false);
        assertFalse(config.isHitlEnabled());
        
        config.setChatModelApiUrl("https://api.test.com");
        assertEquals("https://api.test.com", config.getChatModelApiUrl());
        
        config.setChatModelApiKey("test-key");
        assertEquals("test-key", config.getChatModelApiKey());
        
        config.setChatModelName("gpt-4");
        assertEquals("gpt-4", config.getChatModelName());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        CliConfig original = new CliConfig();
        original.setNickname("SaveTest");
        original.setWorkDir("/save/test");
        original.setPlanningMode(false);
        original.setHitlEnabled(false);
        original.setChatModelApiUrl("https://save.api.com");
        original.setChatModelApiKey("save-key");
        original.setChatModelName("save-model");

        original.saveTo(tempConfigPath);

        assertTrue(Files.exists(tempConfigPath));

        String content = Files.readString(tempConfigPath);
        assertTrue(content.contains("SaveTest"));
        assertTrue(content.contains("/save/test"));
        assertTrue(content.contains("planningMode: false"));
        assertTrue(content.contains("hitlEnabled: false"));
        assertTrue(content.contains("https://save.api.com"));
        assertTrue(content.contains("save-key"));
        assertTrue(content.contains("save-model"));
    }

    @Test
    void testSaveWithoutChatModel() throws IOException {
        CliConfig config = new CliConfig();
        config.setNickname("SimpleTest");
        
        config.saveTo(tempConfigPath);
        
        String content = Files.readString(tempConfigPath);
        assertTrue(content.contains("SimpleTest"));
        assertFalse(content.contains("chatModel"));
    }
}
