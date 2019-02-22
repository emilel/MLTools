package LinearAlgebra.ReadWrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit 5 test class for the BFNAParser class.
 */
class BFNAParserTest {
    private BFNAParser parser;

    @BeforeEach
    void setUp() {
        parser = new BFNAParser(' ', ';');
    }

    @Test
    void emptyString() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(""), "IllegalArgumentException " +
                "not thrown when trying to parse empty String");
    }

    @Test
    void parse3x3Matrix() {
        String string = "0 1 2;3 4 5;6 7 8";
        float[][] parseResult = parser.parse(string);
        float[][] ans = new float[][]{new float[]{0f, 1f, 2f}, new float[]{3f, 4f, 5f}, new float[]{6f, 7f, 8f}};

        assertArrayEquals(ans, parseResult, "3x3 matrix not parsed correctly");
    }

    @Test
    void parse1x3Matrix() {
        assertArrayEquals(new float[][]{new float[]{0f, 1f, 2f}},
                parser.parse("0 1 2"), "1x3 matrix not parsed correctly");
    }

    @Test
    void parse3x1Matrix() {
        assertArrayEquals(new float[][]{new float[]{0f}, new float[]{3f}, new float[]{6f}},
                parser.parse("0;3;6"), "3x1 not parsed correctly");
    }

    @Test
    void parseNegativeElements() {
        float[][] mat1 = new float[][]{new float[]{-1f, 1f, 10f}};
        assertArrayEquals(mat1, parser.parse("-1 1 10"), "Error when parsing negative values");
    }

    @Test
    void shouldThrowExceptionIfMismatchingRows() {
        assertThrows(RuntimeException.class, () -> parser.parse("0 1 2;0 1;10 20"));
        assertThrows(RuntimeException.class, () -> parser.parse("0 1;0 1 20;10"));
    }
}