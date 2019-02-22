package MachineLearning;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixException;
import LinearAlgebra.MatrixFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static Misc.MapFeatures.allPolynomials;

/**
 * Superclass for different types of Regression.
 */
public abstract class Regression implements Algorithm {
    Matrix X;
    Matrix y;
    Matrix theta;
    private float alpha;
    private int origCols;
    float lambda;
    private int polynomialDegree;
    MatrixFactory mf;
    int m;
    int n;
    private List<Float> costHistory;
    private boolean hasChanged;
    private Matrix predictions;

    /**
     * Creates an instance of Regression.
     * @param trainingSet the m x n design Matrix.
     * @param labels the m x 1 vector containing the label for every training example, in the same order as in the
     *               design Matrix.
     * @param polynomialDegree if the maximum degree is higher than one, the columns will be combined to create
     *                         polynomials up to the given degree.
     * @param learningRate the learning rate of the system (alpha).
     * @param regularizationParameter the regularization parameter of the system (0 for no regularization).
     * @param mf the Matrix factory to use.
     */
    Regression(Matrix trainingSet, Matrix labels, int polynomialDegree, float learningRate,
                      float regularizationParameter,
                      MatrixFactory mf) {
        this.origCols = trainingSet.cols();
        if (polynomialDegree == 1) {
            this.X = trainingSet.addc1();
        } else {
            this.X = allPolynomials(trainingSet, polynomialDegree, mf);
        }
        this.y = labels;
        this.costHistory = new LinkedList<>();
        this.theta = mf.zeroes(X.cols(), 1);
        this.alpha = learningRate;
        this.lambda = regularizationParameter;
        this.polynomialDegree = polynomialDegree;
        this.mf = mf;
        this.m = X.rows();
        this.n = X.cols();
        this.hasChanged = false;
    }

    /**
     * Trains the system using gradient descent.
     * @param iterations the number of iterations to train.
     */
    public void train(int iterations) {
        costHistory = new LinkedList<>();
        for (int i = 0; i < iterations; i++) {
            theta = theta.sub(mf.s(alpha).mul(gradient()));
            costHistory.add(cost());
        }
        hasChanged = true;
    }

    /**
     * Predicts the output of new examples.
     * @param examples the Matrix containing the examples. Every example should be one row, unprocessed and in the same
     *                 form as the training examples.
     * @return
     */
    public Matrix predict(Matrix examples) {
        if (examples.cols() != origCols) throw new MatrixException("Columns in argument is not equal to columns " +
                "trained with");
        return predictProcessed(process(examples));
    }

    /**
     * Processes new examples in the same way that the original training set was processed.
     * @param examples the new examples to process.
     * @return the processed examples.
     */
    private Matrix process(Matrix examples) {
        if (polynomialDegree == 1) return examples.addc1();
        else return allPolynomials(examples, polynomialDegree, mf);
    }

    /**
     * Returns the (processed) training set.
     * @return the training set.
     */
    public Matrix getTrainingSet() {
        return X;
    }

    /**
     * Returns the learned parameters (or just zeroes if not trained yet).
     * @return the learned parameters
     */
    public Matrix getTheta() {
        return theta;
    }

    /**
     * Calculates the cost of the regularization.
     * @return the cost of the regularization.
     */
    Matrix regCost() {
        return mf.s(lambda).mul(theta.subm(1, n - 1, 0, 0)).sumsqr();
    }

    /**
     * Returns how the cost changed during training.
     * @return a list of all costs during training.
     */
    public List<Float> getCostHistory() {
        return costHistory;
    }

    public Matrix getPredictions() {
        if (hasChanged || predictions == null) {
            predictions = predictProcessed(X);
            hasChanged = false;
            return predictions;
        }
        else return predictions;
    }

    /**
     * Returns the labels.
     * @return the labels.
     */
    public Matrix getLabels() {
        return y;
    }

    /**
     * Visualizes the cost by printing out dots representing the cost at every iteration.
     * @param width the maximum amount of dots on a single row.
     */
    public void visualizeCost(int width) {
        float max = Collections.max(costHistory);
        float factor = max / width;
        int[] costs = costHistory.stream().mapToInt(d -> (int) (d / factor)).toArray();

        StringBuilder sb = new StringBuilder();
        for (float cost : costs) {
            for (int i = 0; i < cost; i++) {
                sb.append('.');
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

    /**
     * Predicts the output of an example, after it has been processed.
     * @param processedExamples the processed examples.
     * @return the predicted output.
     */
    protected abstract Matrix predictProcessed(Matrix processedExamples);

    /**
     * Returns the current cost, based on the training set.
     * @return the current cost.
     */
    public abstract float cost();

    /**
     * Returns the gradient.
     * @return the gradient.
     */
    public abstract Matrix gradient();
}
