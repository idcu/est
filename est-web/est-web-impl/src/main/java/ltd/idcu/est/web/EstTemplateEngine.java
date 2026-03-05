package ltd.idcu.est.web;

import ltd.idcu.est.web.api.View;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstTemplateEngine implements View.ViewEngine {

    private static final String NAME = "est-template";
    private static final String[] EXTENSIONS = {"html", "htm", "txt", "est"};

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");
    private static final Pattern IF_PATTERN = Pattern.compile("\\{%\\s*if\\s+(.+?)\\s*%\\}");
    private static final Pattern ELIF_PATTERN = Pattern.compile("\\{%\\s*elif\\s+(.+?)\\s*%\\}");
    private static final Pattern ELSE_PATTERN = Pattern.compile("\\{%\\s*else\\s*%\\}");
    private static final Pattern ENDIF_PATTERN = Pattern.compile("\\{%\\s*endif\\s*%\\}");
    private static final Pattern FOR_PATTERN = Pattern.compile("\\{%\\s*for\\s+(\\w+)\\s+in\\s+(.+?)\\s*%\\}");
    private static final Pattern ENDFOR_PATTERN = Pattern.compile("\\{%\\s*endfor\\s*%\\}");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("\\{#.*?#\\}", Pattern.DOTALL);

    private final Map<String, Function<Object, String>> filters;

    public EstTemplateEngine() {
        this.filters = new HashMap<>();
        registerDefaultFilters();
    }

    private void registerDefaultFilters() {
        filters.put("upper", s -> s != null ? s.toString().toUpperCase() : "");
        filters.put("lower", s -> s != null ? s.toString().toLowerCase() : "");
        filters.put("capitalize", s -> {
            if (s == null) return "";
            String str = s.toString();
            return str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        });
        filters.put("trim", s -> s != null ? s.toString().trim() : "");
        filters.put("escape", s -> {
            if (s == null) return "";
            String str = s.toString();
            return str.replace("&", "&amp;")
                      .replace("<", "&lt;")
                      .replace(">", "&gt;")
                      .replace("\"", "&quot;")
                      .replace("'", "&#39;");
        });
        filters.put("length", s -> {
            if (s == null) return "0";
            if (s instanceof CharSequence) return String.valueOf(((CharSequence) s).length());
            if (s instanceof Collection) return String.valueOf(((Collection<?>) s).size());
            if (s.getClass().isArray()) return String.valueOf(java.lang.reflect.Array.getLength(s));
            return "0";
        });
    }

    public void registerFilter(String name, Function<Object, String> filter) {
        filters.put(name, filter);
    }

    @Override
    public String render(String template, Map<String, Object> model) {
        if (template == null) return "";
        if (model == null) model = new HashMap<>();

        String processedTemplate = removeComments(template);
        processedTemplate = processForLoops(processedTemplate, model);
        processedTemplate = processConditionals(processedTemplate, model);
        processedTemplate = processVariables(processedTemplate, model);

        return processedTemplate;
    }

    private String removeComments(String template) {
        Matcher matcher = COMMENT_PATTERN.matcher(template);
        return matcher.replaceAll("");
    }

    private String processForLoops(String template, Map<String, Object> model) {
        StringBuilder result = new StringBuilder();
        int pos = 0;
        Matcher forMatcher = FOR_PATTERN.matcher(template);

        while (forMatcher.find(pos)) {
            result.append(template, pos, forMatcher.start());
            
            String varName = forMatcher.group(1);
            String iterableExpr = forMatcher.group(2);
            
            int endForPos = findMatchingEndTag(template, forMatcher.end(), "for", "endfor");
            if (endForPos == -1) {
                result.append(template.substring(forMatcher.start()));
                break;
            }
            
            String loopBody = template.substring(forMatcher.end(), endForPos);
            
            Object iterable = resolveValue(iterableExpr, model);
            if (iterable != null) {
                Iterator<?> iterator = getIterator(iterable);
                if (iterator != null) {
                    int index = 0;
                    while (iterator.hasNext()) {
                        Object item = iterator.next();
                        Map<String, Object> loopModel = new HashMap<>(model);
                        loopModel.put(varName, item);
                        loopModel.put(varName + "_index", index);
                        loopModel.put(varName + "_first", index == 0);
                        loopModel.put(varName + "_last", !iterator.hasNext());
                        
                        String processedBody = render(loopBody, loopModel);
                        result.append(processedBody);
                        index++;
                    }
                }
            }
            
            pos = findEndTagEnd(template, endForPos, "endfor");
        }
        
        result.append(template.substring(pos));
        return result.toString();
    }

    private String processConditionals(String template, Map<String, Object> model) {
        StringBuilder result = new StringBuilder();
        int pos = 0;
        Matcher ifMatcher = IF_PATTERN.matcher(template);

        while (ifMatcher.find(pos)) {
            result.append(template, pos, ifMatcher.start());
            
            int ifStart = ifMatcher.start();
            int endifPos = findMatchingEndTag(template, ifMatcher.end(), "if", "endif");
            
            if (endifPos == -1) {
                result.append(template.substring(ifStart));
                break;
            }
            
            String fullBlock = template.substring(ifMatcher.end(), endifPos);
            String selectedContent = "";
            boolean anyConditionMet = false;
            int blockPos = 0;
            
            while (blockPos < fullBlock.length() && !anyConditionMet) {
                int nextElif = findNextTag(fullBlock, blockPos, "elif");
                int nextElse = findNextTag(fullBlock, blockPos, "else");
                
                if (blockPos == 0) {
                    String condition = ifMatcher.group(1);
                    if (evaluateCondition(condition, model)) {
                        int endContent = (nextElif != -1 && (nextElse == -1 || nextElif < nextElse)) ? nextElif : 
                                        (nextElse != -1 ? nextElse : fullBlock.length());
                        selectedContent = fullBlock.substring(0, endContent);
                        anyConditionMet = true;
                    }
                } else if (nextElif != -1 && nextElif == blockPos) {
                    Matcher elifMatcher = ELIF_PATTERN.matcher(fullBlock.substring(nextElif));
                    if (elifMatcher.find()) {
                        String elifCondition = elifMatcher.group(1);
                        int elifEnd = nextElif + elifMatcher.end();
                        if (evaluateCondition(elifCondition, model)) {
                            int nextNextElif = findNextTag(fullBlock, elifEnd, "elif");
                            int nextNextElse = findNextTag(fullBlock, elifEnd, "else");
                            int endContent = (nextNextElif != -1 && (nextNextElse == -1 || nextNextElif < nextNextElse)) ? nextNextElif :
                                           (nextNextElse != -1 ? nextNextElse : fullBlock.length());
                            selectedContent = fullBlock.substring(elifEnd, endContent);
                            anyConditionMet = true;
                        }
                    }
                } else if (nextElse != -1 && nextElse == blockPos) {
                    Matcher elseMatcher = ELSE_PATTERN.matcher(fullBlock.substring(nextElse));
                    if (elseMatcher.find()) {
                        int elseEnd = nextElse + elseMatcher.end();
                        selectedContent = fullBlock.substring(elseEnd);
                        anyConditionMet = true;
                    }
                }
                
                if (!anyConditionMet) {
                    if (nextElif != -1 && (nextElse == -1 || nextElif < nextElse)) {
                        Matcher elifMatcher = ELIF_PATTERN.matcher(fullBlock.substring(nextElif));
                        if (elifMatcher.find()) {
                            blockPos = nextElif;
                        } else {
                            break;
                        }
                    } else if (nextElse != -1) {
                        blockPos = nextElse;
                    } else {
                        break;
                    }
                }
            }
            
            result.append(selectedContent);
            pos = findEndTagEnd(template, endifPos, "endif");
            ifMatcher = IF_PATTERN.matcher(template);
            ifMatcher.region(pos, template.length());
        }
        
        result.append(template.substring(pos));
        return result.toString();
    }

    private String processVariables(String template, Map<String, Object> model) {
        StringBuffer result = new StringBuffer();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String expr = matcher.group(1).trim();
            String value = processExpression(expr, model);
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String processExpression(String expr, Map<String, Object> model) {
        String[] parts = expr.split("\\|");
        Object value = resolveValue(parts[0].trim(), model);
        
        for (int i = 1; i < parts.length; i++) {
            String filterName = parts[i].trim();
            Function<Object, String> filter = filters.get(filterName);
            if (filter != null) {
                value = filter.apply(value);
            }
        }
        
        return value != null ? value.toString() : "";
    }

    private Object resolveValue(String expr, Map<String, Object> model) {
        expr = expr.trim();
        
        if ((expr.startsWith("\"") && expr.endsWith("\"")) || 
            (expr.startsWith("'") && expr.endsWith("'"))) {
            return expr.substring(1, expr.length() - 1);
        }
        
        if ("true".equals(expr)) return true;
        if ("false".equals(expr)) return false;
        if ("null".equals(expr)) return null;
        
        try {
            if (expr.contains(".")) {
                return resolveNestedProperty(expr, model);
            }
            return model.get(expr);
        } catch (Exception e) {
            return null;
        }
    }

    private Object resolveNestedProperty(String expr, Map<String, Object> model) {
        String[] parts = expr.split("\\.");
        Object current = model.get(parts[0]);
        
        for (int i = 1; i < parts.length && current != null; i++) {
            String part = parts[i];
            
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                try {
                    Field field = current.getClass().getDeclaredField(part);
                    field.setAccessible(true);
                    current = field.get(current);
                } catch (NoSuchFieldException e) {
                    try {
                        String getterName = "get" + part.substring(0, 1).toUpperCase() + part.substring(1);
                        Method method = current.getClass().getMethod(getterName);
                        current = method.invoke(current);
                    } catch (Exception ex) {
                        current = null;
                    }
                } catch (Exception e) {
                    current = null;
                }
            }
        }
        
        return current;
    }

    private boolean evaluateCondition(String condition, Map<String, Object> model) {
        condition = condition.trim();
        
        if (condition.contains(">=")) {
            return compareNumeric(condition, ">=", model) >= 0;
        }
        
        if (condition.contains("<=")) {
            return compareNumeric(condition, "<=", model) <= 0;
        }
        
        if (condition.contains("==")) {
            String[] parts = condition.split("==", 2);
            Object left = resolveValue(parts[0].trim(), model);
            Object right = resolveValue(parts[1].trim(), model);
            return Objects.equals(left, right);
        }
        
        if (condition.contains("!=")) {
            String[] parts = condition.split("!=", 2);
            Object left = resolveValue(parts[0].trim(), model);
            Object right = resolveValue(parts[1].trim(), model);
            return !Objects.equals(left, right);
        }
        
        if (condition.contains(">")) {
            return compareNumeric(condition, ">", model) > 0;
        }
        
        if (condition.contains("<")) {
            return compareNumeric(condition, "<", model) < 0;
        }
        
        Object value = resolveValue(condition, model);
        return isTruthy(value);
    }

    private int compareNumeric(String condition, String operator, Map<String, Object> model) {
        String[] parts = condition.split(Pattern.quote(operator), 2);
        Object left = resolveValue(parts[0].trim(), model);
        Object right = resolveValue(parts[1].trim(), model);
        
        double leftNum = toDouble(left);
        double rightNum = toDouble(right);
        
        return Double.compare(leftNum, rightNum);
    }

    private double toDouble(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof CharSequence) return ((CharSequence) value).length() > 0;
        if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
        if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();
        if (value.getClass().isArray()) return java.lang.reflect.Array.getLength(value) > 0;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        return true;
    }

    private Iterator<?> getIterator(Object obj) {
        if (obj instanceof Iterable) {
            return ((Iterable<?>) obj).iterator();
        }
        if (obj.getClass().isArray()) {
            List<Object> list = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                list.add(java.lang.reflect.Array.get(obj, i));
            }
            return list.iterator();
        }
        return null;
    }

    private int findMatchingEndTag(String template, int startPos, String startTag, String endTag) {
        int depth = 1;
        int pos = startPos;
        
        while (pos < template.length()) {
            Pattern startPattern = Pattern.compile("\\{%\\s*" + startTag + "\\s");
            Pattern endPattern = Pattern.compile("\\{%\\s*" + endTag + "\\s*%\\}");
            
            Matcher startMatcher = startPattern.matcher(template);
            Matcher endMatcher = endPattern.matcher(template);
            
            boolean foundStart = startMatcher.find(pos);
            boolean foundEnd = endMatcher.find(pos);
            
            if (!foundEnd) break;
            
            if (foundStart && startMatcher.start() < endMatcher.start()) {
                depth++;
                pos = startMatcher.end();
            } else {
                depth--;
                if (depth == 0) {
                    return endMatcher.start();
                }
                pos = endMatcher.end();
            }
        }
        
        return -1;
    }

    private int findNextTag(String template, int startPos, String tagName) {
        Pattern pattern;
        if ("endif".equals(tagName) || "endfor".equals(tagName)) {
            pattern = Pattern.compile("\\{%\\s*" + tagName + "\\s*%\\}");
        } else if ("elif".equals(tagName)) {
            pattern = ELIF_PATTERN;
        } else if ("else".equals(tagName)) {
            pattern = ELSE_PATTERN;
        } else {
            pattern = Pattern.compile("\\{%\\s*" + tagName + "\\s");
        }
        
        Matcher matcher = pattern.matcher(template);
        if (matcher.find(startPos)) {
            return matcher.start();
        }
        return -1;
    }

    private int findEndTagEnd(String template, int tagStart, String tagName) {
        Pattern pattern = Pattern.compile("\\{%\\s*" + tagName + "\\s*%\\}");
        Matcher matcher = pattern.matcher(template);
        if (matcher.find(tagStart)) {
            return matcher.end();
        }
        return template.length();
    }

    @Override
    public void render(String template, Map<String, Object> model, OutputStream outputStream) {
        try {
            String rendered = render(template, model);
            outputStream.write(rendered.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to render template to output stream", e);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getExtensions() {
        return EXTENSIONS;
    }
}
