package MachineLearning;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of OneVsAll-classification.
 */
public class OneVsAll implements Algorithm {
    private Map<Float, LogisticRegression> logRegs;
    private MatrixFactory mf;
    private Matrix y;
    private Matrix X;

    /**
     * Creates an instance of OneVsAll, and creates all the necessary instances of LogisticRegression needed.
     * @param trainingSet the m x n design Matrix.
     * @param labels the m x 1 label vector.
     * @param polynomialDegree the highest degree of polynomials that will be generated.
     * @param learningRate the learning rate of the system (alpha).
     * @param regularizationParameter the regularization parameter of the system, lambda (0 for no regularization).
     * @param mf the MatrixFactory to use.
     */
    public OneVsAll(Matrix trainingSet, Matrix labels, int polynomialDegree, float learningRate,
                    float regularizationParameter, MatrixFactory mf) {
        this.logRegs = new HashMap<>();
        this.mf = mf;
        this.X = trainingSet;
        this.y = labels;
        Set<Float> uniqueLabels = labels.unq();
        int m = labels.rows();
        for (Float f : uniqueLabels) {
            Matrix correctLabels = labels.equalsEw(mf.ones(m, 1).mul(f));
            logRegs.put(f, new LogisticRegression(trainingSet, correctLabels, polynomialDegree, learningRate,
                    regularizationParameter, mf));
        }
    }

    /**
     * Trains the OneVsAll-classifier.
     * @param iterations the number of iterations for every label.
     */
    public void train(int iterations) {
        for (Map.Entry<Float, LogisticRegression> logRegEntry : logRegs.entrySet()) {
            logRegEntry.getValue().train(iterations);
        }
    }

    /**
     * Predicts the output of new examples.
     * @param examples the p x q example Matrix to predict the outputs of.
     * @return a p x 1 vector containing the labels the system predicts from every example.
     */
    public Matrix predict(Matrix examples) {
        Matrix res = mf.ones(1, 1);
        for (int i = 0; i < examples.rows(); i++) {
            Matrix example = examples.row(i);
            float highestProbability = 0f;
            float bestLabel = 0f;
            for (Map.Entry<Float, LogisticRegression> logRegEntry : logRegs.entrySet()) {
                float pred = logRegEntry.getValue().predict(example).toFloat();
                if (pred > highestProbability) {
                    highestProbability = pred;
                    bestLabel = logRegEntry.getKey();
                }
            }
            res = res.concv(mf.s(bestLabel));
        }
        return res.rowr(1, res.rows() - 1);
    }

    /**
     * Predicts every example in the design Matrix, and calculates the accuracy (correct outputs / number of examples).
     * @return the accuracy.
     */
    public float getAccuracy() {
        Matrix predictions = predict(X);
        return predictions.equalsEw(y).sum().toFloat() / (float) X.rows();
    }
}
