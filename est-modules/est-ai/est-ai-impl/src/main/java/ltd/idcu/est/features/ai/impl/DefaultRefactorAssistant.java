package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRefactorAssistant implements RefactorAssistant {
    
    private static final Pattern FOR_LOOP_PATTERN = Pattern.compile(
        "for\\s*\\((\\w+)\\s+([\\w\\[\\]]+)\\s*:\\s*([\\w.]+)\\)\\s*\\{[^}]*\\}"
    );
    
    private static final Pattern NULL_CHECK_PATTERN = Pattern.compile(
        "if\\s*\\((\\w+)\\s*!\\s*=\\s*null\\)\\s*\\{"
    );
    
    private static final Pattern MAGIC_NUMBER_PATTERN = Pattern.compile(
        "\\b(\\d{3,})\\b"
    );
    
    @Override
    public List<RefactorSuggestion> analyze(String code) {
        return analyze(code, RefactorOptions.defaults());
    }
    
    @Override
    public List<RefactorSuggestion> analyze(String code, RefactorOptions options) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        suggestions.addAll(checkForLoopRefactoring(code));
        suggestions.addAll(checkNullCheckRefactoring(code));
        suggestions.addAll(checkMagicNumberRefactoring(code));
        suggestions.addAll(checkMethodLengthRefactoring(code));
        suggestions.addAll(checkVariableNamingRefactoring(code));
        
        return suggestions;
    }
    
    private List<RefactorSuggestion> checkForLoopRefactoring(String code) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        if (code.contains("for (")) {
            RefactorSuggestion suggestion = RefactorSuggestion.create(
                "使用 EST Collection 替代传统 for 循环",
                "EST 提供了更优雅的集合操作方式"
            )
            .id("replace-traditional-loop")
            .category("collection")
            .severity(RefactorSuggestion.RefactorSeverity.MEDIUM)
            .addBenefit("代码更简洁")
            .addBenefit("更符合函数式编程风格")
            .addBenefit("更容易并行化");
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
    
    private List<RefactorSuggestion> checkNullCheckRefactoring(String code) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        if (code.contains("!= null") || code.contains("== null")) {
            RefactorSuggestion suggestion = RefactorSuggestion.create(
                "考虑使用 Optional 替代 null 检查",
                "Optional 提供了更安全的 null 处理方式"
            )
            .id("use-optional")
            .category("null-safety")
            .severity(RefactorSuggestion.RefactorSeverity.MEDIUM)
            .addBenefit("更安全的 null 处理")
            .addBenefit("更清晰的代码意图")
            .addBenefit("减少 NullPointerException");
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
    
    private List<RefactorSuggestion> checkMagicNumberRefactoring(String code) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        Matcher matcher = MAGIC_NUMBER_PATTERN.matcher(code);
        if (matcher.find()) {
            RefactorSuggestion suggestion = RefactorSuggestion.create(
                "提取魔法数字为常量",
                "魔法数字应该定义为有意义的常量"
            )
            .id("extract-magic-number")
            .category("readability")
            .severity(RefactorSuggestion.RefactorSeverity.LOW)
            .addBenefit("提高代码可读性")
            .addBenefit("便于统一修改")
            .addBenefit("更好的代码维护性");
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
    
    private List<RefactorSuggestion> checkMethodLengthRefactoring(String code) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        String[] lines = code.split("\n");
        int openBraces = 0;
        int methodStartLine = -1;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            openBraces += countChar(line, '{');
            openBraces -= countChar(line, '}');
            
            if (line.contains("public") && line.contains("(") && line.contains(")") && methodStartLine == -1) {
                methodStartLine = i;
            }
            
            if (openBraces == 0 && methodStartLine != -1) {
                int methodLength = i - methodStartLine + 1;
                if (methodLength > 50) {
                    RefactorSuggestion suggestion = RefactorSuggestion.create(
                        "方法过长，考虑拆分",
                        "方法应该保持简短，通常不超过 50 行"
                    )
                    .id("split-long-method")
                    .category("maintainability")
                    .severity(RefactorSuggestion.RefactorSeverity.HIGH)
                    .location(methodStartLine, i)
                    .addBenefit("提高可读性")
                    .addBenefit("便于测试")
                    .addBenefit("提高复用性");
                    
                    suggestions.add(suggestion);
                }
                methodStartLine = -1;
            }
        }
        
        return suggestions;
    }
    
    private List<RefactorSuggestion> checkVariableNamingRefactoring(String code) {
        List<RefactorSuggestion> suggestions = new ArrayList<>();
        
        if (code.contains(" temp ") || code.contains(" tmp ")) {
            RefactorSuggestion suggestion = RefactorSuggestion.create(
                "避免使用 temp/tmp 等模糊变量名",
                "变量名应该有明确的含义"
            )
            .id("meaningful-names")
            .category("readability")
            .severity(RefactorSuggestion.RefactorSeverity.LOW)
            .addBenefit("提高代码可读性")
            .addBenefit("减少理解成本");
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
    
    private int countChar(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }
    
    @Override
    public String applyRefactoring(String code, RefactorSuggestion suggestion) {
        if ("replace-traditional-loop".equals(suggestion.getId())) {
            return replaceTraditionalLoop(code);
        }
        return code;
    }
    
    private String replaceTraditionalLoop(String code) {
        return code;
    }
    
    @Override
    public List<RefactorSuggestion> getAvailableRefactorings() {
        List<RefactorSuggestion> available = new ArrayList<>();
        
        available.add(RefactorSuggestion.create(
            "使用 EST Collection 替代传统 for 循环",
            "EST 提供了更优雅的集合操作方式"
        ).id("replace-traditional-loop").category("collection"));
        
        available.add(RefactorSuggestion.create(
            "考虑使用 Optional 替代 null 检查",
            "Optional 提供了更安全的 null 处理方式"
        ).id("use-optional").category("null-safety"));
        
        available.add(RefactorSuggestion.create(
            "提取魔法数字为常量",
            "魔法数字应该定义为有意义的常量"
        ).id("extract-magic-number").category("readability"));
        
        available.add(RefactorSuggestion.create(
            "方法过长，考虑拆分",
            "方法应该保持简短，通常不超过 50 行"
        ).id("split-long-method").category("maintainability"));
        
        return available;
    }
    
    @Override
    public boolean canApply(String code, RefactorSuggestion suggestion) {
        return true;
    }
}
