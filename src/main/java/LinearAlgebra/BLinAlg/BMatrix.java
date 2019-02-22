package LinearAlgebra.BLinAlg;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixException;
import Misc.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of the Matrix interface (Bad Matrix). Uses a nested array of floats for internal storage of the
 * elements.
 * COL_DELIMITER is the standard column delimiter used by the toString() method.
 * ROW_DELIMITER is the standard row delimiter used by the toString() method.
 * ZERO_TOLERANCE is the tolerance used when finding the inverse of square Matrices.
 * EQUALITY_TOLERANCE is the standard maximum allowed difference between two elements with the same index in two
 * different Matrices, which should still be considered equal.
 * matrixInverter is the instance of a MatrixInverter used to find the inverse of square Matrices.
 * bPseudoInverter is the instance of a BPseudoInverter used to find the pseudo inverse of rectangular Matrices.
 *
 * See the documentation of the superclass Matrix.
 */
public class BMatrix implements Matrix {
    private static final char COL_DELIMITER = ' ';
    private static final char ROW_DELIMITER = '\n';
    private static final float ZERO_TOLERANCE = 1e-5f;
    private static final float EQUALITY_TOLERANCE = 1e-4f;
    private static final MatrixInverter matrixInverter = new NUCESInverter(ZERO_TOLERANCE);
    private static final BPseudoInverter bPseudoInverter = new GenInv();
    private final float[][] mat;
    private final int m;
    private final int n;

    BMatrix(float[][] matrix) {
        this.mat = matrix;
        m = mat.length;
        n = mat[0].length;
    }

    BMatrix(float scalar) {
        this.mat = new float[][]{new float[]{scalar}};
        this.m = 1;
        this.n = 1;
    }

    public int rows() {
        return m;
    }

    public int cols() {
        return n;
    }

    public Tuple<Integer> size() {
        return new Tuple<>(m, n);
    }

    public BMatrix copy() {
        float[][] res = copyMatrix(mat);
        return new BMatrix(res);
    }

    public BMatrix conch(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;

        if (!(m == other.m)) {
            throw new MatrixException("Mismatching number of rows, " + m + " and " + other.m);
        }

        float[][] res = createfloatArray(m, n + other.cols());
        for (int i = 0; i < m; i++) {
            res[i] = combineHorizontally(mat[i], other.mat[i]);
        }
        return new BMatrix(res);
    }

    public BMatrix concv(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;

        if (!(n == other.n)) {
            throw new MatrixException("Mismatching number of columns, " + n + " and " + other.n);
        }

        float[][] res = createfloatArray(m + other.m, n);
        System.arraycopy(mat, 0, res, 0, m);
        System.arraycopy(other.mat, 0, res, m, other.rows());
        return new BMatrix(res);
    }

    public float get(int row, int col) {
        return mat[row][col];
    }

    public float toFloat() {
        if (size().equals(new Tuple<>(1, 1))) {
            return mat[0][0];
        } else {
            throw new MatrixException("Matrix is not 1x1");
        }
    }

    public BMatrix row(int row) {
        checkIndices(row, 0);
        return subm(row, row, 0, cols() - 1);
    }

    public BMatrix col(int col) {
        checkIndices(0, col);
        return subm(0, rows() - 1, col, col);
    }

    public BMatrix rows(int... rowIndices) {
        int rowsToReturn = rowIndices.length;
        float[][] res = createfloatArray(rowsToReturn, n);
        int i = 0;
        for (int rowIndex : rowIndices) {
            checkIndices(rowIndex, 0);
            res[i++] = mat[rowIndex];
        }
        return new BMatrix(res);
    }

    public BMatrix cols(int... colIndices) {
        int colsToReturn = colIndices.length;
        float[][] res = createfloatArray(m, colsToReturn);
        for (int i = 0; i < m; i++) {
            int j = 0;
            for (int colIndex : colIndices) {
                checkIndices(0, colIndex);
                res[i][j++] = mat[i][colIndex];
            }
        }
        return new BMatrix(res);
    }

    public BMatrix rowr(int from, int to) {
        return subm(from, to, 0, cols() - 1);
    }

    public BMatrix colr(int from, int to) {
        return subm(0, rows() - 1, from, to);
    }

