package LinearAlgebra.ReadWrite;

import LinearAlgebra.Matrix;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BMatrixPrinter<E> implements MatrixPrinter<E> {
    private final char colDelimiter;
    private final char rowDelimiter;

    public BMatrixPrinter(char colDelimiter, char rowDelimiter) {
        this.colDelimiter = colDelimiter;
        this.rowDelimiter = rowDelimiter;
    }

    public void print(Matrix matrix, String filename) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filename),
                        StandardCharsets.UTF_8))) {
            writer.write(matrix.toString(colDelimiter, rowDelimiter));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
