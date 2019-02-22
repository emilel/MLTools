package LinearAlgebra;

import Misc.Tuple;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for two dimensional, immutable matrices. All methods expect the matrices to use floats for internal
 * representation. Elements are accessed by naming the row first, then column. Matrices are of dimension m x n.
 */
public interface Matrix {

    /**
     * Returns the number of rows in this Matrix.
     * @return the number of rows in this Matrix.
     */
    int rows();

    /**
     * Returns the number of columns in this Matrix.
     * @return the number of columns in this Matrix.
     */
    int cols();

    /**
     * Returns a Tuple representing the size of this Matrix (rows, columns).
     * @return a Tuple representing the size of this Matrix (rows, columns).
     */
    Tuple<Integer> size();

    /**
     * Copies this Matrix.
     * @return a copy of this Matrix.
     */
    Matrix copy();

    /**
     * Concatenates this Matrix with the other Matrix horizontally.
     * @param other m x q Matrix (number of rows must be equal to the number of rows in this Matrix).
     * @return m x (n + q) Matrix. This Matrix, followed by the other Matrix to the right.
     */
    Matrix conch(Matrix other);

    /**
     * Concatenates this Matrix with the other Matrix vertically.
     * @param other p x n Matrix (number of columns must be equal to the number of columns in this Matrix).
     * @return (m + p) x n Matrix. This Matrix, followed by the other Matrix below.
     */
    Matrix concv(Matrix other);

    /**
     * Returns the element at the given index.
     * @param row which row the wanted element is at
     * @param col which column the wanted element is at
     * @return the primitive float located at the index.
     */
    float get(int row, int col);

    /**
     * For 1x1 Matrices, this method returns the only element as a primitive float.
     * @return the element in this 1x1 Matrix
     */
    float toFloat();

    /**
     * Returns the 1 x n row Matrix at the given index.
     * @param row the wanted row.
     * @return the 1 x n row Matrix at the given index.
     */
    Matrix row(int row);

    /**
     * Returns the m x 1 column Matrix at the given index.
     * @param col the wanted column.
     * @return the m x 1 column Matrix at the given index.
     */
    Matrix col(int col);

    /**
     * Returns a Matrix with n columns containing all rows given by the argument.
     * @param rows the indices of the rows to return.
     * @return a Matrix with n columns containing the rows in argument.
     */
    Matrix rows(int... rows);

    /**
     * Returns a Matrix with m rows containing all columns given by argument.
     * @param cols the indices of the columns to return.
     * @return a Matrix with m rows containing the columns in argument.
     */
    Matrix cols(int... cols);

    /**
     * Returns a Matrix with (to - from + 1) rows and n columns, containing every row in the range given by argument.
     * @param from the first row to include.
     * @param to the last to include.
     * @return a Matrix with (to - from + 1) rows and n columns, containing every row in the range.
     */
    Matrix rowr(int from, int to);

    /**
     * Returns a Matrix with m rows and (to - from + 1) columns, containing every column in the range given by argument.
     * @param from the first column to include.
     * @param to the last column to include.
     * @return a Matrix with m rows and (to - from + 1) columns, containing every column in the range.
     */
    Matrix colr(int from, int to);

    /**
     * Returns a rectangular submatrix of this matrix, with (toRow - fromRow + 1) rows and (toCol - fromCol + 1) columns.
     * @param fromRow the first row to include
     * @param toRow the last row to include
     * @param fromCol the first column to include
     * @param toCol the last column to include
     * @return a rectangular submatrix of this matrix
     */
    Matrix subm(int fromRow, int toRow, int fromCol, int toCol);

    /**
     * Copies this matrix, and inserts the element at the given index.
     * @param row the row where the element should be inserted.
     * @param col the column where the element should be inserted.
     * @param element the element to insert
     * @return a m x n Matrix with the given element at the given index.
     */
    Matrix ins(int row, int col, float element);

    /**
     * Copies this matrix, and inserts the given submatrix starting at the given index (left corner).
     * @param fromRow the row where the submatrix starts being inserted.
     * @param fromCol the column where the submatrix starts being inserted.
     * @param other the submatrix to insert
     * @return a m x n Matrix with the other matrix inserted, starting at the given index.
     */
    Matrix ins(int fromRow, int fromCol, Matrix other);

    /**
     * Finds the minimum value in every row.
     * @return a Tuple<Matrix> of m x 1 Matrices. The first Matrix contains the minimum values, and the second Matrix
     * contains the indices of the minimum values.
     */
    Tuple<Matrix> minh();

    /**
     * Finds the minimum value in every column.
     * @return a Tuple<Matrix> of 1 x n Matrices. The first Matrix contains the minimum values, and the second Matrix
     * contains the indices of the minimum values.
     */
    Tuple<Matrix> minv();

    /**
     * Finds the maximum value in every row.
     * @return a Tuple<Matrix> of m x 1 Matrices. The first Matrix contains the minimum values, and the second Matrix
     * contains the indices of the minimum values.
     */
    Tuple<Matrix> maxh();

