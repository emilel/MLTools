package LinearAlgebra.ReadWrite;

/**
 * A class able to parse arrays containing floats from a String.
 */
public class BFNAParser implements FNAParser {
    private final char colDelimiter;
    private final char rowDelimiter;

    public BFNAParser(char colDelimiter, char rowDelimiter) {
        this.colDelimiter = colDelimiter;
        this.rowDelimiter = rowDelimiter;
    }

    public float[][] parse(String values) {
        if (values.equals("")) throw new IllegalArgumentException();

        String[] rows = values.split(String.valueOf(rowDelimiter));
        String[] firstRow = rows[0].split(String.valueOf(colDelimiter));
        int cols = firstRow.length;

        float[][] res = new float[rows.length][firstRow.length];

        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split(String.valueOf(colDelimiter));
            if (row.length != cols) {
                throw new ParserException("Mismatching columns. " + cols + " columns in first row, " + row.length +
                        " columns in " + i + ":th row");
            }
            for (int j = 0; j < row.length; j++) {
                res[i][j] = Float.valueOf(row[j]);
            }
        }

        return res;
    }
}
