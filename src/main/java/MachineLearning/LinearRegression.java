package MachineLearning;

import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixException;
import LinearAlgebra.MatrixFactory;

public class LinearRegression extends Regression {

    public LinearRegression(Matrix trainingSet, Matrix labels, int polynomialDegree, float learningRate,
                            float regularizationParameter, MatrixFactory mf) {
        super(trainingSet, labels, polynomialDegree, learningRate, regularizationParameter, mf);
        if (!(trainingSet.rows() == labels.rows())) {
            throw new MatrixException("Training set and labels have different number of rows (" +
                    trainingSet.rows() + " and " + labels.rows() + ")");
        } else if (labels.cols() != 1) {
            throw new MatrixException("Label set should be a vector");
        }
    }

    protected Matrix predictProcessed(Matrix processedExamples) {
        return processedExamples.mul(theta);
    }

    public float cost() {
        return 1f / (2f * m) *
                X.mul(theta).sub(y).sumsqr()
                        .add(regCost()).toFloat();
    }

    public Matrix gradient() {
        return mf.s(1f / m).mul(
                X.tran().mul(X.mul(theta).sub(y)).add(
                        mf.s(lambda).mul(mf.s(0f).concv(
                                theta.subm(1, n - 1, 0, 0)))));
    }

}
