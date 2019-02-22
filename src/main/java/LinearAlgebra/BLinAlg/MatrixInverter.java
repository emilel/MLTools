package LinearAlgebra.BLinAlg;

/**
 * An interface for classes able to find the inverse of square matrices, represented as nested ararys of floats.
 */
interface MatrixInverter {
    /**
     * Finds the inverse of a square matrix.
     * @param mat the matrix to invert.
     * @return the inverse of the argument matrix.
     */
    float[][] invert(float[][] mat);
}
