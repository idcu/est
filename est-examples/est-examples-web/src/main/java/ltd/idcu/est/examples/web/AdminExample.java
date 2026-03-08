package ltd.idcu.est.examples.web;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.AdminApplication;

public class AdminExample {
    
    public static void main(String[] args) {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("EST Admin Console - 管理后台示例");
        System.out.println("=".repeat(80));
        
        AdminApplication adminApp = Admin.create("EST Admin Console", "1.0.0");
        
        adminApp.run(8080);
    }
    
    public static void run() {
        main(new String[]{});
    }
}
