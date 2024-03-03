package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that an element did not add due to an incorrect format.
 */
public class AddIOException extends IOException {
    /**
     * Constructs an AddIOException with a default error message.
     */
    public AddIOException() {
        super("Did not add due to incorrect format.");
    }
}