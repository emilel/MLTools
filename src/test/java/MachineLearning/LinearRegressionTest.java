package MachineLearning;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinearRegressionTest {
    private static final MatrixFactory mf = new BMatrixFactory();
    private Matrix X;
    private Matrix y;
    private LinearRegression linReg;

    void setUpCase1() {
        X = mf.m("0 4 1;1 3 4;10 1 2;1 0 3;8 10 0;1 40 1");
        y = mf.m("42;38.5;19;6.5;104;402.5");
        linReg = new LinearRegression(X, y, 1, 0.003f, 0f, mf);
    }

    void setUpCase2() {
        X = mf.m("16 -38 22 -1 17 15;-15 -5 24 -4 21 13;36 -10 -25 12 -18 -27");
        y = mf.m("-141.8;7.2;-65.6");
        linReg = new LinearRegression(X, y, 1,0.001f, 1f, mf);
    }

    @Test
    void testTrain() {
        setUpCase1();
        linReg.train(100);
        List<Float> costHistory = linReg.getCostHistory();
        Iterator<Float> it = costHistory.iterator();
        float prev = it.next();
        while (it.hasNext()) {
            float current = it.next();
            if (current > prev) {
                fail("Cost increases during one iteration");
            }
            prev = current;
        }
    }

    @Test
    void testPredictions() {
        setUpCase1();
        linReg.train(100);
        assertTrue(y.equals(linReg.getPredictions(), 1.7f), "Predictions are not close enough to " +
                "real labels after 100 iterations");
    }

    @Test
    void testPredict() {
        setUpCase1();
        linReg.train(100);
        Matrix X2 = mf.m("22 -4 -18;24 12 15;-25 17 13;-1 21 -27");
        Matrix X2Ans = mf.m("-64;163;184.5;156.5");

        assertTrue(X2Ans.equals(linReg.predict(X2), 15f), "Error predicting multiple examples at once");
    }

    @Test
    void testGetTheta() {
        setUpCase1();
        linReg.train(100);
        assertEquals(mf.m("0.5864;0.5378;9.9968;1.4274"), linReg.getTheta(), "Theta is wrong after" +
                " training");
    }

    @Test
    void testRegularization() {
        setUpCase2();
        assertEquals(4077.0732421875, linReg.cost(), "Wrong cost before training when using regularization");
        assertTrue(mf.m("66.7;1579.5;-2002.08;435.6;224.7;359.5;87.4").equals(linReg.gradient(), 1f),
                "Wrong gradient before training using regularization");
        linReg.train(1);
        assertEquals(122.451904296875, linReg.cost(),
                "Wrong cost after using regularization");
        assertEquals(mf.m("5.8041;252.3826;-297.1139;30.8428;30.5175;16.6368;15.5527"), linReg.gradient(),
                "Wrong gradient after using regularization");
    }
}