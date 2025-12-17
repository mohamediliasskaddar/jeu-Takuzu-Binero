package Solver;

import model.Grid;
import java.util.List;

public class BacktrackingSolver {

    // ======== Backtracking classique ========
    public boolean solve(Grid grid) {
        Position pos = SolverUtils.findEmpty(grid);
        if (pos == null) return true;

        int r = pos.row, c = pos.col;

        for (int val : new int[]{0, 1}) {
            grid.set(r, c, val);
            if (grid.isValidPartial() && solve(grid)) return true;
            grid.set(r, c, -1); // backtrack
        }

        return false;
    }

    // ======== Solveur optimisé (AC3 + FC + LCV) ========
    public boolean solveOptimized(Grid grid) {
        grid.initDomains();
        AC3 ac3 = new AC3();
        if (!ac3.runAC3(grid)) return false;

        return backtrack(grid);
    }

    // Backtracking avec Forward Checking + LCV
    private boolean backtrack(Grid grid) {
        Position pos = SolverUtils.findEmpty(grid);
        if (pos == null) return true;

        int r = pos.row, c = pos.col;

        List<Integer> valuesToTry = LCVHeuristic.getLCVValues(grid, r, c);// value to test

        for (int val : valuesToTry) {
            grid.set(r, c, val);

            if (grid.forwardCheck(r, c, val)) {
                if (backtrack(grid)) return true;
            }

            grid.set(r, c, -1);
            grid.initDomains();
            new AC3().runAC3(grid);
        }

        return false;
    }
}