    /**
     * Finds the maximum value in every column.
     * @return a Tuple<Matrix> of 1 x n Matrices. The first Matrix contains the minimum values, and the second Matrix
     * contains the indices of the minimum values.
     */
    Tuple<Matrix> maxv();

    /**
     * Adds this Matrix with the other elementwise, or if the argument is a scalar, adds the scalar to every element.
     * @param other the m x n Matrix, or the 1 x 1 scalar, to add
     * @return a m x n Matrix where every element is the sum of the corresponding elements in the terms
     */
    Matrix add(Matrix other);

    /**
     * Adds a row Matrix to every row in this matrix.
     * @param row the 1 x n row Matrix to add
     * @return a m x n Matrix where every row is the sum of the corresponding row in this matrix and the argument row
     * matrix
     */
    Matrix addr(Matrix row);

    /**
     * Adds a column Matrix to every column in this matrix.
     * @param column the m x 1 column Matrix to add.
     * @return a m x n Matrix where every row is the sum of the corresponding column in this matrix and the argument
     * column matrix.
     */
    Matrix addc(Matrix column);

    /**
     * Adds a scalar to every element in this Matrix.
     * @param scalar the scalar to add.
     * @return a m x n Matrix where every element is the sum of the corresponding element in this Matrix and the scalar.
     */
    Matrix add(float scalar);

    /**
     * Subtracts this Matrix with another Matrix of the same size, or if the argument is a scalar, subtracts the scalar
     * from every element.
     * @param Matrix the m x n to subtract, or the 1 x 1 scalar, to subtract
     * @return a m x n where every element is the difference between the corresponding elements in this Matrix and the
     * argument Matrix.
     */
    Matrix sub(Matrix Matrix);

    /**
     * Subtracts a row Matrix from every row in this matrix.
     * @param row the 1 x n row Matrix to subtract.
     * @return a m x n Matrix where every row is the difference of the corresponding row in this matrix and the argument
     * row matrix.
     */
    Matrix subr(Matrix row);

    /**
     * Subtracts a column Matrix from every column in this matrix.
     * @param column the m x 1 column Matrix to subtract.
     * @return a m x n Matrix where every row is the difference of the corresponding column in this matrix and the
     * argument column matrix.
     */
    Matrix subc(Matrix column);

    /**
     * Subtracts a scalar from every element in this Matrix.
     * @param scalar the scalar to subtract.
     * @return a m x n Matrix where every element is the difference of the corresponding element in this Matrix and the
     * scalar.
     */
    Matrix sub(float scalar);

    /**
     * Multiplies this Matrix with the argument Matrix or scalar
     * @param other a n x q Matrix, or a 1x1 scalar
     * @return a m x q Matrix which is the result of the multiplication, or a m x n Matrix if the argument was a scalar
     */
    Matrix mul(Matrix other);

    /**
     * Multiplies this Matrix with a scalar.
     * @param scalar the scalar to multiply with
     * @return a m x n Matrix where every element is the product of the corresponding element in this Matrix and the
     * scalar
     */
    Matrix mul(float scalar);

    /**
     * Multiplies this Matrix with a row Matrix. The first element in every row is multiplied with the first element
     * in the argument row Matrix, and the second element in every row is multiplied with the second element in the
     * argument row Matrix etc.
     * @param row the 1 x n row Matrix to multiply with.
     * @return a m x n Matrix which is the result of the multiplication.
     */
    Matrix mulr(Matrix row);

    /**
     * Multiplies this Matrix with a column Matrix. The first element in every column is multiplied with the first
     * element in the argument column Matrix, and the second element in every column is multiplied with the second
     * element in the argument column Matrix etc.
     * @param column the m x 1 column Matrix to multiply with.
     * @return a m x n Matrix which is the result of the multiplication.
     */
    Matrix mulc(Matrix column);

    /**
     * Multiplies this Matrix with the argument Matrix elementwise.
     * @param other the m x n Matrix to do elementwise multiplication with.
     * @return a m x n Matrix where every element is the product of the corresponding elements in this Matrix and the
     * argument Matrix.
     */
    Matrix mulew(Matrix other);

    /**
     * Divides this Matrix with a scalar.
     * @param scalar the scalar to divide with.
     * @return a m x n Matrix where every element is the result of the division between the corresponding element in
     * this Matrix and the scalar.
     */
    Matrix div(float scalar);

    /**
     * Divides this Matrix with a row Matrix. See method mulr(Matrix row).
     * @param row the 1 x n row Matrix to divide with
     * @return a m x n Matrix which is the result of the division.
     */
    Matrix divr(Matrix row);

    /**
     * Divides this Matrix with a column Matrix. See method mulc(Matrix column).
     * @param Matrix the m x 1 column Matrix to divide with.
     * @return a m x n Matrix which is the result of the division.
     */
    Matrix divc(Matrix Matrix);

    /**
     * Divides this Matrix with the argument Matrix elementwise.
     * @param other the m x n Matrix to divide with.
     * @return a m x n Matrix where every element is the result of the division between the corresponding elements in
     * this Matrix and the argument Matrix.
     */
    Matrix divew(Matrix other);

