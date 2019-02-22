package Misc;

import static Misc.MapFeatures.allPolynomials;
import static org.junit.jupiter.api.Assertions.*;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import org.junit.jupiter.api.Test;

class MapFeaturesTest {
    private static MatrixFactory mf = new BMatrixFactory();

    @Test
    void testMapFeatures() {
        Matrix mat = mf.m("0 1 2;3 6 2");
        Matrix ans = mf.m("1 0 1 2 0 0 0 1 2 4 0 0 0 0 0 0 1 2 4 8;" +
                                   "1 3 6 2 9 18 6 36 12 4 27 54 18 108 36 12 216 72 24 8");
        Matrix allPol = allPolynomials(mat, 3, mf);
        assertEquals(ans, allPol, "Error mapping features of 2 x 3 Matrix to order 3");
    }

}