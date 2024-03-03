package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that the execution did not occur because the charset is empty.
 */
public class EmptyCharSetIOException extends IOException {
    /**
     * Constructs an EmptyCharSetIOException with a default error message.
     */
    public EmptyCharSetIOException() {
        super("Did not execute. Charset is empty.");
    }
}
