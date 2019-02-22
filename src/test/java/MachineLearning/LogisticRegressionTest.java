package MachineLearning;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import LinearAlgebra.ReadWrite.BFNAReader;
import LinearAlgebra.ReadWrite.FNAReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for logistic regression. Uses data from Coursera's Machine Learning course by Andrew Ng.
 */
class LogisticRegressionTest {
    private MatrixFactory mf = new BMatrixFactory();
    private FNAReader rdr = new BFNAReader(',');
    private LogisticRegression logReg;
    private Matrix X;
    private Matrix y;

    @BeforeEach
    void setUp() {
        Matrix D = mf.m(rdr.r("src/test/resources/LogisticRegression/ex2data2.txt", 118, 3));
        X = D.colr(0, 1);
        y = D.col(2);
        logReg = new LogisticRegression(X, y, 6, 1f, 1f, mf);
    }

    @Test
    void testCostBeforeTraining() {
        assertEquals(0.6931472f, logReg.cost(), "Error getting cost before training");
    }

    @Test
    void testGradientBeforeTraining() {
        assertEquals(mf.m("0.008475;0.018788;0.000078;0.050345;0.011501"), logReg.gradient().rowr(0, 4),
                "Error getting gradient before training");
    }

    @Test
    void testAccuracy() {
        logReg.train(100);
        assertEquals(0.8305084705352783, logReg.getAccuracy(), "Accuracy is wrong after training");
    }

    @Test
    void testEvaluationMetrics() {
        logReg.train(100);
        assertEquals(0.7714285850524902, logReg.getPrecision(), "Error getting precision after training");
        assertEquals(0.931034505367279, logReg.getRecall(), "Error getting recall after training");
        assertEquals(0.8437499403953552, logReg.getF1Score(), "Error getting F1-score after training");
    }

    @Test
    void testPredictNewExamples() {
        setUp();
        logReg.train(100);
        assertEquals(logReg.getBinaryPredictions(), logReg.predictBinary(X), "Error predicting design matrix");
    }
}