package LinearAlgebra.ReadWrite;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BMatrixPrinterTest {
    private static final MatrixPrinter matrixPrinter = new BMatrixPrinter(' ', '\n');
    private static final MatrixFactory mf = new BMatrixFactory(' ', ';');
    private static Matrix matrix;

    @BeforeAll
    static void setUp() {
        matrix = mf.m("1 2 3;4 5 6;7 8 9");
    }

    @AfterAll
    static void tearDown() {
        File file = new File("matrix.txt");
        file.delete();
    }

    @Test
    void testPrint() {
        matrixPrinter.print(matrix, "matrix.txt");
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader in = new BufferedReader(new FileReader("matrix.txt"))) {
            line = in.readLine();
            while (line != null) {
                sb.append(line);
                line = in.readLine();
                if (line != null) sb.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(matrix.toString(), sb.toString(), "Printed String is not equal to read String");
    }
}