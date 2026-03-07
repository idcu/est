package ltd.idcu.est.cli;

import ltd.idcu.est.cli.ide.ESTAnnotationRegistry;
import ltd.idcu.est.cli.ide.ESTCodeAnalyzer;
import ltd.idcu.est.cli.ide.ESTProjectScanner;
import ltd.idcu.est.cli.ide.ESTTemplateRegistry;
import ltd.idcu.est.codegen.CodeGeneratorMain;

public class EstCliMain {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];
        String[] remainingArgs = new String[args.length - 1];
        System.arraycopy(args, 1, remainingArgs, 0, remainingArgs.length);

        try {
            switch (command) {
                case "codegen":
                    CodeGeneratorMain.main(remainingArgs);
                    break;
                case "ide":
                    handleIdeCommands(remainingArgs);
                    break;
                case "help":
                default:
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void handleIdeCommands(String[] args) {
        if (args.length == 0) {
            printIdeUsage();
            return;
        }

        String subCommand = args[0];
        switch (subCommand) {
            case "scan":
                scanProject(args);
                break;
            case "analyze":
                analyzeCode(args);
                break;
            case "templates":
                listTemplates();
                break;
            case "annotations":
                listAnnotations();
                break;
            default:
                printIdeUsage();
        }
    }

    private static void scanProject(String[] args) {
        String projectPath = args.length > 1 ? args[1] : ".";
        System.out.println("Scanning project at: " + projectPath);
        ESTProjectScanner scanner = new ESTProjectScanner();
        scanner.scan(projectPath);
        System.out.println("Project scan completed!");
    }

    private static void analyzeCode(String[] args) {
        String filePath = args.length > 1 ? args[1] : null;
        if (filePath == null) {
            System.err.println("Please specify a file to analyze");
            return;
        }
        System.out.println("Analyzing file: " + filePath);
        ESTCodeAnalyzer analyzer = new ESTCodeAnalyzer();
        analyzer.analyze(filePath);
        System.out.println("Code analysis completed!");
    }

    private static void listTemplates() {
        System.out.println("Available code templates:");
        ESTTemplateRegistry registry = new ESTTemplateRegistry();
        registry.getTemplates().forEach(template -> 
            System.out.println("  - " + template.getName() + ": " + template.getDescription())
        );
    }

    private static void listAnnotations() {
        System.out.println("Available EST annotations:");
        ESTAnnotationRegistry registry = new ESTAnnotationRegistry();
        registry.getAnnotations().forEach(annotation -> 
            System.out.println("  - " + annotation.getName() + ": " + annotation.getDescription())
        );
    }

    private static void printUsage() {
        System.out.println("EST Framework CLI");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  est <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  codegen    Code generation tools (database, POJO, etc.)");
        System.out.println("  ide        IDE integration and analysis tools");
        System.out.println("  help       Show this help message");
        System.out.println();
        System.out.println("For more information about a command, use:");
        System.out.println("  est <command>");
    }

    private static void printIdeUsage() {
        System.out.println("EST IDE Tools");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  est ide <subcommand> [options]");
        System.out.println();
        System.out.println("Subcommands:");
        System.out.println("  scan [path]      Scan EST project structure");
        System.out.println("  analyze <file>   Analyze EST code file");
        System.out.println("  templates         List available code templates");
        System.out.println("  annotations       List EST framework annotations");
    }
}
