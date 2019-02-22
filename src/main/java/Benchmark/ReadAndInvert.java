package Benchmark;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import LinearAlgebra.ReadWrite.BFNAReader;
import LinearAlgebra.ReadWrite.FNAReader;

/**
 * A Runnable able to read a Matrix from disk (located in a textfile) and invert it.
 */
class ReadAndInvert implements Runnable {
    private static FNAReader reader;
    private final MatrixFactory mf;
    private final String filename;
    private final int rows;
    private final int cols;


    ReadAndInvert(String filename, int rows, int cols) {
        reader = new BFNAReader(' ');
        mf = new BMatrixFactory();
        this.filename = filename;
        this.rows = rows;
        this.cols = cols;
    }

    public void run() {
        System.out.println("Reading values from disk...");
        float[][] prim = reader.r(filename, rows, cols);
        System.out.println("Creating the matrix...");
        Matrix matrix = mf.m(prim);
        System.out.println("Calling pseudo inv method...");
        matrix.pinv();
    }
}
