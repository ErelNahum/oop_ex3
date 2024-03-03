package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that the execution did not occur due to an incorrect command.
 */
public class NameIOException extends IOException {
    /**
     * Constructs a NameIOException with a default error message.
     */
    public NameIOException() {
        super("Did not execute due to incorrect command.");
    }
}
