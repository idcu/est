package ltd.idcu.est.codecli.agent;

import ltd.idcu.est.agent.api.AgentRequest;
import ltd.idcu.est.agent.api.AgentResponse;
import ltd.idcu.est.agent.api.Skill;
import ltd.idcu.est.agent.api.SkillInput;
import ltd.idcu.est.agent.api.SkillResult;

import java.util.List;
import java.util.Scanner;

public class AgentCommandHandler {
    
    private final AgentManager agentManager;
    private final Scanner scanner;
    
    public AgentCommandHandler(AgentManager agentManager) {
        this.agentManager = agentManager;
        this.scanner = new Scanner(System.in);
    }
    
    public void handleAgentCommand(String command) {
        if (command.equalsIgnoreCase("agent")) {
            printAgentHelp();
            return;
        }
        
        if (command.equalsIgnoreCase("agent skills")) {
            listAgentSkills();
            return;
        }
        
        if (command.equalsIgnoreCase("agent clear")) {
            clearAgentMemory();
            return;
        }
        
        if (command.startsWith("agent generate")) {
            handleCodeGeneration(command.substring("agent generate".length()).trim());
            return;
        }
        
        if (command.startsWith("agent explain")) {
            handleCodeExplanation(command.substring("agent explain".length()).trim());
            return;
        }
        
        if (command.startsWith("agent optimize")) {
            handleCodeOptimization(command.substring("agent optimize".length()).trim());
            return;
        }
        
        if (command.startsWith("agent bugfix")) {
            handleBugFix(command.substring("agent bugfix".length()).trim());
            return;
        }
        
        if (command.startsWith("agent document")) {
            handleDocumentation(command.substring("agent document".length()).trim());
            return;
        }
        
        if (command.startsWith("agent ")) {
            handleAgentQuery(command.substring("agent ".length()).trim());
            return;
        }
        
        System.out.println("Unknown agent command. Type 'agent' for help.");
    }
    
    private void printAgentHelp() {
        System.out.println("=== EST Agent Commands ===");
        System.out.println();
        System.out.println("agent skills          - List all available agent skills");
        System.out.println("agent clear           - Clear agent memory");
        System.out.println();
        System.out.println("agent generate <desc> - Generate code (type: entity/controller/service/mapper)");
        System.out.println("agent explain <file>  - Explain code from file");
        System.out.println("agent optimize <file> - Analyze and suggest code optimizations");
        System.out.println("agent bugfix <file>   - Analyze and fix bugs in code");
        System.out.println("agent document <file> - Generate documentation for code");
        System.out.println();
        System.out.println("agent <query>         - Ask agent a question (uses multi-step reasoning)");
        System.out.println();
    }
    
    private void listAgentSkills() {
        System.out.println("=== Available Agent Skills ===");
        System.out.println();
        
        List<Skill> skills = agentManager.getAgent().getAllSkills();
        for (Skill skill : skills) {
            System.out.println("  - " + skill.getName());
            System.out.println("    " + skill.getDescription());
            System.out.println();
        }
        
        System.out.println("Total: " + skills.size() + " skills");
    }
    
    private void clearAgentMemory() {
        agentManager.clearMemory();
        System.out.println("Agent memory cleared.");
    }
    
    private void handleCodeGeneration(String args) {
        System.out.println("=== Code Generation ===");
        System.out.println();
        
        String description = args;
        String type = "entity";
        
        if (description.isEmpty()) {
            System.out.print("Enter code description: ");
            description = scanner.nextLine();
        }
        
        System.out.print("Enter code type (entity/controller/service/mapper) [entity]: ");
        String typeInput = scanner.nextLine().trim();
        if (!typeInput.isEmpty()) {
            type = typeInput;
        }
        
        System.out.println();
        System.out.println("Generating " + type + " code...");
        System.out.println();
        
        Skill skill = agentManager.getAgent().getSkill("code_generation");
        if (skill != null) {
            SkillInput input = new SkillInput();
            input.setParameter("description", description);
            input.setParameter("type", type);
            
            SkillResult result = skill.execute(input, null);
            if (result.isSuccess()) {
                System.out.println("Generated Code:");
                System.out.println("===============");
                System.out.println(result.getData());
            } else {
                System.out.println("Error: " + result.getErrorMessage());
            }
        } else {
            System.out.println("Code generation skill not found.");
        }
    }
    
