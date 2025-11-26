package controller;

import model.Grid;
import Solver.BacktrackingSolver;

public class UserGame {

    private Grid grid;
    private BacktrackingSolver solver = new BacktrackingSolver();

    public UserGame(Grid grid) {
        this.grid = grid;
    }

    // L'utilisateur tente de remplir une case
    public String playMove(int r, int c, int value) {

        // 1. Empêcher modification des cases données
        if (grid.isGiven(r, c)) {
            return "❌ Vous ne pouvez pas modifier une case fixe.";
        }

        // 2. Jouer le coup
        grid.set(r, c, value);

        // 3. Vérifier si valide partiellement
        if (!grid.isValidPartial()) {
            return "⚠️ Coup invalide : règle Binairo violée.";
        }

        // 4. Si la grille est complète → vérifions si elle a une solution
        if (grid.isFilled()) {
            Grid copy = grid.copy();
            boolean solvable = solver.solve(copy);

            if (solvable) {
                return "🎉 Félicitations ! Votre grille est correcte !";
            } else {
                return "❌ Cette grille n'a aucune solution.";
            }
        }

        return "✔️ Coup accepté.";
    }

    // Le joueur demande une solution complète
    public String askForSolution() {
        Grid copy = grid.copy();
        boolean ok = solver.solve(copy);

        if (!ok) return "❌ Cette grille ne peut pas être résolue.";

        return "🔍 Solution trouvée :\n" + copy;
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean isSolvable() {
        Grid copy = grid.copy();
        return solver.solve(copy);
    }
    public String suggestMove() {
        Grid copy = grid.copy();

        if (!solver.solve(copy)) {
            return "❌ Impossible de trouver une solution à partir de cette grille.";
        }

        int size = grid.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid.get(r, c) == -1 && !grid.isGiven(r, c)) {
                    int correctValue = copy.get(r, c);
                    return "💡 Suggestion : mettre " + correctValue + " à la position (" + r + ", " + c + ")";
                }
            }
        }

        return "🎉 La grille est déjà complète !";
    }

}
