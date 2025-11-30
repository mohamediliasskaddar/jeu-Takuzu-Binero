package utils;

import Solver.BacktrackingSolver;
import model.Grid;

public class GridUtils {

    private static final BacktrackingSolver solver = new BacktrackingSolver();

    public static boolean isValid(Grid grid) {
        int size = grid.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int v = grid.get(r, c);
                if (v != -1) {
                    if (!grid.isValidPartial()) return false;
                }
            }
        }
        return true;
    }

    public static boolean isSolvable(Grid grid) {
        Grid copy = deepCopy(grid);
        return solver.solve(copy);
    }

    public static Grid deepCopy(Grid original) {
        int size = original.getSize();
        Grid g = new Grid(size);

        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                g.set(r, c, original.get(r, c));

        return g;
    }
}
