package ltd.idcu.est.ide.support;

public class CodeTemplate {
    private final String id;
    private final String name;
    private final String description;
    private final String template;
    private final FileType fileType;
    private final int priority;

    public CodeTemplate(String id, String name, String description, String template, FileType fileType, int priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.template = template;
        this.fileType = fileType;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTemplate() {
        return template;
    }

    public FileType getFileType() {
        return fileType;
    }

    public int getPriority() {
        return priority;
    }

    public CodeCompletionItem toCodeCompletionItem() {
        return new CodeCompletionItem(
                name,
                template,
                description,
                CodeCompletionItem.CompletionKind.TEMPLATE,
                priority
        );
    }
}
