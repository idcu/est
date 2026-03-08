package ltd.idcu.est.scaffold;

import java.io.IOException;
import java.util.Map;

public class ProjectConfig {
    private String groupId = "com.example";
    private String artifactId;
    private String version = "1.0.0-SNAPSHOT";
    private String packageName;
    private String description;
    private String estVersion = "2.1.0";
    private int javaVersion = 21;
    private boolean gitInit = false;
    private boolean dryRun = false;
    private boolean docker = false;
    private String preset = null;
    private boolean ciGithub = false;
    private boolean ciGitlab = false;
    private boolean ciJenkins = false;

    public ProjectConfig(String projectName) {
        this.artifactId = projectName;
        this.packageName = projectName.replace("-", "").toLowerCase();
    }

    public ProjectConfig(String projectName, boolean loadDefaults) throws IOException {
        this(projectName);
        if (loadDefaults) {
            loadFromConfigFile();
        }
    }

    public void loadFromConfigFile() throws IOException {
        Map<String, String> config = ConfigFileParser.loadConfig();
        Map<String, String> defaults = ConfigFileParser.getDefaultConfig(config);
        
        if (defaults.containsKey("groupId")) {
            this.groupId = defaults.get("groupId");
        }
        if (defaults.containsKey("version")) {
            this.version = defaults.get("version");
        }
        if (defaults.containsKey("javaVersion")) {
            try {
                this.javaVersion = Integer.parseInt(defaults.get("javaVersion"));
            } catch (NumberFormatException e) {
            }
        }
    }

    public void applyPreset(String presetName) throws IOException {
        Map<String, String> config = ConfigFileParser.loadConfig();
        Map<String, String> preset = ConfigFileParser.getPreset(config, presetName);
        
        if (preset.containsKey("groupId")) {
            this.groupId = preset.get("groupId");
        }
        if (preset.containsKey("version")) {
            this.version = preset.get("version");
        }
        if (preset.containsKey("javaVersion")) {
            try {
                this.javaVersion = Integer.parseInt(preset.get("javaVersion"));
            } catch (NumberFormatException e) {
            }
        }
        this.preset = presetName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstVersion() {
        return estVersion;
    }

    public void setEstVersion(String estVersion) {
        this.estVersion = estVersion;
    }

    public int getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(int javaVersion) {
        this.javaVersion = javaVersion;
    }

    public boolean isGitInit() {
        return gitInit;
    }

    public void setGitInit(boolean gitInit) {
        this.gitInit = gitInit;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public boolean isDocker() {
        return docker;
    }

    public void setDocker(boolean docker) {
        this.docker = docker;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public boolean isCiGithub() {
        return ciGithub;
    }

    public void setCiGithub(boolean ciGithub) {
        this.ciGithub = ciGithub;
    }

    public boolean isCiGitlab() {
        return ciGitlab;
    }

    public void setCiGitlab(boolean ciGitlab) {
        this.ciGitlab = ciGitlab;
    }

    public boolean isCiJenkins() {
        return ciJenkins;
    }

    public void setCiJenkins(boolean ciJenkins) {
        this.ciJenkins = ciJenkins;
    }

    public String getPackagePath() {
        return groupId.replace(".", "/") + "/" + packageName;
    }

    public String getFullPackageName() {
        return groupId + "." + packageName;
    }
}
