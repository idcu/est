package ltd.idcu.est.cli.ide;

public class Diagnostic {
    private final String message;
    private final Severity severity;
    private final int line;
    private final int column;
    private final int endLine;
    private final int endColumn;

    public enum Severity {
        ERROR,
        WARNING,
        INFO,
        HINT
    }

    public Diagnostic(String message, Severity severity, int line, int column) {
        this(message, severity, line, column, line, column);
    }

    public Diagnostic(String message, Severity severity, int line, int column, int endLine, int endColumn) {
        this.message = message;
        this.severity = severity;
        this.line = line;
        this.column = column;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public String toString() {
        return "Diagnostic{" +
                "message='" + message + '\'' +
                ", severity=" + severity +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
