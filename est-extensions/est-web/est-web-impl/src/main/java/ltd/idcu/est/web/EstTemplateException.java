package ltd.idcu.est.web;

public class EstTemplateException extends RuntimeException {

    private final String template;
    private final int lineNumber;
    private final int columnNumber;

    public EstTemplateException(String message) {
        this(message, null, -1, -1, null);
    }

    public EstTemplateException(String message, String template) {
        this(message, template, -1, -1, null);
    }

    public EstTemplateException(String message, Throwable cause) {
        this(message, null, -1, -1, cause);
    }

    public EstTemplateException(String message, String template, int lineNumber, int columnNumber, Throwable cause) {
        super(buildMessage(message, template, lineNumber, columnNumber), cause);
        this.template = template;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    private static String buildMessage(String message, String template, int lineNumber, int columnNumber) {
        StringBuilder sb = new StringBuilder(message);
        if (template != null) {
            sb.append("\nTemplate: ").append(template);
        }
        if (lineNumber >= 0) {
            sb.append("\nLine: ").append(lineNumber);
            if (columnNumber >= 0) {
                sb.append(", Column: ").append(columnNumber);
            }
        }
        return sb.toString();
    }

    public String getTemplate() {
        return template;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
