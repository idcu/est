package ltd.idcu.est.core.api.exception;

@Deprecated
public class CircularDependencyException extends ltd.idcu.est.core.container.api.exception.CircularDependencyException {
    public CircularDependencyException(String message, java.util.List<String> resolutionChain) {
        super(message, resolutionChain);
    }
}
