package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminApplication;

public class EstAdminMain {
    
    public static void main(String[] args) {
        int port = 8080;
        
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default 8080");
            }
        }
        
        AdminApplication adminApp = Admin.create("EST Admin Console", "1.0.0");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nStopping EST Admin Console...");
            adminApp.stop();
        }));
        
        try {
            adminApp.run(port);
        } catch (Exception e) {
            System.err.println("Failed to start EST Admin Console: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
