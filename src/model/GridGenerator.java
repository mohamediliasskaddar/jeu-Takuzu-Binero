package model;

import solver.BacktrackingSolver;
import java.util.*;

public class GridGenerator {

    /**
     * difficulty = pourcentage de cases à retirer :
     * 0.40 = débutant
     * 0.55 = intermédiaire
     * 0.70 = expert
     */
    public static Grid generate(int size, double difficulty) {

        BacktrackingSolver solver = new BacktrackingSolver();

        // 1. Générer une grille complète
        Grid full = new Grid(size);
        solver.solve(full);  // → maintenant full est 100% valide

        // 2. Préparer la liste des cases à mélanger
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                cells.add(new int[]{r, c});
        Collections.shuffle(cells);

        int toRemove = (int)(size * size * difficulty);
        int removed = 0;

        // 3. Enlever les cases tout en gardant la grille résoluble
        for (int[] pos : cells) {
            if (removed >= toRemove) break;

            int r = pos[0], c = pos[1];
            int backup = full.get(r, c);

            full.set(r, c, -1);

            // Vérifier si la grille reste résoluble
            Grid copy = full.copy();
            if (!solver.solve(copy)) {
                full.set(r, c, backup);  // restaurer si ça casse l'unicité
            } else {
                removed++;
            }
        }

        // 4. Marquer les cases données
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (full.get(r, c) != -1)
                    full.setGiven(r, c, full.get(r, c));

        return full;
    }

    // --- Modes pratiques ---
    public static Grid beginner6()    { return generate(6, 0.40); }
    public static Grid intermediate8(){ return generate(8, 0.55); }
    public static Grid expert10()     { return generate(10, 0.70); }
}
