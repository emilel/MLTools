package LinearAlgebra.ReadWrite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BFNAReader implements FNAReader {
    private char colDelimiter;

    public BFNAReader(char colDelimiter) {
        this.colDelimiter = colDelimiter;
    }

    public float[][] r(String filename, int rows, int cols) {
        float[][] res = new float[rows][cols];

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = in.readLine()) != null) {
                String[] floats = line.split(String.valueOf(colDelimiter));
                for (int j = 0; j < cols; j++) {
                    res[i][j] = Float.valueOf(floats[j]);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public float[][] r(String filename) {
        List<List<Float>> list = new LinkedList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] stringRow = line.split(String.valueOf(colDelimiter));
                List<Float> row = new LinkedList<>();
                for (String stringFloat : stringRow) {
                    row.add(Float.valueOf(stringFloat));
                }
                list.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        float[][] res = new float[list.size()][list.get(0).size()];

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).size(); j++) {
                res[i][j] = list.get(i).get(j);
            }
        }

        return res;
    }
}