    public BMatrix subm(int fromRow, int toRow, int fromCol, int toCol) {
        checkIndices(fromRow, fromCol);
        checkIndices(toRow, toCol);

        float[][] res = createfloatArray(toRow - fromRow + 1, toCol - fromCol + 1);
        int k = 0;
        int l;
        for (int i = fromRow; i <= toRow; i++) {
            l = 0;
            for (int j = fromCol; j <= toCol; j++) {
                res[k][l++] = mat[i][j];
            }
            k++;
        }
        return new BMatrix(res);
    }

    public BMatrix ins(int row, int col, float element) {
        BMatrix copy = copy();
        copy.mat[row][col] = element;
        return copy;
    }

    public BMatrix ins(int fromRow, int fromCol, Matrix otherMatrix) {
        BMatrix copy = copy();

        checkIndices(fromRow, fromCol);
        checkIndices(fromRow + otherMatrix.rows() - 1, fromCol + otherMatrix.cols() - 1);
        BMatrix other = (BMatrix) otherMatrix;
        int i = fromRow;
        for (float[] row : other.mat) {
            int j = fromCol;
            for (float element : row) {
                copy.mat[i][j++] = element;
            }
            i++;
        }

        return copy;
    }

    public Tuple<Matrix> minh() {
        return findExtremasHorizontally(Comparator.comparingDouble(d -> d));
    }

    public Tuple<Matrix> minv() {
        return findExtremasVertically(Comparator.comparing(d -> d));
    }

    public Tuple<Matrix> maxh() {
        return findExtremasHorizontally(Comparator.comparing(d -> -d));
    }

    public Tuple<Matrix> maxv() {
        return findExtremasVertically(Comparator.comparing(d -> -d));
    }

    public BMatrix add(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;

        int otherRows = other.rows();
        int otherCols = other.cols();

        if (otherRows == 1 && otherCols == 1) {
            return add(otherMatrix.get(0, 0));
        } else if (m == 1 && n == 1) {
            return other.add(mat[0][0]);
        } else if (otherRows == m && otherCols == n) {
            return addMatrix(other);
        } else {
            throw new MatrixException("Incompatible sizes for addition/subtraction, "
                    + size() + " and " + otherMatrix.size());
        }
    }

