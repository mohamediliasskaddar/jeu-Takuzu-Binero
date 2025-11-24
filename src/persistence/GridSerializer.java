package persistence;

import model.Grid;

import java.io.*;
import java.util.StringTokenizer;

public class GridSerializer {
    public static void save(Grid grid, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(grid.getSize());
            for (int i = 0; i < grid.getSize(); i++) {
                for (int j = 0; j < grid.getSize(); j++) {
                    int v = grid.get(i, j);
                    writer.print((v == -1 ? "." : v) + (grid.isGiven(i, j) ? "!" : ""));
                    if (j < grid.getSize() - 1) writer.print(" ");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Grid load(File file, int expectedSize) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int size = Integer.parseInt(reader.readLine().trim());
            if (size != expectedSize) return null;

            Grid grid = new Grid(size);
            for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                StringTokenizer tok = new StringTokenizer(line);
                for (int j = 0; j < size; j++) {
                    String token = tok.nextToken();
                    int val = token.equals(".") ? -1 : Integer.parseInt(token.substring(0, 1));
                    boolean given = token.endsWith("!");
                    if (given) {
                        grid.setGiven(i, j, val);
                    } else {
                        grid.set(i, j, val);
                    }
                }
            }
            return grid;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}