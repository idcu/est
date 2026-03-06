package ltd.idcu.est.core.container.api.exception;

import java.util.List;

public class CircularDependencyException extends RuntimeException {

    private final List<String> dependencyChain;

    public CircularDependencyException(String message, List<String> dependencyChain) {
        super(message);
        this.dependencyChain = dependencyChain;
    }

    public CircularDependencyException(String message, Throwable cause, List<String> dependencyChain) {
        super(message, cause);
        this.dependencyChain = dependencyChain;
    }

    public List<String> getDependencyChain() {
        return dependencyChain;
    }
}
