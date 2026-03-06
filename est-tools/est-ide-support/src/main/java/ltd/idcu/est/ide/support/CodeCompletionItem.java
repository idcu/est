package ltd.idcu.est.ide.support;

public class CodeCompletionItem {
    private final String label;
    private final String insertText;
    private final String description;
    private final CompletionKind kind;
    private final int sortText;

    public enum CompletionKind {
        KEYWORD,
        CLASS,
        METHOD,
        FIELD,
        ANNOTATION,
        PROPERTY,
        TEMPLATE,
        SNIPPET
    }

    public CodeCompletionItem(String label, String insertText, String description, CompletionKind kind, int sortText) {
        this.label = label;
        this.insertText = insertText;
        this.description = description;
        this.kind = kind;
        this.sortText = sortText;
    }

    public CodeCompletionItem(String label, String insertText, String description, CompletionKind kind) {
        this(label, insertText, description, kind, 0);
    }

    public String getLabel() {
        return label;
    }

    public String getInsertText() {
        return insertText;
    }

    public String getDescription() {
        return description;
    }

    public CompletionKind getKind() {
        return kind;
    }

    public int getSortText() {
        return sortText;
    }

    @Override
    public String toString() {
        return "CodeCompletionItem{" +
                "label='" + label + '\'' +
                ", kind=" + kind +
                '}';
    }
}
