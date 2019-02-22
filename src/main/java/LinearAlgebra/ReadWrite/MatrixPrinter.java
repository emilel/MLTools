package LinearAlgebra.ReadWrite;

import LinearAlgebra.Matrix;

interface MatrixPrinter<E> {
    void print(Matrix matrix, String filename);
}
