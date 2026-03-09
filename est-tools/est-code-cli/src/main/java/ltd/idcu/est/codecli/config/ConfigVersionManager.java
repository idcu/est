package ltd.idcu.est.codecli.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigVersionManager {

    private static final String BACKUP_DIR = System.getProperty("user.home") + "/.est/backups";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final Path configPath;

    public ConfigVersionManager(Path configPath) {
        this.configPath = configPath;
    }

    public ConfigVersionManager(String configPath) {
        this(Paths.get(configPath));
    }

    public Path createBackup() throws IOException {
        if (!Files.exists(configPath)) {
            throw new IOException("Config file does not exist: " + configPath);
        }

        Path backupDir = Paths.get(BACKUP_DIR);
        Files.createDirectories(backupDir);

        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String fileName = configPath.getFileName().toString();
        String backupFileName = fileName + "." + timestamp + ".bak";
        Path backupPath = backupDir.resolve(backupFileName);

        Files.copy(configPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        return backupPath;
    }

    public List<Path> listBackups() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupDir)) {
            return new ArrayList<>();
        }

        String baseName = configPath.getFileName().toString();
        try (Stream<Path> stream = Files.list(backupDir)) {
            return stream
                .filter(path -> path.getFileName().toString().startsWith(baseName + "."))
                .filter(path -> path.getFileName().toString().endsWith(".bak"))
                .sorted((a, b) -> b.getFileName().toString().compareTo(a.getFileName().toString()))
                .collect(Collectors.toList());
        }
    }

    public void restoreBackup(Path backupPath) throws IOException {
        if (!Files.exists(backupPath)) {
            throw new IOException("Backup file does not exist: " + backupPath);
        }

        if (Files.exists(configPath)) {
            createBackup();
        }

        Files.createDirectories(configPath.getParent());
        Files.copy(backupPath, configPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteBackup(Path backupPath) throws IOException {
        if (Files.exists(backupPath)) {
            Files.delete(backupPath);
        }
    }

    public void deleteOldBackups(int keepCount) throws IOException {
        List<Path> backups = listBackups();
        if (backups.size() > keepCount) {
            for (int i = keepCount; i < backups.size(); i++) {
                deleteBackup(backups.get(i));
            }
        }
    }

    public Path getLatestBackup() throws IOException {
        List<Path> backups = listBackups();
        if (backups.isEmpty()) {
            return null;
        }
        return backups.get(0);
    }

    public String getBackupTimestamp(Path backupPath) {
        String fileName = backupPath.getFileName().toString();
        String baseName = configPath.getFileName().toString();
        String timestampPart = fileName.substring(baseName.length() + 1, fileName.length() - 4);
        return timestampPart;
    }

    public LocalDateTime parseBackupTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DATE_FORMATTER);
    }
}
