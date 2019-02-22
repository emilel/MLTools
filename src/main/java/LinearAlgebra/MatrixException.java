package LinearAlgebra;

/**
 * The Exception to throw when something goes wrong performing Matrix operations.
 */
public class MatrixException extends RuntimeException {

    /**
     * Constructor.
     * @param message the message of the Exception.
     */
    public MatrixException(String message) {
        super(message);
    }
}
