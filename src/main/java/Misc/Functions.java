package Misc;

import LinearAlgebra.Matrix;

/**
 * A collection of useful functions.
 */
public class Functions {
    /**
     * Uses the sigmoid function on every element in the m x n input Matrix.
     * @param matrix the input Matrix.
     * @return a m x n Matrix where the elements are the outputs of the sigmoid function.
     */
    public static Matrix sigmoid(Matrix matrix) {
        return matrix.map(d -> 1f / (1f + (float) Math.exp(-d)));
    }

    /**
     * Uses the logarithm on every element in the m x n input Matrix.
     * @param matrix the input Matrix.
     * @return a m x n Matrix where the elements are the outputs of the logarithm function.
     */
    public static Matrix log(Matrix matrix) {
        return matrix.map(d -> (float) Math.log(d));
    }

    /**
     * Finds the binomial coefficient n choose k (n over k).
     * @param n the total number of elements.
     * @param k how many elements to choose.
     * @return n choose k.
     */
    public static int choose(int n, int k) {
        int t = 1;
        for (int i = n; i > n - k; i--) {
            t *= i;
        }
        return t / factorial(k);
    }

    /**
     * Finds the factorial of a number.
     * @param n the input number.
     * @return n factorial.
     */
    public static int factorial(int n) {
        if (n == 0) return 1;
        else return n * factorial(n - 1);
    }
}
