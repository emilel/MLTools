package LinearAlgebra.BLinAlg;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixException;
import LinearAlgebra.MatrixFactory;
import Misc.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 class for testing the BMatrix class.
 */
class BMatrixTest {
    private static MatrixFactory mf;
    private static Matrix tbt1;
    private static Matrix tbt2;
    private static Matrix tbt3;
    private static Matrix fbt;
    private static Matrix tbf;

    @BeforeAll
    static void setUp() {
        mf = new BMatrixFactory(' ', ';');
        tbt1 = mf.m("0 1 -2;3 4 5;6 7 8");
        tbt2 = mf.m("5 2 4;3 4 5;-800 0 -1");
        tbt3 = mf.m("10 30 2;10 10 -100;10 0 0");
        fbt = mf.m("5 2 1;5 1 4;9 9 10;4 1 -100;-1 20 20");
        tbf = mf.m("10 20 30 40 50;1 2 3 4 5;-1 -2 -3 -4 -5");
    }

    @Test
    void testCopy() {
        Matrix mat = tbt1.copy();
        assertEquals(tbt1, mat, "Copied matrix not equal to original");
        assertNotSame(tbt1, mat, "Copied matrix refers to the original");

    }

    @Test
    void testEquals() {
        Matrix mat = tbt1.copy();
        assertEquals(tbt1, mat, "Equal matrices not equal");
        assertEquals(mat, tbt1, "Equal matrices not equal");
        assertNotEquals(tbt1, tbt2, "Non equal matrices equal");
        assertNotEquals(tbt1, "not a matrix", "Matrix is equal to non matrix");
        assertNotEquals(tbt1.conch(mf.m("0;0;0")), tbt1, "Matrices of different " +
                "sizes should never be equal");
    }

    @Test
    void testExactEquals() {
        Matrix mat = tbt1.copy();
        assertTrue(tbt1.equals(mat, 0f), "Equal matrices not equal");
        assertTrue(mat.equals(tbt1, 0f), "Equal matrices not equal");
        assertFalse(tbt1.equals(tbt2, 0f), "Non equal matrices equal");
        assertFalse(tbt1.equals("not a matrix", 0f), "Matrix is equal to non matrix");
        assertFalse(tbt1.conch(mf.m("0;0;0")).equals(tbt1, 0f), "Matrices of different " +
                "sizes should never be equal");
    }

    @Test
    void testAlmostEquals() {
        Matrix almostTbt1 = mf.m("1 1 -1;3 4 4;5 8 9");
        Matrix notTbt1 = mf.m("0 1 -4;3 4 5;6 7 8");
        assertTrue(tbt1.equals(almostTbt1, 1f), "Matrices within the equality tolerance should be " +
                "equal");
        assertFalse(tbt1.equals(notTbt1, 1f), "Matrices whose difference are outside the" +
                " equality tolerance should not be equal");
    }

