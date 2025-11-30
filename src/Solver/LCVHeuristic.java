package Solver;

import model.Grid;
import java.util.*;

public class LCVHeuristic {

    // Retourne la liste des valeurs triées selon LCV (moins contraignant en premier)
    public static List<Integer> getLCVValues(Grid grid, int r, int c) {
        Map<Integer, Integer> scores = new HashMap<>();
        int[] domain = {0, 1};

        for (int val : domain) {
            grid.set(r, c, val);
            int score = 0;
            int size = grid.getSize();

            // compter combien de cases voisines deviennent moins flexibles
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
}
