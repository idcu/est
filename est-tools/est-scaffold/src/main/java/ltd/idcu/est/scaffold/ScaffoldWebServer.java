package ltd.idcu.est.scaffold;

public class ScaffoldWebServer {
    
    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("  EST Scaffold - Web Interface Demo");
        System.out.println("=======================================");
        System.out.println();
        System.out.println("Available commands:");
        System.out.println("  1. List project templates");
        System.out.println("  2. List code snippets");
        System.out.println("  3. Generate sample config");
        System.out.println("  4. Exit");
        System.out.println();
        
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in)
            );
            
            while (true) {
                System.out.print("Enter your choice (1-4): ");
                String input = reader.readLine();
                
                if (input == null) break;
                
                switch (input.trim()) {
                    case "1":
                        listProjectTemplates();
                        break;
                    case "2":
                        listCodeSnippets();
                        break;
                    case "3":
                        generateSampleConfig();
                        break;
                    case "4":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void listProjectTemplates() {
        System.out.println();
        System.out.println("Available Project Templates:");
        System.out.println("============================");
        for (ProjectType type : ProjectType.values()) {
            System.out.println();
            System.out.println("  Type: " + type.getCommand());
            System.out.println("  Name: " + type.getDescription());
            System.out.println("  Desc: " + type.getChineseDescription());
        }
    }
    
    private static void listCodeSnippets() {
        System.out.println();
        System.out.println("Available Code Snippets:");
        System.out.println("=========================");
        for (String name : CodeSnippetGenerator.listTemplates()) {
            CodeSnippetGenerator.CodeTemplate template = CodeSnippetGenerator.getTemplate(name);
            System.out.println();
            System.out.println("  ID: " + name);
            System.out.println("  Name: " + template.name);
            System.out.println("  Desc: " + template.description);
            System.out.println("  Vars: " + template.variables);
        }
    }
    
    private static void generateSampleConfig() {
        System.out.println();
        System.out.println("Generating Sample Configuration...");
        System.out.println("==================================");
        
        ProjectConfig config = new ProjectConfig("demo-project");
        config.setGroupId("com.example");
        config.setPackageName("demo");
        
        System.out.println();
        System.out.println("Sample POM.xml:");
        System.out.println("---------------");
        System.out.println(ConfigGenerator.generatePomXml(config, ProjectType.WEB));
        
        System.out.println();
        System.out.println("Sample application.yml:");
        System.out.println("-----------------------");
        System.out.println(ConfigGenerator.generateApplicationYml(config, ProjectType.WEB));
        
        System.out.println();
        System.out.println("Sample README.md:");
        System.out.println("-----------------");
        System.out.println(ConfigGenerator.generateReadme(config, ProjectType.WEB));
    }
}
