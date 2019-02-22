package MachineLearning;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import LinearAlgebra.ReadWrite.BFNAReader;
import LinearAlgebra.ReadWrite.FNAReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OneVsAllTest {
    private OneVsAll ova;
    private MatrixFactory mf = new BMatrixFactory();
    private FNAReader rdr = new BFNAReader(' ');
    private Matrix X;
    private Matrix y;

    @BeforeEach
    void setUp() {
        this.X = mf.m(rdr.r("src/test/resources/MultiClass/ex3dataX.txt", 5000, 400));
        this.y = mf.m(rdr.r("src/test/resources/MultiClass/ex3datay.txt", 5000, 1));
        this.ova = new OneVsAll(X, y, 1,3f, 0.1f, mf);
    }

    @Test
    void testEvaluationMetrics() {
        System.out.println("Training OneVsAll classifier on 5000 examples, this may take approximately 15 seconds...");
        ova.train(100);
        float acc = ova.getAccuracy();
        System.out.println(acc);
        assertTrue(acc > 0.9, "Accuracy is too low after training");
    }
}