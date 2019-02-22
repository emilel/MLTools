package LinearAlgebra;

/**
 * Interface factory class for Matrices.
 */
public interface MatrixFactory {

    /**
     * Factory method for Matrices, taking a nested array of floats as argument.
     * @param elements the elements the Matrix should contain (an array of rows).
     * @return a Matrix containing the elements in the argument.
     */
    Matrix m(float[][] elements);

    /**
     * Factory method for Matrices, taking a String as argument.
     * @param elements the String containing the elements the Matrix should contain, separated by the row and
     *                 column delimiters.
     * @return a Matrix containing the elements in the argument.
     */
    Matrix m(String elements);

    /**
     * Factory method for Matrices, only consisting of one element (scalars).
     * @param element the float to have in the Matrix.
     * @return a 1x1 Matrix containing the float.
     */
    Matrix s(float element);

    /**
     * Factory method for Matrices containing only ones.
     * @param rows the number of rows in the wanted Matrix.
     * @param cols the number of columns in the wanted Matrix.
     * @return a rows x cols Matrix of only ones.
     */
    Matrix ones(int rows, int cols);

    /**
     * Factory method for Matrices containing only zeroes.
     * @param rows the number of rows in the wanted Matrix.
     * @param cols the number of columns in the wanted Matrix.
     * @return a rows x cols Matrix of only zeroes.
     */
    Matrix zeroes(int rows, int cols);

    /**
     * Factory method for identity Matrices.
     * @param size the size of the wanted identity Matrix.
     * @return a size x size identity Matrix.
     */
    Matrix identityMatrix(int size);
}
