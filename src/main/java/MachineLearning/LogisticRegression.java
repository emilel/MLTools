package MachineLearning;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;

import static Misc.Functions.log;
import static Misc.Functions.sigmoid;

/**
 * Implementation of logistic regression.
 */
public class LogisticRegression extends Regression {

    /**
     * Constructor for LogisticRegression. Saves all necessary parameters, but does not train the system until method
     * train(int iterations) is called.
     * @param trainingSet the m x n design Matrix with all the training examples. Should not contain a column of ones
     *                    for the intercept (constant) term.
     * @param labels the labels of the every training example. Should be a m x 1 vector of 1's and 0's.
     * @param polynomialDegree if the maximum degree is higher than one, the columns will be combined to create
     *                         polynomials up to the given degree.
     * @param learningRate the learning rate of the system (alpha).
     * @param regularizationParameter the regularization parameter of the system (0 for no regularization).
     * @param mf the Matrix factory to use.
     */
    public LogisticRegression(Matrix trainingSet, Matrix labels, int polynomialDegree, float learningRate,
                              float regularizationParameter, MatrixFactory mf) {
        super(trainingSet, labels, polynomialDegree, learningRate, regularizationParameter, mf);
    }

    /**
     * Predicts the output of examples that have been processed.
     * @param processedExamples the processed examples.
     * @return the predicted output.
     */
    protected Matrix predictProcessed(Matrix processedExamples) {
        return sigmoid(processedExamples.mul(theta));
    }

    /**
     * Predicts the output of examples as either a 1 or a 0.
     * @param examples the unprocessed examples.
     * @return a vector with the binary outputs.
     */
    public Matrix predictBinary(Matrix examples) {
        return predict(examples).map(d -> {
            if (d < 0.5f) return 0f;
            else return 1f;
        });
    }

    /**
     * Returns the cost of predicting the design Matrix using the current theta.
     * @return the current cost.
     */
    public float cost() {
        Matrix h = getPredictions();
        return mf.s(-1f / m).mul(y.tran().mul(log(h))
                .add(mf.s(1f).sub(y).tran().mul(log(mf.s(1).sub(h)))))
                .add(regCost())
                .toFloat();
    }

    /**
     * Returns the binary predictions of the design Matrix.
     * @return the binary predictions of the design Matrix.
     */
    protected Matrix getBinaryPredictions() {
        return getPredictions().map(d -> {
            if (d < 0.5f) return 0f;
            else return 1f;
        });
    }

    /**
     * Returns the precision of theta measured on the design Matrix.
     * @return the precision of theta measured on the design Matrix.
     */
    public float getPrecision() {
        float truePositives = getBinaryPredictions().mulew(y).sum().toFloat();
        float falsePositives = getBinaryPredictions().mulew(y.map(d -> {
            if (d == 0f) return 1f;
            else return 0f;
        })).sum().toFloat();
        return truePositives / (truePositives + falsePositives);
    }

    /**
     * Returns the recall of theta measured on the design Matrix.
     * @return the recall of theta measured on the design Matrix.
     */
    public float getRecall() {
        float truePositives = getBinaryPredictions().mulew(y).sum().toFloat();
        float falseNegatives = getBinaryPredictions().map(d -> {
            if (d == 0f) return 1f;
            else return 0f;
        }).mulew(y).sum().toFloat();
        return truePositives / (truePositives + falseNegatives);
    }

    /**
     * Returns the F1-score of theta measured on the design Matrix.
     * @return the F1-score of theta measured on the design Matrix.
     */
    public float getF1Score() {
        float prec = getPrecision();
        float rec = getRecall();
        return 2 * prec * rec / (prec + rec);
    }

    /**
     * Returns the accuracy of theta measured on the design Matrix.
     * @return the accuracy of theta measured on the design Matrix.
     */
    public float getAccuracy() {
        return getBinaryPredictions().equalsEw(y).sum().toFloat() / (float) m;
    }

    /**
     * Returns the gradient.
     * @return the gradient.
     */
    public Matrix gradient() {
        return mf.s(1f / m).mul(X.tran().mul(h().sub(y))
        .add(mf.s(lambda).mul(mf.s(0).concv(theta.rowr(1, n - 1)))));
    }

    /**
     * Returns the hypothesis of the design Matrix.
     * @return the hypothesis of the design Matrix.
     */
    private Matrix h() {
        return sigmoid(X.mul(theta));
    }
}
