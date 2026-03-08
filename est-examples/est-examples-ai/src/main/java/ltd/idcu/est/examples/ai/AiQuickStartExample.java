package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.impl.Ai;

public class AiQuickStartExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI 助手快速开始示�?===\n");
        
        System.out.println("1. 获取快速参�?");
        System.out.println(Ai.quickRef("web"));
        System.out.println();
        
        System.out.println("2. 获取最佳实�?");
        System.out.println(Ai.bestPractice("error-handling"));
        System.out.println();
        
        System.out.println("3. 获取教程:");
        System.out.println(Ai.tutorial("first-app"));
        System.out.println();
        
        System.out.println("4. 代码建议:");
        System.out.println(Ai.suggest("我需要创建一个Web应用来处理HTTP请求"));
        System.out.println();
        
        System.out.println("5. 代码解释:");
        System.out.println(Ai.explain("WebApplication app = Web.create(\"MyApp\", \"1.0.0\");"));
        System.out.println();
        
        System.out.println("6. 代码优化建议:");
        System.out.println(Ai.optimize("app.get(\"/\", (req, res) -> { res.send(\"Hello\"); });"));
        System.out.println();
    }
}
