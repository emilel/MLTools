package LinearAlgebra.BLinAlg;

import LinearAlgebra.Matrix;

import java.util.Arrays;

/**
 * A class able to find the pseudo inverse of a BMatrix. Uses the geninv algorithm found in the following paper:
 * Courrieu, Pierre. (2008). Fast Computation of Moore-Penrose Inverse Matrices. Neural Information Processing-Letters
 * and Reviews. 8.
 */
public class GenInv implements BPseudoInverter {

    /**
     * Finds the pseudo inverse of a rectangular BMatrix.
     * @param bMatrix the BMatrix to invert.
     * @return the pseudo inverse of the BMatrix given as argument.
     */
    public BMatrix pseudoInvert(BMatrix bMatrix) {
        boolean transpose = false;
        int m = bMatrix.rows();
        int n = bMatrix.cols();
        BMatrix A;

        if (m < n) {
            transpose = true;
            A = bMatrix.mul(bMatrix.tran());
            n = m;
        } else {
            A = bMatrix.tran().mul(bMatrix);
        }

        Matrix dA = A.diag();
        float tol = dA.usemask(dA.mask(d -> d > 0)).minv().get(0).toFloat() * 1e-8f;

        float[][] l = new float[A.rows()][A.cols()];
        for (float[] row : l) {
            Arrays.fill(row, 0f);
        }
        BMatrix L = new BMatrix(l);

        int r = -1;
        for (int k = 0; k < n; k++) {
            r++;

            if (r > 0) {
                L = L.ins(k, r,
                        A.subm(k, n - 1, k, k)
                                .sub(
                                        L.subm(k, n - 1, 0, r - 1)
                                                .mul(L.subm(k, k, 0, r - 1)
                                                        .tran())));
            } else {
                L = L.ins(k, r, A.subm(k, n - 1, k, k));
            }
            if (L.get(k, r) > tol) {
                L = L.ins(k, r, (float) Math.sqrt(L.get(k, r)));
                if (k != n - 1) {
                    L = L.ins(k + 1, r,
                            L.subm(k + 1, n - 1, r, r)
                                    .div(L.get(k, r))
                    );
                }
            } else {
                r--;
            }
        }

        L = L.colr(0, r);
        Matrix M = L.tran().mul(L).inv();

        if (transpose) {
            return bMatrix.tran().mul(L).mul(M).mul(M).mul(L.tran());
        } else {
            return L.mul(M).mul(M).mul(L.tran()).mul(bMatrix.tran());
        }
    }
}