    /**
     * Computes the sum of every row (iterates the Matrix horizontally).
     * @return a 1 x n column Matrix where every element is the sum of the elements in the corresponding row.
     */
    Matrix sumh();

    /**
     * Computes the sum of every column (iterates the Matrix vertically).
     * @return a 1 x m row Matrix where every element is the sum of the elements in the corresponding column.
     */
    Matrix sumv();

    /**
     * Computes the sum of every element in this Matrix.
     * @return a 1x1 Matrix (scalar) containing the sum of every element in this Matrix.
     */
    Matrix sum();

    /**
     * Computes the sum of every element squared in this Matrix.
     * @return a 1 x 1 Matrix (scalar) containing the sum of every element squared in this Matrix.
     */
    Matrix sumsqr();

    /**
     * Computes the inverse of this Matrix (needs to be quadratic).
     * @return the m x m inverse of this Matrix.
     */
    Matrix inv();

    /**
     * Computes the pseudo inverse of this Matrix (can be rectangular).
     * @return the n x m pseudo inverse of this Matrix.
     */
    Matrix pinv();

    /**
     * Computes the transpose of this Matrix.
     * @return the n x m transpose of this Matrix.
     */
    Matrix tran();

    /**
     * Creates a diagonal Matrix containing the elements in the diagonal of this Matrix.
     * @return a diagonal Matrix containing the elements in the diagonal of this Matrix.
     */
    Matrix diag();

    /**
     * Turns this Matrix into a column Matrix, column by column.
     * @return a (m * n) x 1 column Matrix containing every element in this Matrix.
     */
    Matrix vec();

    /**
     * Iterates over the Matrix, applying a function to every element and returning the result.
     * @param function the function to apply.
     * @return a m x n Matrix where the function has been applied to every element in this Matrix.
     */
    Matrix map(Function<Float, Float> function);

    /**
     * Creates a Matrix of 1's (true) and 0's (false) depending on whether the predicate held true or not for the
     * corresponding element in this Matrix.
     * @param pred the predicate used.
     * @return a m x n Matrix of 1's and 0's
     */
    Matrix mask(Predicate<Float> pred);

    /**
     * Uses a mask of 1's and 0's of the same size as this Matrix, and returns a column Matrix containing every element
     * for which the corresponding mask element was 1.
     * @param mask the mask to use.
     * @return a column Matrix of values from this Matrix, for which the predicate held true.
     */
    Matrix usemask(Matrix mask);

    /**
     * Creates a copy of this matrix, and adds a row of ones to the top of that Matrix.
     * @return a (m + 1) x n Matrix, where the top row is ones and the rest is identical to this Matrix.
     */
    Matrix addr1();

    /**
     * Creates a copy of this matrix, and adds a column of ones to the left of that Matrix.
     * @return a m x (n + 1) Matrix where the left column is ones and the rest is identical to this Matrix.
     */
    Matrix addc1();

    /**
     * Removes the top row of this Matrix.
     * @return a (m - 1) x n Matrix with the same elements as this (except the top row).
     */
    Matrix rtr();

    /**
     * Removes the left column of this Matrix.
     * @return a m x (n - 1) Matrix with the same elements as this (except the left column).
     */
    Matrix rlc();

    /**
     * Finds every unique value in this Matrix.
     * @return a Set containing every unique value in this Matrix.
     */
    Set<Float> unq();

    /**
     * Performs singular value decomposition of this Matrix.
     * @return a Tuple containing the U, S and V matrices.
     */
    Tuple<Matrix> svd();

    /**
     * Returns a String representing this Matrix, separated by the delimiters given as arguments.
     * @param colDelimiter the character between columns.
     * @param rowDelimiter the character between rows.
     * @return a String representing this Matrix.
     */
    String toString(char colDelimiter, char rowDelimiter);

    /**
     * Returns a String representing this Matrix, using the standard delimiters.
     * @return a String representing this Matrix, using the standard delimiters.
     */
    @Override
    String toString();

    /**
     * Compares this Matrix with another Object, and determines whether they should be considered equal. The other
     * object needs to be a Matrix of the same type and size, and at every index, the difference must not exceed the
     * limit given by the argument.
     * @param other the Object to compare with.
     * @param tolerance the maximum allowed difference between two elements with the same indices, that still results
     *                  in the Matrices being considered equal.
     * @return whether the Matrices are equal or not.
     */
    boolean equals(Object other, float tolerance);

    /**
     * Compares this Matrix with another Object, and determines whether they should be considered equal. The maximum
     * allowed difference is the standard tolerance. See method equals(Object other, float tolerance).
     * @param other the Object to compare with.
     * @return whether the Matrices are equal or not.
     */
    @Override
    boolean equals(Object other);

    /**
     * Compares this Matrix with another Matrix, and determines which elements at the same indexes are equal (1) and
     * which are not (0). The maximum allowed difference is the standard tolerance.
     * @param other the m x n Matrix to compare with.
     * @return a m x n Matrix of 1's and 0's.
     */
    Matrix equalsEw(Matrix other);
}
