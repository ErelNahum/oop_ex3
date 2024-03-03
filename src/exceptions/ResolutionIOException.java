package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that the resolution did not change due to exceeding boundaries.
 */
public class ResolutionIOException extends IOException {
    /**
     * Constructs a ResolutionIOException with a default error message.
     */
    public ResolutionIOException() {
        super("Did not change resolution due to incorrect format.");
    }
}
