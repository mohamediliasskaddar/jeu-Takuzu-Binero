package Solver;

import model.Grid;

public class SolverUtils {

    // Trouver la première case vide (-1)
    public static Position findEmpty(Grid grid) {
        int size = grid.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid.get(r, c) == -1) return new Position(r, c);
            }
        }
        return null;
    }

}