    private void handleCodeExplanation(String filePath) {
        System.out.println("=== Code Explanation ===");
        System.out.println();
        
        if (filePath.isEmpty()) {
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();
        }
        
        System.out.println("Analyzing file: " + filePath);
        System.out.println();
        
        Skill skill = agentManager.getAgent().getSkill("code_explanation");
        if (skill != null) {
            SkillInput input = new SkillInput();
            input.setParameter("filePath", filePath);
            
            SkillResult result = skill.execute(input, null);
            if (result.isSuccess()) {
                System.out.println(result.getData());
            } else {
                System.out.println("Error: " + result.getErrorMessage());
            }
        } else {
            System.out.println("Code explanation skill not found.");
        }
    }
    
    private void handleCodeOptimization(String filePath) {
        System.out.println("=== Code Optimization ===");
        System.out.println();
        
        if (filePath.isEmpty()) {
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();
        }
        
        System.out.println("Analyzing file: " + filePath);
        System.out.println();
        
        Skill skill = agentManager.getAgent().getSkill("code_optimization");
        if (skill != null) {
            SkillInput input = new SkillInput();
            input.setParameter("filePath", filePath);
            
            SkillResult result = skill.execute(input, null);
            if (result.isSuccess()) {
                System.out.println(result.getData());
            } else {
                System.out.println("Error: " + result.getErrorMessage());
            }
        } else {
            System.out.println("Code optimization skill not found.");
        }
    }
    
    private void handleBugFix(String filePath) {
        System.out.println("=== Bug Fix Analysis ===");
        System.out.println();
        
        if (filePath.isEmpty()) {
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();
        }
        
        System.out.print("Enter error message (optional): ");
        String errorMessage = scanner.nextLine();
        
        System.out.println();
        System.out.println("Analyzing file: " + filePath);
        System.out.println();
        
        Skill skill = agentManager.getAgent().getSkill("bug_fix");
        if (skill != null) {
            SkillInput input = new SkillInput();
            input.setParameter("filePath", filePath);
            input.setParameter("errorMessage", errorMessage);
            
            SkillResult result = skill.execute(input, null);
            if (result.isSuccess()) {
                System.out.println(result.getData());
            } else {
                System.out.println("Error: " + result.getErrorMessage());
            }
        } else {
            System.out.println("Bug fix skill not found.");
        }
    }
    
    private void handleDocumentation(String filePath) {
        System.out.println("=== Documentation Generation ===");
        System.out.println();
        
        if (filePath.isEmpty()) {
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();
        }
        
        System.out.print("Enter document type (javadoc/readme) [javadoc]: ");
        String docType = scanner.nextLine().trim();
        if (docType.isEmpty()) {
            docType = "javadoc";
        }
        
        System.out.println();
        System.out.println("Generating documentation for: " + filePath);
        System.out.println();
        
        Skill skill = agentManager.getAgent().getSkill("documentation");
        if (skill != null) {
            SkillInput input = new SkillInput();
            input.setParameter("filePath", filePath);
            input.setParameter("docType", docType);
            
            SkillResult result = skill.execute(input, null);
            if (result.isSuccess()) {
                System.out.println("Generated Documentation:");
                System.out.println("======================");
                System.out.println(result.getData());
            } else {
                System.out.println("Error: " + result.getErrorMessage());
            }
        } else {
            System.out.println("Documentation skill not found.");
        }
    }
    
    private void handleAgentQuery(String query) {
        System.out.println("=== Agent Processing ===");
        System.out.println();
        System.out.println("Query: " + query);
        System.out.println();
        System.out.println("Processing... (this may take a moment)");
        System.out.println();
        
        AgentResponse response = agentManager.processRequest(
            new AgentRequest(query, 10, null)
        );
        
        System.out.println("=== Execution Steps ===");
        for (AgentResponse.AgentStep step : response.getSteps()) {
            System.out.println();
            System.out.println("Step " + step.getStepNumber() + ": " + step.getAction());
            System.out.println("  " + step.getObservation());
        }
        
        System.out.println();
        System.out.println("=== Final Answer ===");
        System.out.println(response.getFinalAnswer());
    }
}
