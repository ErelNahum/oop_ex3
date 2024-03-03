package exceptions;

import java.io.IOException;

/**
 * Custom IOException indicating that the resolution did not change due to exceeding boundaries.
 */
public class ResolutionBoundariesIOException extends IOException {
    /**
     * Constructs a ResolutionIOException with a default error message.
     */
    public ResolutionBoundariesIOException() {
        super("Did not change resolution due to exceeding boundaries.");
    }
}
