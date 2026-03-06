package ltd.idcu.est.dbgenerator.config;

import java.util.ArrayList;
import java.util.List;

public class GeneratorConfig {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private String packageName;
    private String outputDir;
    private List<String> includeTables = new ArrayList<>();
    private List<String> excludeTables = new ArrayList<>();
    private boolean generateEntity = true;
    private boolean generateRepository = true;
    private boolean generateService = true;
    private boolean generateController = true;
    private boolean generateDto = true;
    private String entityPackage;
    private String repositoryPackage;
    private String servicePackage;
    private String controllerPackage;
    private String dtoPackage;
    private boolean useLombok = false;
    private boolean useSwagger = false;
    private boolean useMybatisPlus = false;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public List<String> getIncludeTables() {
        return includeTables;
    }

    public void setIncludeTables(List<String> includeTables) {
        this.includeTables = includeTables;
    }

    public void addIncludeTable(String tableName) {
        this.includeTables.add(tableName);
    }

    public List<String> getExcludeTables() {
        return excludeTables;
    }

    public void setExcludeTables(List<String> excludeTables) {
        this.excludeTables = excludeTables;
    }

    public void addExcludeTable(String tableName) {
        this.excludeTables.add(tableName);
    }

    public boolean isGenerateEntity() {
        return generateEntity;
    }

    public void setGenerateEntity(boolean generateEntity) {
        this.generateEntity = generateEntity;
    }

    public boolean isGenerateRepository() {
        return generateRepository;
    }

    public void setGenerateRepository(boolean generateRepository) {
        this.generateRepository = generateRepository;
    }

    public boolean isGenerateService() {
        return generateService;
    }

    public void setGenerateService(boolean generateService) {
        this.generateService = generateService;
    }

    public boolean isGenerateController() {
        return generateController;
    }

    public void setGenerateController(boolean generateController) {
        this.generateController = generateController;
    }

    public boolean isGenerateDto() {
        return generateDto;
    }

    public void setGenerateDto(boolean generateDto) {
        this.generateDto = generateDto;
    }

    public String getEntityPackage() {
        return entityPackage != null ? entityPackage : (packageName != null ? packageName + ".entity" : null);
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getRepositoryPackage() {
        return repositoryPackage != null ? repositoryPackage : (packageName != null ? packageName + ".repository" : null);
    }

    public void setRepositoryPackage(String repositoryPackage) {
        this.repositoryPackage = repositoryPackage;
    }

    public String getServicePackage() {
        return servicePackage != null ? servicePackage : (packageName != null ? packageName + ".service" : null);
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getControllerPackage() {
        return controllerPackage != null ? controllerPackage : (packageName != null ? packageName + ".controller" : null);
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getDtoPackage() {
        return dtoPackage != null ? dtoPackage : (packageName != null ? packageName + ".dto" : null);
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackage = dtoPackage;
    }

    public boolean isUseLombok() {
        return useLombok;
    }

    public void setUseLombok(boolean useLombok) {
        this.useLombok = useLombok;
    }

    public boolean isUseSwagger() {
        return useSwagger;
    }

    public void setUseSwagger(boolean useSwagger) {
        this.useSwagger = useSwagger;
    }

    public boolean isUseMybatisPlus() {
        return useMybatisPlus;
    }

    public void setUseMybatisPlus(boolean useMybatisPlus) {
        this.useMybatisPlus = useMybatisPlus;
    }

    public boolean shouldGenerateTable(String tableName) {
        if (!excludeTables.isEmpty() && excludeTables.contains(tableName)) {
            return false;
        }
        if (!includeTables.isEmpty()) {
            return includeTables.contains(tableName);
        }
        return true;
    }
}
