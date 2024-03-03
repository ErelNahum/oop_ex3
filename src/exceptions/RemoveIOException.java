package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that an element did not remove due to an incorrect format.
 */
public class RemoveIOException extends IOException {
    /**
     * Constructs a RemoveIOException with a default error message.
     */
    public RemoveIOException() {
        super("Did not remove due to incorrect format.");
    }
}
