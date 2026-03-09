package ltd.idcu.est.codecli.search;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {

    private List<String> includeExtensions;
    private List<String> excludeExtensions;
    private List<String> includePaths;
    private List<String> excludePaths;
    private Integer minScore;
    private Integer maxResults;
    private boolean caseSensitive;
    private boolean fuzzySearch;
    private int fuzzyDistance;

    public SearchFilter() {
        this.includeExtensions = new ArrayList<>();
        this.excludeExtensions = new ArrayList<>();
        this.includePaths = new ArrayList<>();
        this.excludePaths = new ArrayList<>();
        this.caseSensitive = false;
        this.fuzzySearch = false;
        this.fuzzyDistance = 2;
    }

    public SearchFilter addIncludeExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        includeExtensions.add(extension.toLowerCase());
        return this;
    }

    public SearchFilter addExcludeExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        excludeExtensions.add(extension.toLowerCase());
        return this;
    }

    public SearchFilter addIncludePath(String path) {
        includePaths.add(path.toLowerCase());
        return this;
    }

    public SearchFilter addExcludePath(String path) {
        excludePaths.add(path.toLowerCase());
        return this;
    }

    public SearchFilter setMinScore(int minScore) {
        this.minScore = minScore;
        return this;
    }

    public SearchFilter setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public SearchFilter setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public SearchFilter setFuzzySearch(boolean fuzzySearch) {
        this.fuzzySearch = fuzzySearch;
        return this;
    }

    public SearchFilter setFuzzyDistance(int fuzzyDistance) {
        this.fuzzyDistance = fuzzyDistance;
        return this;
    }

    public boolean matches(String filePath, int score) {
        if (minScore != null && score < minScore) {
            return false;
        }

        String lowerPath = filePath.toLowerCase();

        if (!includeExtensions.isEmpty()) {
            boolean matches = false;
            for (String ext : includeExtensions) {
                if (lowerPath.endsWith(ext)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                return false;
            }
        }

        for (String ext : excludeExtensions) {
            if (lowerPath.endsWith(ext)) {
                return false;
            }
        }

        if (!includePaths.isEmpty()) {
            boolean matches = false;
            for (String path : includePaths) {
                if (lowerPath.contains(path)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                return false;
            }
        }

        for (String path : excludePaths) {
            if (lowerPath.contains(path)) {
                return false;
            }
        }

        return true;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public boolean isFuzzySearch() {
        return fuzzySearch;
    }

    public int getFuzzyDistance() {
        return fuzzyDistance;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public static SearchFilter javaFilesOnly() {
        return new SearchFilter().addIncludeExtension("java");
    }

    public static SearchFilter excludeTestFiles() {
        return new SearchFilter().addExcludePath("test");
    }

    public static SearchFilter sourceFilesOnly() {
        return new SearchFilter()
            .addIncludeExtension("java")
            .addIncludeExtension("js")
            .addIncludeExtension("ts")
            .addIncludeExtension("py")
            .addExcludePath("test")
            .addExcludePath("node_modules");
    }
}
