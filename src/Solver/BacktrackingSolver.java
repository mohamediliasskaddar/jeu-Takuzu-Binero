package Solver;

import model.Grid;
import java.util.*;

public class BacktrackingSolver {

    // ======== Méthode principale combinée ========
    public boolean solveOptimized(Grid grid) {
        grid.initDomains();
        AC3 ac3 = new AC3();
        if (!ac3.runAC3(grid)) return false;

        return backtrack(grid);
    }

    // ======== Backtracking avec Forward Checking + LCV ========
    private boolean backtrack(Grid grid) {
        Position pos = findEmpty(grid);
        if (pos == null) return true;

        int r = pos.r, c = pos.c;

        List<Integer> valuesToTry = getLCVValues(grid, r, c);

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

    // ======== Trouver la première case vide ========
    private Position findEmpty(Grid grid) {
        int size = grid.getSize();
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (grid.get(r, c) == -1)
                    return new Position(r, c);
        return null;
    }

    // ======== LCV : valeur la moins contraignante ========
    private List<Integer> getLCVValues(Grid grid, int r, int c) {
        Map<Integer, Integer> scores = new HashMap<>();

        for (int val : new int[]{0, 1}) {
            grid.set(r, c, val);
            int score = 0;
            int size = grid.getSize();

            for (int i = 0; i < size; i++) {
                if (i != c && grid.get(r, i) == -1 && !grid.isValidPartial()) score++;
                if (i != r && grid.get(i, c) == -1 && !grid.isValidPartial()) score++;
            }

            scores.put(val, score);
            grid.set(r, c, -1);
        }

        return scores.keySet().stream()
                .sorted(Comparator.comparingInt(scores::get))
                .toList();
    }

    // ======== Backtracking classique ========
    public boolean solve(Grid grid) {
        Position pos = findEmpty(grid);
        if (pos == null) return true;

        int r = pos.r, c = pos.c;

        for (int val : new int[]{0, 1}) {
            grid.set(r, c, val);
            if (grid.isValidPartial() && solve(grid)) return true;
            grid.set(r, c, -1);
        }

        return false;
    }

    // ======== Classe interne ========
    private static class Position {
        int r, c;
        Position(int r, int c) { this.r = r; this.c = c; }
    }
}
