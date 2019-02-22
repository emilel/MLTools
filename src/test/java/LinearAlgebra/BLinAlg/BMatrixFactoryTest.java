package LinearAlgebra.BLinAlg;

import LinearAlgebra.Matrix;
import Misc.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BMatrixFactoryTest {
    static private BMatrixFactory mf;

    @BeforeAll
    static void setUp() {
        mf = new BMatrixFactory();
    }

    @Test
    void testOnes() {
        Matrix ones = mf.ones(5, 3);
        assertEquals(new Tuple<>(5, 3), ones.size(), "ones() returning matrix of wrong size");
        int rows = ones.rows();
        int cols = ones.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (ones.get(i, j) != 1) {
                    fail("Matrix of ones containing something else than ones");
                }
            }
        }
    }

    @Test
    void testZeroes() {
        Matrix zeroes = mf.zeroes(4, 10);
        assertEquals(new Tuple<>(4, 10), zeroes.size(), "zeroes() returning matrix of wrong size");
        int rows = zeroes.rows();
        int cols = zeroes.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (zeroes.get(i, j) != 0) {
                    fail("Matrix of zeroes containing something else than zeroes");
                }
            }
        }
    }

    @Test
    void testIdentityMatrix() {
        assertEquals(new BMatrix(new float[][]{new float[]{1f}}), mf.identityMatrix(1),
                "Error creating 1x1 matrix");
        assertEquals(new BMatrix(new float[][]{new float[]{1f, 0f, 0f}, new float[]{0f, 1f, 0f},
                new float[]{0f, 0f, 1f}}), mf.identityMatrix(3), "Error creating 3x3 matrix");
    }

    @Test
    void testScalar() {
        Matrix scalar = new BMatrix(new float[][]{new float[]{4f}});
        assertEquals(scalar, mf.s(4f), "Error creating a scalar");
        assertEquals(1, scalar.rows(), "Number of rows of a scalar should be one");
        assertEquals(1, scalar.cols(), "Number of columns of a scalar should be one");
    }
}