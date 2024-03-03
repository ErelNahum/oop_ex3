package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that the output method did not change due to an incorrect format.
 */
public class OutputIOException extends IOException {
    /**
     * Constructs an OutputIOException with a default error message.
     */
    public OutputIOException() {
        super("Did not change output method due to incorrect format.");
    }
}
