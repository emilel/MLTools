package Misc;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.MatrixFactory;
import org.junit.jupiter.api.Test;

import static Misc.Functions.*;
import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {
    private static MatrixFactory mf = new BMatrixFactory();

    @Test
    void testSigmoid() {
        assertEquals(mf.s(0.5f), sigmoid(mf.s(0f)));
        assertEquals(mf.m("0.982013 0.000335;0.9241 0.5744"), sigmoid(mf.m("4 -8;2.5 0.3")));
    }

    @Test
    void testLog() {
        assertEquals(mf.m("2.30258;-0.6931"), log(mf.m("10;0.5")));
    }

    @Test
    void testChoose() {
        assertEquals(10, choose(5, 2));
        assertEquals(1, choose(10, 10));
        assertEquals(80200, choose(401, 2));
    }

    @Test
    void testFactorial() {
        assertEquals(6, factorial(3));
        assertEquals(120, factorial(5));
    }
}