package ltd.idcu.est.codecli.config;

import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assertions.*;

public class ConfigValidatorTest {
    
    @Test
    void testValidConfig() {
        CliConfig config = new CliConfig();
        config.setNickname("ValidBot");
        config.setWorkDir(System.getProperty("java.io.tmpdir"));
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertTrue(result.isValid());
        assertFalse(result.hasErrors());
        assertFalse(result.hasWarnings());
    }
    
    @Test
    void testInvalidNickname() {
        CliConfig config = new CliConfig();
        config.setNickname("");
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("昵称")));
    }
    
    @Test
    void testInvalidNicknameSpecialChars() {
        CliConfig config = new CliConfig();
        config.setNickname("Invalid@#$%");
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
    }
    
    @Test
    void testInvalidWorkDir() {
        CliConfig config = new CliConfig();
        config.setWorkDir("/this/path/does/not/exist/ever/12345");
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("工作目录")));
    }
    
    @Test
    void testNullWorkDir() {
        CliConfig config = new CliConfig();
        config.setWorkDir(null);
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
    }
    
    @Test
    void testMissingChatModel() {
        CliConfig config = new CliConfig();
        config.setNickname("TestBot");
        config.setWorkDir(System.getProperty("java.io.tmpdir"));
        config.setChatModelApiUrl(null);
        config.setChatModelApiKey(null);
        config.setChatModelName(null);
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("LLM")));
    }
    
    @Test
    void testPartialChatModel() {
        CliConfig config = new CliConfig();
        config.setNickname("TestBot");
        config.setWorkDir(System.getProperty("java.io.tmpdir"));
        config.setChatModelApiUrl("https://api.test.com");
        config.setChatModelApiKey(null);
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
    }
    
    @Test
    void testCompleteChatModel() {
        CliConfig config = new CliConfig();
        config.setNickname("TestBot");
        config.setWorkDir(System.getProperty("java.io.tmpdir"));
        config.setChatModelApiUrl("https://api.test.com");
        config.setChatModelApiKey("test-key");
        config.setChatModelName("gpt-4");
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertTrue(result.isValid());
        assertFalse(result.hasWarnings());
    }
    
    @Test
    void testMultipleErrors() {
        CliConfig config = new CliConfig();
        config.setNickname("");
        config.setWorkDir("/invalid/path/that/never/exists");
        
        ConfigValidator.ValidationResult result = ConfigValidator.validate(config);
        
        assertFalse(result.isValid());
        assertTrue(result.getErrors().size() >= 2);
    }
    
    @Test
    void testValidationResultGetters() {
        ConfigValidator.ValidationResult result = new ConfigValidator.ValidationResult();
        
        result.addError("Error 1");
        result.addError("Error 2");
        result.addWarning("Warning 1");
        
        assertEquals(2, result.getErrors().size());
        assertEquals(1, result.getWarnings().size());
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
        assertTrue(result.hasWarnings());
    }
}
