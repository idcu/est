package ltd.idcu.est.config.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfigVersionManager {
    String createVersion(Map<String, Object> properties, String comment);

    Optional<ConfigVersion> getVersion(String versionId);

    List<ConfigVersion> listVersions();

    List<ConfigVersion> listVersions(int limit);

    void rollbackTo(String versionId);

    Optional<ConfigVersion> getCurrentVersion();

    void deleteVersion(String versionId);

    void clearVersions();
}
