package ltd.idcu.est.scaffold;

public enum ProjectType {
    BASIC("basic", "Basic EST Application", "最简单的EST应用"),
    WEB("web", "Web Application with EST Framework", "Web应用，包含路由和静态文件"),
    API("api", "REST API with EST Framework", "RESTful API应用"),
    CLI("cli", "Command Line Tool", "命令行工具"),
    LIBRARY("library", "Library Project", "库项目"),
    PLUGIN("plugin", "EST Plugin Project", "EST插件"),
    MICROSERVICE("microservice", "Microservice Project", "微服务应用"),
    FULLSTACK("fullstack", "Full-Stack Web Application", "全栈Web应用"),
    AI_ENHANCED("ai-enhanced", "AI-Enhanced Application", "AI增强应用"),
    REALTIME("realtime", "Real-Time Application", "实时应用（WebSocket）"),
    CACHE_APP("cache", "Caching Application", "缓存应用"),
    EVENT_DRIVEN("event", "Event-Driven Application", "事件驱动应用"),
    SECURE_APP("secure", "Secure Application", "安全应用（认证授权）");

    private final String command;
    private final String description;
    private final String chineseDescription;

    ProjectType(String command, String description, String chineseDescription) {
        this.command = command;
        this.description = description;
        this.chineseDescription = chineseDescription;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getChineseDescription() {
        return chineseDescription;
    }

    public static ProjectType fromCommand(String command) {
        for (ProjectType type : values()) {
            if (type.command.equalsIgnoreCase(command)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown project type: " + command);
    }
}
