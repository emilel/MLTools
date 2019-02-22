package LinearAlgebra.BLinAlg;

import LinearAlgebra.MatrixFactory;
import LinearAlgebra.ReadWrite.BFNAParser;
import LinearAlgebra.ReadWrite.FNAParser;

import java.util.Arrays;

/**
 * A factory class for BMatrices. See the documentation of the MatricFac class.
 */

public class BMatrixFactory implements MatrixFactory {
    private static final char STD_COL_DELIMITER = ' ';
    private static final char STD_ROW_DELIMITER = ';';
    private final FNAParser BFNAParser;

    /**
     * Constructor, which takes the column and row delimiters as arguments.
     * @param colDelimiter the character between columns.
     * @param rowDelimiter the character between rows.
     */
    public BMatrixFactory(char colDelimiter, char rowDelimiter) {
        BFNAParser = new BFNAParser(colDelimiter, rowDelimiter);
    }

    /**
     * Constructor which uses the standard column and row delimiters.
     */
    public BMatrixFactory() {
        this(STD_COL_DELIMITER, STD_ROW_DELIMITER);
    }

    public BMatrix m(float[][] elements) {
        return new BMatrix(elements);
    }

    public BMatrix m(String elements) {
        return m(BFNAParser.parse(elements));
    }

    public BMatrix s(float element) {
        return new BMatrix(new float[][]{new float[]{element}});
    }

    public BMatrix ones(int rows, int cols) {
        float[][] res = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            res[i] = new float[cols];
            Arrays.fill(res[i], 1f);
        }
        return new BMatrix(res);
    }

    public BMatrix zeroes(int rows, int cols) {
        float[][] res = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            res[i] = new float[cols];
            Arrays.fill(res[i], 0f);
        }
        return new BMatrix(res);
    }

    public BMatrix identityMatrix(int size) {
        float[][] res = new float[size][size];
        for (float[] row : res) {
            Arrays.fill(row, 0f);
        }
        for (int i = 0; i < size; i++) {
            res[i][i] = 1f;
        }
        return new BMatrix(res);
    }
}
