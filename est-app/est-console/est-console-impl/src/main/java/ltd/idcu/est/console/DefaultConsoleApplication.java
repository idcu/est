package ltd.idcu.est.console;

import ltd.idcu.est.console.api.Command;
import ltd.idcu.est.console.api.ConsoleApplication;
import ltd.idcu.est.console.api.ConsoleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConsoleApplication implements ConsoleApplication {
    
    private String name;
    private volatile boolean running;
    private final Map<String, Command> commands;
    
    public DefaultConsoleApplication() {
        this.commands = new HashMap<>();
        this.running = false;
        this.name = "EST Console Application";
    }
    
    @Override
    public void run(String[] args) {
        if (running) {
            throw new ConsoleException("Application is already running");
        }
        
        running = true;
        System.out.println(name + " started");
        
        try {
            if (args.length > 0) {
                executeCommand(args);
            } else {
                runInteractiveMode();
            }
        } finally {
            running = false;
        }
    }
    
    @Override
    public void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        System.out.println(name + " stopped");
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }
    
    private void executeCommand(String[] args) {
        String commandName = args[0];
        Command command = commands.get(commandName);
        
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            printHelp();
            return;
        }
        
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);
        
        try {
            command.execute(commandArgs);
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void runInteractiveMode() {
        System.out.println("Entering interactive mode. Type 'help' for available commands, 'exit' to quit.");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            if ("exit".equalsIgnoreCase(line) || "quit".equalsIgnoreCase(line)) {
                stop();
                break;
            }
            
            if ("help".equalsIgnoreCase(line)) {
                printHelp();
                continue;
            }
            
            String[] parts = line.split("\\s+");
            executeCommand(parts);
        }
        
        scanner.close();
    }
    
    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help  - Show this help message");
        System.out.println("  exit  - Exit the application");
        
        for (Command command : commands.values()) {
            System.out.println("  " + command.getName() + " - " + command.getDescription());
        }
    }
}
