package exceptions;
import java.io.IOException;

/**
 * Custom IOException indicating that the execution did not occur due to a problem with the image file.
 */
public class ImageIOException extends IOException {
    /**
     * Constructs an ImageIOException with a default error message.
     */
    public ImageIOException() {
        super("Did not execute due to problem with image file.");
    }
}
