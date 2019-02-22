package LinearAlgebra.ReadWrite;

import LinearAlgebra.BLinAlg.BMatrixFactory;
import LinearAlgebra.Matrix;
import LinearAlgebra.MatrixFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BFNAReaderTest {
    private static final MatrixFactory mf = new BMatrixFactory();
    private static final FNAReader reader = new BFNAReader(' ');
    private static Matrix matrix;

    @BeforeAll
    static void setUp() {
        matrix = mf.m("0 1 2 3 4;6 7 8 9 10;11 12 13 14 15");
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("matrix.txt"),
                        StandardCharsets.UTF_8))) {
            writer.write(matrix.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Test
    void testReadKnownSize() {
        assertEquals(matrix, mf.m(reader.r("matrix.txt", 3, 5)),
                "Error reading matrix from file");
    }

    @Test
    void testReadUnknownSize() {
        assertEquals(matrix, mf.m(reader.r("matrix.txt")),
                "Error reading matrix from file");
    }

    @AfterAll
    static void tearDown() {
        File file = new File("matrix.txt");
        file.delete();
    }
}