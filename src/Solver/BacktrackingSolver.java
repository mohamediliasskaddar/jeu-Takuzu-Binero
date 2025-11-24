package solver;

import model.Grid;

public class BacktrackingSolver {

    // ======== Méthode principale =========
    public boolean solve(Grid grid) {
        Position pos = findEmpty(grid);
        if (pos == null) return true;   // plus de cases vides → fini

        int r = pos.r, c = pos.c;

        // Essayer 0 et 1
        for (int value : new int[]{0, 1}) {

            grid.set(r, c, value);

            // Vérification partielle
            if (grid.isValidPartial()) {

                // Continuer récursivement
                if (solve(grid)) {
                    return true;
                }
            }
        }

        // Retour arrière
        grid.set(r, c, -1);
        return false;
    }

    // ======== Trouver la première case vide =========
    private Position findEmpty(Grid grid) {
        int size = grid.getSize();
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (grid.get(r, c) == -1)
                    return new Position(r, c);

        return null;
    }

    // ======== Classe interne simple ========
    private static class Position {
        int r, c;
        Position(int r, int c) { this.r = r; this.c = c; }
    }
}
