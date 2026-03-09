package ltd.idcu.est.codecli.config;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class ConfigVersionManagerTest {

    private Path tempConfigPath;
    private Path backupDir;
    private ConfigVersionManager versionManager;

    @BeforeEach
    void beforeEach() throws IOException {
        tempConfigPath = Files.createTempFile("est-code-cli-test", ".yml");
        Files.writeString(tempConfigPath, "nickname: Test\nworkDir: .\n");
        
        Path backupDir = Paths.get(System.getProperty("user.home"), ".est", "backups");
        Files.createDirectories(backupDir);
        
        versionManager = new ConfigVersionManager(tempConfigPath);
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(tempConfigPath)) {
            Files.delete(tempConfigPath);
        }
        
        List<Path> backups = versionManager.listBackups();
        for (Path backup : backups) {
            Files.deleteIfExists(backup);
        }
    }

    @Test
    void testCreateBackup() throws IOException {
        Path backupPath = versionManager.createBackup();
        assertNotNull(backupPath);
        assertTrue(Files.exists(backupPath));
        
        String backupContent = Files.readString(backupPath);
        assertTrue(backupContent.contains("nickname: Test"));
    }

    @Test
    void testCreateBackupFileNotExists() {
        Path nonExistentPath = tempConfigPath.getParent().resolve("non-existent.yml");
        ConfigVersionManager nonExistentManager = new ConfigVersionManager(nonExistentPath);
        
        IOException exception = null;
        try {
            nonExistentManager.createBackup();
        } catch (IOException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertNotNull(exception.getMessage().contains("Config file does not exist"));
    }

    @Test
    void testListBackups() throws IOException {
        versionManager.createBackup();
        versionManager.createBackup();
        versionManager.createBackup();
        
        List<Path> backups = versionManager.listBackups();
        assertEquals(3, backups.size();
    }

    @Test
    void testListBackupsEmpty() throws IOException {
        List<Path> backups = versionManager.listBackups();
        assertTrue(backups.isEmpty();
    }

    @Test
    void testRestoreBackup() throws IOException {
        Path backupPath = versionManager.createBackup();
        
        Files.writeString(tempConfigPath, "nickname: Modified\n");
        
        versionManager.restoreBackup(backupPath);
        
        String restoredContent = Files.readString(tempConfigPath);
        assertTrue(restoredContent.contains("nickname: Test"));
    }

    @Test
    void testRestoreBackupCreatesBackupBeforeRestore() throws IOException {
        Path backup1 = versionManager.createBackup();
        
        Files.writeString(tempConfigPath, "nickname: Modified1\n");
        Path backup2 = versionManager.createBackup();
        
        Files.writeString(tempConfigPath, "nickname: Modified2\n");
        
        versionManager.restoreBackup(backup1);
        
        List<Path> backups = versionManager.listBackups();
        assertEquals(3, backups.size();
    }

    @Test
    void testRestoreBackupNotExists() {
        Path nonExistentBackup = tempConfigPath.getParent().resolve("non-existent.bak");
        
        IOException exception = null;
        try {
            versionManager.restoreBackup(nonExistentBackup);
        } catch (IOException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertNotNull(exception.getMessage().contains("Backup file does not exist"));
    }

    @Test
    void testDeleteBackup() throws IOException {
        Path backupPath = versionManager.createBackup();
        assertTrue(Files.exists(backupPath));
        
        versionManager.deleteBackup(backupPath);
        assertFalse(Files.exists(backupPath));
    }

    @Test
    void testDeleteOldBackups() throws IOException {
        for (int i = 0; i < 5; i++) {
            versionManager.createBackup();
        }
        
        versionManager.deleteOldBackups(3);
        
        List<Path> backups = versionManager.listBackups();
        assertEquals(3, backups.size();
    }

    @Test
    void testGetLatestBackup() throws IOException {
        Path backup1 = versionManager.createBackup();
        Path backup2 = versionManager.createBackup();
        
        Path latest = versionManager.getLatestBackup();
        assertNotNull(latest);
        
        List<Path> backups = versionManager.listBackups();
        assertEquals(backups.get(0), latest);
    }

    @Test
    void testGetLatestBackupEmpty() throws IOException {
        Path latest = versionManager.getLatestBackup();
        assertNull(latest);
    }

    @Test
    void testGetBackupTimestamp() throws IOException {
        Path backupPath = versionManager.createBackup();
        String timestamp = versionManager.getBackupTimestamp(backupPath);
        assertNotNull(timestamp);
        assertTrue(timestamp.matches("\\d{8}_\\d{6}"));
    }

    @Test
    void testParseBackupTimestamp() throws IOException {
        Path backupPath = versionManager.createBackup();
        String timestamp = versionManager.getBackupTimestamp(backupPath);
        LocalDateTime dateTime = versionManager.parseBackupTimestamp(timestamp);
        assertNotNull(dateTime);
    }
}