    public BMatrix addc(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;
        float[][] res = createfloatArray(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] + other.mat[i][0];
            }
        }

        return new BMatrix(res);
    }

    public BMatrix addr(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;
        float[][] res = createfloatArray(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] + other.mat[0][j];
            }
        }
        return new BMatrix(res);
    }

    public BMatrix add(float scalar) {
        return map(d -> d + scalar);
    }

    public BMatrix sub(Matrix other) {
        return add(other.map(d -> -d));
    }

    public BMatrix subr(Matrix row) {
        return addr(row.map(d -> -d));
    }

    public BMatrix subc(Matrix column) {
        return addc(column.map(d -> -d));
    }

    public BMatrix sub(float scalar) {
        return add(-scalar);
    }

    public BMatrix mul(Matrix otherMatrix) {
        BMatrix other = ((BMatrix) otherMatrix);

        if (n == other.m) {
            return multiplyMatrix(other);
        } else if (other.m == 1 && other.n == 1) {
            return mul(other.get(0, 0));
        } else if (m == 1 && n == 1) {
            return other.mul(mat[0][0]);
        } else {
            throw new MatrixException("Mismatching matrices for multiplication, " + size() + " and " + other.size());
        }
    }

    public BMatrix mul(float scalar) {
        return map(d -> d * scalar);
    }

    public Matrix mulr(Matrix rowMatrix) {
        BMatrix row = ((BMatrix) rowMatrix);
        if (!(row.cols() == n && row.rows() == 1)) {
            throw new MatrixException("Argument is not row of right size " + row.size());
        }
        float[][] res = createfloatArray(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] * row.mat[0][j];
            }
        }
        return new BMatrix(res);
    }

    public BMatrix mulc(Matrix columnMatrix) {
        BMatrix column = ((BMatrix) columnMatrix);
        if (!(column.rows() == m && column.cols() == 1)) {
            throw new MatrixException("Argument is not column of right size " + column.size());
        }
        float[][] res = createfloatArray(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] * column.mat[i][0];
            }
        }
        return new BMatrix(res);
    }

    public BMatrix mulew(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;

        if (m != other.rows() || n != other.cols()) {
            throw new MatrixException("Matrices are not of same size, " + size() + " and " + other.size());
        }
        float[][] res = createfloatArray(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] * other.mat[i][j];
            }
        }
        return new BMatrix(res);
    }

    public Matrix div(float scalar) {
        return mul(1 / scalar);
    }

    public Matrix divr(Matrix row) {
        return mulr(row.map(d -> 1 / d));
    }

    public Matrix divc(Matrix column) {
        return mulc(column.map(d -> 1 / d));
    }

    public Matrix divew(Matrix other) {
        return mulew(other.map(d -> 1 / d));
    }

    public BMatrix sumh() {
        float[][] res = createfloatArray(m, 1);
        for (int i = 0; i < m; i++) {
            float sum = 0;
            for (int j = 0; j < n; j++) {
                sum += mat[i][j];
            }
            res[i][0] = sum;
        }
        return new BMatrix(res);
    }

    public BMatrix sumv() {
        float[][] res = createfloatArray(1, n);
        for (int j = 0; j < n; j++) {
            float sum = 0;
            for (int i = 0; i < m; i++) {
                sum += mat[i][j];
            }
            res[0][j] = sum;
        }
        return new BMatrix(res);
    }

    public Matrix sum() {
        float sum = 0;
        for (float[] row : mat) {
            for (float element : row) {
                sum += element;
            }
        }
        return new BMatrix(sum);
    }

    public Matrix sumsqr() {
        return map(d -> (float) Math.pow(d, 2)).sum();
    }

    public BMatrix inv() {
        float[][] copy = copyMatrix(mat);
        return new BMatrix(matrixInverter.invert(copy));
    }

    public BMatrix pinv() {
        return bPseudoInverter.pseudoInvert(this);
    }

    public BMatrix tran() {
        float[][] res = createfloatArray(n, m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[j][i] = mat[i][j];
            }
        }
        return new BMatrix(res);
    }

    public BMatrix diag() {
        int end = Math.min(m, n);
        float[][] res = createfloatArray(end, 1);
        for (int i = 0; i < end; i++) {
            res[i][0] = mat[i][i];
        }
        return new BMatrix(res);
    }

    public BMatrix vec() {
        float[][] res = createfloatArray(m * n, 1);

        int k = 0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                res[k++][0] = mat[i][j];
            }
        }

        return new BMatrix(res);
    }

    public BMatrix map(Function<Float, Float> function) {
        float[][] res = createfloatArray(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = function.apply(mat[i][j]);
            }
        }
        return new BMatrix(res);
    }

    public Matrix mask(Predicate<Float> pred) {
        return map(d -> {
            if (pred.test(d)) return 1f;
            else return 0f;
        });
    }

    public Matrix usemask(Matrix maskMatrix) {
        BMatrix mask = (BMatrix) maskMatrix;
        ArrayList<Float> values = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                if (mask.mat[i][j] != 0) {
                    values.add(mat[i][j]);
                }
            }
        }

        float[][] res = createfloatArray(values.size(), 1);
        int i = 0;
        for (float element : values) {
            res[i++][0] = element;
        }
        return new BMatrix(res);
    }

    public BMatrix addr1() {
        float[][] ones = createfloatArray(1, n);
        Arrays.fill(ones[0], 1f);
        BMatrix rowOfOnes = new BMatrix(ones);
        return rowOfOnes.concv(this);
    }

    public BMatrix addc1() {
        float[][] ones = createfloatArray(m, 1);
        for (float[] row : ones) {
            row[0] = 1f;
        }
        BMatrix columnOfOnes = new BMatrix(ones);
        return columnOfOnes.conch(this);
    }

    public BMatrix rtr() {
        if (m < 2) {
            throw new MatrixException("Can not remove top row from matrix with less than two m");
        }
        float[][] res = createfloatArray(m - 1, n);
        for (int i = 1; i < m; i++) {
            System.arraycopy(mat[i], 0, res[i - 1], 0, n);
        }
        return new BMatrix(res);
    }

    public BMatrix rlc() {
        if (n < 2) {
            throw new MatrixException("Can not remove left column from matrix with only one column");
        }
        float[][] res = createfloatArray(m, n - 1);
        for (int i = 0; i < m; i++) {
            System.arraycopy(mat[i], 1, res[i], 0, n - 1);
        }
        return new BMatrix(res);
    }

    public Set<Float> unq() {
        Set<Float> values = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                values.add(mat[i][j]);
            }
        }
        return values;
    }

    public Tuple<Matrix> svd() {
        throw new MatrixException("Feature not yet implemented (please do implement it)");
    }

    public String toString() {
        return toString(COL_DELIMITER, ROW_DELIMITER);
    }

    public String toString(char colDelimiter, char rowDelimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                stringBuilder.append(mat[i][j]);
                if (j != n - 1) {
                    stringBuilder.append(colDelimiter);
                }
            }
            if (i != m - 1) {
                stringBuilder.append(rowDelimiter);
            }
        }

        return stringBuilder.toString();
    }

    public boolean equals(Object other) {
        return equals(other, EQUALITY_TOLERANCE);
    }

    public boolean equals(Object other, float tolerance) {
        if (!(other instanceof BMatrix)) {
            return false;
        } else {
            BMatrix otherMatrix = (BMatrix) other;
            if (!size().equals(otherMatrix.size())) {
                return false;
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (Math.abs(otherMatrix.mat[i][j] - (mat[i][j])) > tolerance) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public Matrix equalsEw(Matrix otherMatrix) {
        BMatrix other = (BMatrix) otherMatrix;
        float[][] res = createfloatArray(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(mat[i][j] - other.mat[i][j]) < EQUALITY_TOLERANCE) {
                    res[i][j] = 1f;
                } else {
                    res[i][j] = 0f;
                }
            }
        }

        return new BMatrix(res);
    }

    private static float[][] createfloatArray(int rows, int cols) {
        return new float[rows][cols];
    }

    private BMatrix addMatrix(BMatrix otherMatrix) {
        float[][] res = createfloatArray(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = mat[i][j] + otherMatrix.mat[i][j];
            }
        }
        return new BMatrix(res);
    }

    private static float[] combineHorizontally(float[] array1, float[] array2) {
        float[] res = new float[array1.length + array2.length];
        System.arraycopy(array1, 0, res, 0, array1.length);
        System.arraycopy(array2, 0, res, array1.length, array2.length);
        return res;
    }

    private float[][] copyMatrix(float[][] mat) {
        float[][] res = createfloatArray(m, n);

        for (int i = 0; i < m; i++) {
            System.arraycopy(mat[i], 0, res[i], 0, n);
        }
        return res;
    }

    private void checkIndices(int row, int col) {
        if (row < 0 || row >= mat.length || col < 0 || col >= mat[0].length) {
            throw new MatrixException("Row or column index out of bounds. Tried to access (" + row + ", " + col
                    + ") in a (" + mat.length + ", " + mat[0].length + ") matrix");
        }
    }

    private Tuple<Matrix> findExtremasVertically(Comparator<Float> comp) {
        float[][] extremas = createfloatArray(1, n);
        float[][] indices = createfloatArray(1, n);
        for (int j = 0; j < n; j++) {
            float extremum = mat[0][j];
            int minIndex = 0;
            for (int i = 1; i < m; i++) {
                if (comp.compare(mat[i][j], extremum) < 0) {
                    extremum = mat[i][j];
                    minIndex = i;
                }
            }
            extremas[0][j] = extremum;
            indices[0][j] = (float) minIndex;
        }
        return new Tuple<>(new BMatrix(extremas), new BMatrix(indices));
    }

    private Tuple<Matrix> findExtremasHorizontally(Comparator<Float> comp) {
        float[][] extremas = createfloatArray(m, 1);
        float[][] indices = createfloatArray(m, 1);
        for (int i = 0; i < m; i++) {
            float extremum = mat[i][0];
            int minIndex = 0;
            for (int j = 1; j < n; j++) {
                if (comp.compare(mat[i][j], extremum) < 0) {
                    extremum = mat[i][j];
                    minIndex = j;
                }
            }
            extremas[i][0] = extremum;
            indices[i][0] = (float) minIndex;
        }
        return new Tuple<>(new BMatrix(extremas), new BMatrix(indices));
    }

    private BMatrix multiplyMatrix(BMatrix other) {
        float[][] res = createfloatArray(m, other.cols());
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < other.cols(); j++) {
                float sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += mat[i][k] * other.mat[k][j];
                }
                res[i][j] = sum;
            }
        }

        return new BMatrix(res);
    }

}
