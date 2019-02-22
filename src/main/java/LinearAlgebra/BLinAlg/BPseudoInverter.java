package LinearAlgebra.BLinAlg;

/**
 * An interface for classes that can invert rectangular BMatrices.
 */
interface BPseudoInverter {
    /**
     * Finds the pseudo inverse of a rectangular BMatrix.
     * @param bMatrix the BMatrix to invert.
     * @return the pseudo inverse of the BMatrix given as argument.
     */
    BMatrix pseudoInvert(BMatrix bMatrix);
}