    @Test
    void testGet() {
        Matrix mat = mf.m("0 1 0;0 -1 0;1 0 0");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i == 0 && j == 1 && mat.get(i, j) != 1f) ||
                        (i == 1 && j == 1 && mat.get(i, j) != -1f) ||
                        (i == 2 && j == 0 && mat.get(i, j) != 1f)) {
                    fail("Get method not working");
                }
            }
        }
    }

    @Test
    void testSize() {
        assertEquals(tbt1.size(), new Tuple<>(3, 3), "Wrong size is returned by size()");
        assertEquals(fbt.size(), new Tuple<>(5, 3), "Wrong size is returned by size()");
    }

    @Test
    void testAdd() {
        Matrix sum1 = tbt1.add(tbt2);
        Matrix sum2 = tbt2.add(tbt1);
        Matrix ans = mf.m("5 3 2;6 8 10;-794 7 7");

        assertEquals(sum1, ans, "Sum of two matrices not equal");
        assertEquals(sum2, ans, "Sum of two matrices not equal");
        assertEquals(sum1, sum2, "Sum of two matrices in different order not equal");
        assertThrows(MatrixException.class, () -> tbt1.add(fbt), "Trying to add different size matrices " +
                "does not throw exception");
        assertEquals(mf.m("6 3 2;6 2 5;10 10 11;5 2 -99;0 21 21"), fbt.add(mf.m("1")),
                "Adding a 1x1 matrix should be treated as addition with a scalar");
        assertEquals(mf.m("6 3 2;6 2 5;10 10 11;5 2 -99;0 21 21"), mf.m("1").add(fbt),
                "Adding a 1x1 matrix should be treated as addition with a scalar");
    }

    @Test
    void matrixAdditionShouldBeCommutative() {
        assertEquals(tbt1.add(tbt2.add(tbt3)), tbt2.add(tbt3.add(tbt1)), "Addition of matrices not " +
                "commutative");
        assertEquals(tbt3.add(tbt2.add(tbt1)), tbt1.add(tbt3.add(tbt2)), "Addition of matrices not " +
                "commutative");
    }

    @Test
    void concatenateMatrices() {
        assertEquals(mf.m("0 1 -2 5 2 4;3 4 5 3 4 5;6 7 8 -800 0 -1"),
                tbt1.conch(tbt2),
                "Error concatenating two matrices of equal size horizontally");
        assertEquals(mf.m("0 1 -2;3 4 5;6 7 8;5 2 4;3 4 5;-800 0 -1"),
                tbt1.concv(tbt2),
                "Error concatenating two matrices of equal size vertically");
        assertEquals(mf.m("0 1 -2 10 20 30 40 50;3 4 5 1 2 3 4 5;6 7 8 -1 -2 -3 -4 -5"),
                tbt1.conch(tbf),
                "Error concatenating two matrices of different but matching size horizontally");
        assertEquals(mf.m("5 2 1;5 1 4;9 9 10;4 1 -100;-1 20 20;5 2 4;3 4 5;-800 0 -1"),
                fbt.concv(tbt2),
                "Error concatenating two matrices of different but matching size vertically");
        assertThrows(MatrixException.class, () -> tbt1.conch(fbt), "Trying to conc " +
                "mismatching matrices should throw an exception");
        assertThrows(MatrixException.class, () -> tbf.concv(tbt1), "Trying to conc " +
                "mismatching matrices should throw an exception");
    }

    @Test
    void testAddOnes() {
        Matrix res1 = fbt.addr1();
        Matrix res2 = fbt.addc1();

        assertEquals(mf.m("1 1 1;5 2 1;5 1 4;9 9 10;4 1 -100;-1 20 20"),
                res1, "Error adding ones to the top row of matrix");
        assertEquals(mf.m("1 5 2 1;1 5 1 4;1 9 9 10;1 4 1 -100;1 -1 20 20"),
                res2, "Error adding ones to the bottom row of matrix");
    }

    @Test
    void testGetRow() {
        assertEquals(mf.m("0 1 -2"),
                tbt1.row(0), "Error getting row");
        assertEquals(mf.m("-1 -2 -3 -4 -5"),
                tbf.row(2), "Error getting row");
        assertThrows(MatrixException.class, () -> tbt2.row(3), "Not throwing Exception while trying to " +
                "get row out of bounds");
    }

    @Test
    void testGetColumn() {
        assertEquals(mf.m("2;4;0"),
                tbt2.col(1), "Error getting column");
        assertEquals(mf.m("2;1;9;1;20"),
                fbt.col(1), "Error getting column");
        assertThrows(MatrixException.class, () -> tbt2.col(-1), "Not throwing Exception while " +
                "get row out of bounds");
    }

    @Test
    void testGetRows() {
        assertEquals(mf.m("5 2 1;4 1 -100;-1 20 20"),
                fbt.rows(0, 3, 4), "Error getting multiple rows");
        assertEquals(mf.m("3 4 5"),
                tbt1.rows(1), "Error getting one row from rows(int... rows)");
        assertThrows(MatrixException.class, () -> tbt2.rows(0, 3),
                "Not throwing Exception when trying to get at least one out of bounds row");
    }

    @Test
    void TestGetColumns() {
        assertEquals(mf.m("20;2;-2;"),
                tbf.cols(1), "Error getting one column using cols(int... cols)");
        assertEquals(mf.m("5 2;3 4;-800 0"),
                tbt2.cols(0, 1), "Error getting multiple columns");
        assertThrows(MatrixException.class,
                () -> tbt1.cols(-1, 0), "Error getting at least one out of bounds row");
    }

    @Test
    void TestMinimum() {
        Tuple<Matrix> res1 = tbt1.minh();
        Matrix minElements1 = res1.get(0);
        Matrix minIndices1 = res1.get(1);
        Tuple<Matrix> res2 = fbt.minv();
        Matrix minElements2 = res2.get(0);
        Matrix minIndices2 = res2.get(1);

        assertEquals(mf.m("-2;3;6"),
                minElements1, "Error getting min values from each row");
        assertEquals(mf.m("2;0;0"),
                minIndices1, "Error getting row indices of the min values from each row");
        assertEquals(mf.m("-1 1 -100"),
                minElements2, "Error getting min values from each column");
        assertEquals(mf.m("4 1 3"),
                minIndices2, "Error getting row indices of the min values from each column");
    }

    @Test
    void testMaximum() {
        Tuple<Matrix> res1 = tbt2.maxh();
        Matrix minElements1 = res1.get(0);
        Matrix minIndices1 = res1.get(1);
        Tuple<Matrix> res2 = fbt.maxv();
        Matrix minElements2 = res2.get(0);
        Matrix minIndices2 = res2.get(1);

        assertEquals(mf.m("5;5;0"),
                minElements1, "Error getting max values from each row");
        assertEquals(mf.m("0;2;1"),
                minIndices1, "Error getting row indices of the max values from each row");
        assertEquals(mf.m("9 20 20"),
                minElements2, "Error getting max values from each column");
        assertEquals(mf.m("2 4 4"),
                minIndices2, "Error getting row indices of the max values from each column");
    }

    @Test
    void testToElement() {
        assertEquals(5f, mf.m("5").toFloat(), "toFloat() " +
                "does not return element of 1x1 matrix");
        assertEquals(4f, tbt1.row(1).col(1).toFloat(), "toFloat() does not " +
                "return element of 1x1 matrix");
        assertThrows(MatrixException.class, () -> tbt2.toFloat(), "toFloat() does not throw Exception " +
                "when called on non 1x1 matrix");
    }

    @Test
    void testSum() {
        assertEquals(32f, tbt1.sum().toFloat(), "sum() returning wrong sum");
        assertEquals(150f, tbf.sum().toFloat(), "sum() returning wrong sum");
        assertEquals(0f, mf.m("0").sum().toFloat(), "sum() returning wrong sum" +
                " from 1x1 matrix");
    }

    @Test
    void testSumOfSquares() {
        assertEquals(11304f, tbt3.sumsqr().toFloat(), "sumsqr() returning wrong sum");
        assertEquals(4f, mf.m("-2").sumsqr().toFloat(),
                "sumsqr returning wrong sum of 1x1 matrix");
    }

    @Test
    void testApply() {
        assertEquals(mf.m("10 20 30 40 50;1 2 3 4 5;1 2 3 4 5"),
                tbf.map(Math::abs), "Failed to apply Mats.abs(d)");
        assertEquals(mf.m("8 5 7;6 7 8;-797 3 2"),
                tbt2.map(d -> d + 3), "Failed to add 3 to every element");
    }

    @Test
    void testSubtractWithMatrix() {
        assertEquals(mf.m("-5 -1 -6;0 0 0;806 7 9"),
                tbt1.sub(tbt2), "Error subtracting two matrices");
        assertThrows(MatrixException.class, () -> tbt1.sub(fbt), "Trying to sub two different " +
                "sized matrices (neither row or vector) does not throw exception");
        assertEquals(mf.m("-2 -1 -4;1 2 3;4 5 6"), tbt1.sub(mf.m("2")),
                "Subtraction with a 1x1 matrix should" +
                        " be treated as subtraction with a scalar");
    }

    @Test
    void testAddRow() {
        assertEquals(mf.m("0 2 0;3 5 7;6 8 10"), tbt1.addr(mf.m("0 1 2")), "Adding a " +
                "row vector to a matrix should result in adding the row to every row in the matrix");
    }

    @Test
    void testAddColumn() {
        assertEquals(mf.m("0 1 -2;4 5 6;8 9 10"), tbt1.addc(mf.m("0;1;2")),
                "Adding a column vector to a matrix should add the row to every column in the matrix");
    }

    @Test
    void testSubtractRow() {
        assertEquals(mf.m("5 1 2;3 3 3;-800 -1 -3"), tbt2.subr(mf.m("0 1 2")),
                "Subtracting a row vector should sub the row vector from every row");
    }

    @Test
    void testSubtractColumn() {
        assertEquals(mf.m("5 2 4;2 3 4;-802 -2 -3"), tbt2.subc(mf.m("0;1;2")),
                "Subtracting a column vector should sub it from every column");
    }

    @Test
    void testVectorize() {
        assertEquals(mf.m("10;1;-1;20;2;-2;30;3;-3;40;4;-4;50;5;-5"), tbf.vec(),
                "Error vectorizing a matrix");
        assertEquals(mf.m("0;1;2;3"),
                mf.m("0 1 2 3").vec(),
                "Error vectorizing a one row matrix");
        assertEquals(mf.m("10;11;12;13"),
                mf.m("10;11;12;13").vec(), "Error vectorizing a vector");
    }

    @Test
    void testTranspose() {
        assertEquals(mf.m("10 1 -1;20 2 -2;30 3 -3;40 4 -4;50 5 -5"),
                tbf.tran(), "Error transposing a 2x5 matrix");
        assertEquals(mf.m("10 10 10;30 10 0;2 -100 0"),
                tbt3.tran(), "Error transposing a 3x3 matrix");
    }

    @Test
    void testMultiplyWithScalar() {
        assertEquals(mf.m("-7.5 -3 -6;-4.5 -6 -7.5;1200 -0 1.5"),
                tbt2.mul(-1.5f), "Error multypling matrix with scalar");
        assertEquals(mf.m("-7.5 -3 -6;-4.5 -6 -7.5;1200 -0 1.5"), mf.s(-1.5f).mul(tbt2),
                "Error multiplying scalar with a matrix");
    }

    @Test
    void testMultiplyMatrices() {
        assertEquals(mf.m("1603 4 7;-3973 22 27;-6349 40 51"),
                tbt1.mul(tbt2), "Error multiplying two matrices of equal size");
        assertEquals(mf.m("-1460 140 188;80080 60 190;50 20 40"),
                tbt3.mul(tbt2), "Error multiplying two matrices of equal size");
        assertEquals(mf.m("51 102 153 204 255;47 94 141 188 235;89 178 267 356 445;" +
                        "141 282 423 564 705;-10 -20 -30 -40 -50"),
                fbt.mul(tbf), "Error multiplying a 5x3 matrix with a 3x5 matrix");
        assertEquals(mf.m("48 96 144 192 240;29 58 87 116 145;-7999 -15998 -23997 -31996 -39995"),
                tbt2.mul(tbf), "Error multiplying a 3x3 matrix with a 3x5 matrix");
        assertEquals(mf.m("-4.5;24;40.5"), tbt1.mul(mf.m("1;1.5;3")),
                "Error multiplying 3x3 matrix with a 3x1 matrix");
        assertEquals(mf.m("0 0 0;0 0 0;0 0 0"), tbt1.mul(mf.m("0")),
                "Multiplication with a 1x1 matrix should be treated as scalar multiplication");
        assertThrows(MatrixException.class, () -> tbt1.mul(fbt), "Not throwing exception when " +
                "multiplying matrices of incompatible sizes");
    }

    @Test
    void testMultiplyRow() {
        assertEquals(mf.m("0 -1 4;0 -4 -10;0 -7 -16"), tbt1.mulr(mf.m("0 -1 -2")),
                "Multiplying with a row should mul every column in the matrix the corresponding row " +
                        "element");
    }

    @Test
    void testMultiplyColumn() {
        assertEquals(mf.m("10 20 30 40 50;1.5 3 4.5 6 7.5;-3.0 -6.0 -9.0 -12.0 -15.0"),
                tbf.mulc(mf.m("1;1.5;3")), "Multiplying with a column should mul every row " +
                        "with the corresponding element in the column");
    }

    @Test
    void testMultiplyElementWise() {
        assertEquals(mf.m("0 2 -8;9 16 25;-4800 0 -8"),
                tbt1.mulew(tbt2), "Error multiplying two 3x3 matrices elementwise");
        assertThrows(MatrixException.class, () -> tbt1.mulew(tbf),
                "Not throwing exception when trying to mul incompatible matrices elementwise");
    }

    @Test
    void testRemoveTopRow() {
        assertEquals(mf.m("3 4 5;6 7 8"),
                tbt1.rtr(), "Error removing top row from matrix");
        assertThrows(MatrixException.class, () -> mf.m("1 1 1").rtr(),
                "Removing row from 1xN matrix should throw exception");
    }

    @Test
    void testRemoveLeftColumn() {
        assertEquals(mf.m("2 1;1 4;9 10;1 -100;20 20"),
                fbt.rlc(), "Error removing left column");
        assertThrows(MatrixException.class, () -> mf.m("1;1;1").rlc(),
                "Removing column from a Nx1 matrix should throw exception");
    }

    @Test
    void testPseudoInverse() {
        Matrix tbt1PseudoInverse = mf.m("-0.25 -1.8333 1.0833;0.5000 1 -0.5;-0.25 0.5 -0.25");
        assertEquals(tbt1.pinv(), tbt1PseudoInverse, "Error finding the 'pseudo' inv of a 3x3 matrix");
        Matrix fbtPseudoInverse = mf.m("0.0383 0.0408 0.0627 0.0015 -0.0342;" +
                "-0.0033 -0.0061 0.0061 0.0098 0.0474;0.0013 0.0018 0.0025 -0.0098 -0.0009");
        assertEquals(fbt.pinv(), fbtPseudoInverse, "Error finding the pseudo pseudo inv of a 5x3 matrix");
        Matrix tbfPseudoInverse = mf.m("0.0018 0.0002 -0.0002;0.0036 0.0004 -0.0004;" +
                "0.0053 0.0005 -0.0005;0.0071 0.0007 -0.0007;0.0089 0.0009 -0.0009");
    }

    @Test
    void testDiagonal() {
        assertEquals(mf.m("5;1;10"), fbt.diag(), "Error finding the diag of 5x3 matrix");
        assertEquals(mf.m("10;2;-3"), tbf.diag(), "Error finding the diag of 3x5 matrix");
        assertEquals(mf.m("0;4;8"), tbt1.diag(), "Error findinf the diag of 3x3 matrix");
    }

    @Test
    void testInverse1() {
        Matrix fbf = fbt.conch(tbt2.cols(0, 1).concv(
                tbt1.cols(1, 2).rows(0, 1)));
        Matrix fbfInverse = fbf.inv();
        Matrix ans1 = mf.m("0.3359 -0.1379 0.0014 -0.0074 -0.0270;0.1105 -0.1072 0.0005 0.0056 0.0438;" +
                "0.0241 -0.0160 0.0001 -0.0106 -0.0011;0.0053 -0.0030 -0.0012 -0.0002 0.0002;" +
                "-0.4756 0.4674 -0.0011 0.0186 0.0237");

        assertEquals(fbfInverse, ans1, "Error finding the inv of a" +
                " 5x5 matrix");
        assertThrows(MatrixException.class, () -> tbf.inv(), "Trying to invert a non square matrix should throw an " +
                "exception");
        assertThrows(MatrixException.class, () -> fbt.inv(), "Trying to invert a non square matrix should throw an " +
                "exception");
    }

    @Test
    void testInverse2() {
        Matrix tbt1Inverse = tbt1.inv();
        Matrix ans = mf.m("-0.25 -1.8333 1.0833;0.5 1 -0.5;-0.25 0.5 -0.25");
        assertEquals(tbt1Inverse, ans, "Error finding the inv of 3x3 matrix");
    }

    @Test
    void testGetColumnRange() {
        assertEquals(mf.m("0 1 -2;3 4 5;6 7 8"), tbt1.colr(0, 2), "Error getting full" +
                " column range of 3x3 matrix");
        assertEquals(mf.m("20 30 40;2 3 4;-2 -3 -4"), tbf.colr(1, 3), "Error getting " +
                "column range");
        assertThrows(MatrixException.class, () -> tbt2.colr(3, 4), "Not throwing exception when" +
                " trying go get a column range out of bounds");
    }

    @Test
    void testGetRowRange() {
        fbt = mf.m("5 2 1;5 1 4;9 9 10;4 1 -100;-1 20 20");
        assertEquals(mf.m("3 4 5"), tbt2.rowr(1, 1), "Error getting range of one row");
        assertEquals(mf.m("4 1 -100;-1 20 20"), fbt.rowr(3, 4), "Error getting row range");
        assertThrows(MatrixException.class, () -> tbt3.rowr(-1, 0), "Not throwing exception when " +
                "trying to get a row range out of bounds");
    }

    @Test
    void testSetMultiple() {
        fbt = fbt.ins(0, 0, tbt1);
        assertEquals(mf.m("0 1 -2;3 4 5;6 7 8;4 1 -100;-1 20 20"), fbt,
                "Error setting multiple values");
        Matrix fbf = fbt.conch(tbt2.cols(0, 1).concv(
                tbt1.cols(1, 2).rows(0, 1)));
        fbf = fbf.ins(1, 1, tbt2);
        assertEquals(mf.m("0 1 -2 5 2;3 5 2 4 4;6 3 4 5 0;4 -800 0 -1 -2;-1 20 20 4 5"),
                fbf, "Error setting multiple values");
        assertThrows(MatrixException.class, () -> ((BMatrix) tbt1).ins(1, 0, tbt2),
                "An exception should be thrown when trying to ins a matrix that is too large");
        assertThrows(MatrixException.class, () -> ((BMatrix) tbt1).ins(0, 1, tbt2),
                "An exception should be thrown when trying to ins a matrix that is too large");
    }

    @Test
    void testAdditionWithScalar() {
        assertEquals(mf.m("1 2 -1;4 5 6;7 8 9"), tbt1.add(1f), "Error adding a scalar to a matrix");
    }

    @Test
    void testSubtractionWithScalar() {
        assertEquals(mf.m("5 25 -3;5 5 -105;5 -5 -5"), tbt3.sub(5f), "Error subtracting a " +
                "scalar from a matrix");
    }

    @Test
    void testCreateMask() {
        assertEquals(mf.m("1 0 0;1 0 1;1 1 1;1 0 0;0 1 1"), fbt.mask(d -> d > 2), "Error creating " +
                "mask based on predicate");
    }

    @Test
    void testUseMask() {
        assertEquals(mf.m("10;-1;2;3;-3;40;50;-5"),
                tbf.usemask(mf.m("1 0 0 1 1;0 1 1 0 0;1 0 1 0 1")), "Error using mask");
    }

    @Test
    void testGetSub() {
        assertEquals(mf.m("1 4;9 10;1 -100;20 20"), fbt.subm(1, 4, 1, 2),
                "Error getting submatrix");
    }

    @Test
    void testHorizontalSum() {
        assertEquals(mf.m("8;10;28;-95;39"), fbt.sumh(), "Error finding horizontal sum");
    }

    @Test
    void testVerticalSum() {
        assertEquals(mf.m("30 40 -98"), tbt3.sumv(), "Error finding vertical sum");
    }

    @Test
    void testDivide() {
        assertEquals(mf.m("5 15 1;5 5 -50;5 0 0"), tbt3.div(2f), "Error dividing with a scalar");
    }

    @Test
    void testDivideColumn() {
        assertEquals(mf.m("5 2 4;1.5 2 2.5;-266.6667 0 -0.3333"), tbt2.divc(mf.m("1;2;3")),
                "Dividing with a column should div every row with the corresponding element in the column");
    }

    @Test
    void testDivideRow() {
        assertEquals(mf.m("5 1 1.33333;3 2 1.66666;-800 0 -0.33333"), tbt2.divr(mf.m("1 2 3")),
                "Dividing with a row should div every column with the corresponding element in the row");
    }

    @Test
    void TestDivideElementwise() {
        assertEquals(mf.m("5 1 2;1 2 5;-400 0 20"), tbt2.divew(mf.m("1 2 2;3 2 1;2 20 -0.05")),
                "Error performing elementwise division");
    }

    @Test
    void testEqualsEw() {
        assertEquals(mf.m("1 1 0;0 1 0;0 0 1"), tbt1.equalsEw(mf.m("0 1 100;100 4 100;100 100 8")),
                "Error comparing two matrices elementwise");
    }

    @Test
    void testFindUniqueValues() {
        Set<Float> values = new HashSet<>();
        Collections.addAll(values, 5f, 2f, 1f, 4f, 9f, 10f, -100f, -1f, 20f);
        assertEquals(values, fbt.unq(), "Error finding unique values in Matrix");
    }

    //@Test
    void testSingularValueDecomposition() {
        Tuple<Matrix> USV1 = tbt1.svd();
        assertEquals(mf.m("0.0555 0.9856 -0.1599;-0.5000 -0.1112 -0.8599;-0.8643 0.1276 0.4866"),
                USV1.get(0), "Error finding the U matrix of singular value decomposition (3x3)");
        assertEquals(mf.m("14.1184 0 0;0 2.1241 0;0 0 0.4001"), USV1.get(1), "Error finding" +
                "the S matrix of singular value decomposition (3x3)");
        assertEquals(mf.m("-0.4735 0.2033 0.8570;-0.5662 0.6750 -0.4730;-0.6746 -0.7092 -0.2045"),
                USV1.get(2), "Error finding the V matrix of singular value decomposition (3x3)");
        Tuple<Matrix> USV2 = fbt.svd();
        assertEquals(mf.m("0.0091 -0.1418 -0.4075 -0.8711 -0.2346;0.0379 -0.0942 -0.4412 -0.0180 0.8915;" +
                "0.0982 -0.4788 -0.6374 0.4742 -0.3605;-0.9736 -0.2261 0.0151 0.0128 0.0252;" +
                "0.2025 -0.8311 0.4825 -0.1260 0.1399"), USV2.get(0), "Error finding the U matrix of" +
                "singular value decomposition (5x3)");
        assertEquals(mf.m("102.6711 0 0;0 22.2515 0;0 0 10.7475;0 0 0;0 0 0"), USV2.get(1),
                "Error finding the S matrix of singular value decomposition (5x3)");
        assertEquals(mf.m("-0.0290 -0.25 -0.9678 0.0391 -0.9678 0.2488;0.9988 0.0306 -0.0379"),
                USV2.get(2), "Error finding the V matrix of singular value decomposition");
    }
}