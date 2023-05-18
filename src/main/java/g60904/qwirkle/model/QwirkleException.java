package g60904.qwirkle.model;
/**
 * The {@code QwirkleException} class represents an exception specific to the Qwirkle game.
 * It is a subclass of {@link RuntimeException}.
 */
public class QwirkleException extends RuntimeException {
    /**
     * Constructs a new QwirkleException with the specified detail message.
     *
     * @param message the detail message of the exception
     */
    public QwirkleException(String message) {
        super(message);
    }
}
