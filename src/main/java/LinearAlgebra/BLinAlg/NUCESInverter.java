package LinearAlgebra.BLinAlg;

import LinearAlgebra.MatrixException;

import java.util.LinkedList;
import java.util.List;

/**
 * A class able to find the inverse of square matrices. Uses the algorithm found in the following paper:
 * Ahmad, Farooq & Khan, Hamid. (2010). An Efficient and Simple Algorithm for
 * Matrix Inversion. IJTD. 1. 20-27. 10.4018/jtd.2010010102.
 */
class NUCESInverter implements MatrixInverter {
    private final float zeroTolerance;

    /**
     * Constructor for the object.
     * @param zeroTolerance the threshold for when a pivot element should be considered as zero (which means that the
     *                      matrix is not invertible).
     */
    NUCESInverter(float zeroTolerance) {
        this.zeroTolerance = zeroTolerance;
    }

    public float[][] invert(float[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        if (rows != cols) {
            throw new MatrixException("Matrix is not square and therefore not invertible");
        }

        List<Integer> indicesToRepeat = new LinkedList<>();

        for (int p = 0; p < rows; p++) {
            float pivot = mat[p][p];
            if (Math.abs(pivot) < zeroTolerance) {
                indicesToRepeat.add(p);
            } else {
                innerLoop(mat, p, pivot, rows);
            }
        }

        for (int index : indicesToRepeat) {
            float pivot = mat[index][index];
            if (Math.abs(pivot) < zeroTolerance) {
                throw new MatrixException("Can not find inv due to pivot element being too close to zero");
            }

            innerLoop(mat, index, pivot, rows);
        }

        return mat;
    }

    private void innerLoop(float[][] mat, int p, float pivot, int rows) {
        for (int i = 0; i < rows; i++) {
            mat[i][p] = -mat[i][p] / pivot;
        }

        for (int i = 0; i < rows; i++) {
            if (i != p) {
                for (int j = 0; j < rows; j++) {
                    if (j != p) {
                        mat[i][j] = mat[i][j] + mat[p][j] * mat[i][p];
                    }
                }
            }
        }

        for (int j = 0; j < rows; j++) {
            mat[p][j] = mat[p][j] / pivot;
            mat[p][p] = 1 / pivot;
        }
    }
}