package Misc;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;

import static Misc.Functions.choose;

/**
 * This class can take a design Matrix and map its features to all possible polynomials of a given degree. For example,
 * with feature columns A and B and max degree 3, the following Matrix will be returned:
 *      1 A B AA AB BB AAA AAB ABB BBB
 */
public class MapFeatures {

    /**
     * Uses every column to return a Matrix with columns of all possible products up to a certain degree.
     * @param matrix the m x n Matrix with the original features.
     * @param maxDegree the highest degree polynomial in the result Matrix.
     * @param mf the MatrixFactory to use.
     * @return a Matrix with m rows and all possible polynomials of the columns up to a certain degree.
     */
    public static Matrix allPolynomials(Matrix matrix, int maxDegree, MatrixFactory mf) {
        int rows = matrix.rows();
        int cols = matrix.cols();
        int newCols = numberOfPolynomials(cols, maxDegree);

        int[][] exponents = new int[newCols][cols];
        Matrix res = mf.ones(rows, 0);

        int k = 1;
        for (int degree = 1; degree <= maxDegree; degree++) {
            k = rec(exponents, new int[cols], k, 0, cols - 1, degree);
        }

        for (int[] exponent : exponents) {
            Matrix col = mf.ones(rows, 1);
            for (int j = 0; j < cols; j++) {
                int ex = exponent[j];
                col = col.mulew(matrix.col(j).map(d -> (float) Math.pow(d, ex)));
            }
            res = res.conch(col);
        }
        return res;
    }

    private static int rec(int[][] exponents, int[] current, int k, int i, int end, int free) {
        current[i] = free;
        free = 0;
        System.arraycopy(current, 0, exponents[k++], 0, current.length);
        if (i != end) {
            while (current[i] > 0) {
                current[i]--;
                free++;
                k = rec(exponents, current, k, i + 1, end, free);
            }
        } else {
            current[i] = 0;
        }
        return k;
    }

    private static int numberOfPolynomials(int features, int degree) {
        if (degree == 0) return 1;
        else return choose(features + degree - 1, degree)
                + numberOfPolynomials(features, degree - 1);
    }
}
