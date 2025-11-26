//package Solver;
//
//import model.Grid;
//import Solver.AC3;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BacktrackingSolver {
//
//    // ======== Méthode principale =========
//    public boolean solve(Grid grid) {
//        Position pos = findEmpty(grid);
//        if (pos == null) return true;   // plus de cases vides → fini
//
//        int r = pos.r, c = pos.c;
//
//        // Essayer 0 et 1
//        for (int value : new int[]{0, 1}) {
//
//            grid.set(r, c, value);
//
//            // Vérification partielle
//            if (grid.isValidPartial()) {
//
//                // Continuer récursivement
//                if (solve(grid)) {
//                    return true;
//                }
//            }
//        }
//
//        // Retour arrière
//        grid.set(r, c, -1);
//        return false;
//    }
//
//    // ======== Trouver la première case vide =========
//    private Position findEmpty(Grid grid) {
//        int size = grid.getSize();
//        for (int r = 0; r < size; r++)
//            for (int c = 0; c < size; c++)
//                if (grid.get(r, c) == -1)
//                    return new Position(r, c);
//
//        return null;
//    }
//
//    // ======== Classe interne simple ========
//    private static class Position {
//        int r, c;
//        Position(int r, int c) { this.r = r; this.c = c; }
//    }
//
//    public boolean solveWithTimer(Grid grid) {
//        long start = System.nanoTime();  // début du timer
//
//        boolean result = solve(grid);             // ta méthode actuelle
//
//        long end = System.nanoTime();    // fin du timer
//        System.out.println("Temps de résolution : " + (end - start)/1_000_000 + " ms");
//
//        return result;
//    }
//    private List<Integer> getLCVValues(Grid grid, int r, int c) {
//        Map<Integer, Integer> scores = new HashMap<>();
//        for (int val : new int[]{0,1}) {
//            grid.set(r,c,val);
//            int score = 0;
//            int size = grid.getSize();
//
//            // Vérifier lignes et colonnes
//            for (int i=0; i<size; i++) {
//                if (grid.get(r,i)==-1 && !grid.isValidPartial()) score++;
//                if (grid.get(i,c)==-1 && !grid.isValidPartial()) score++;
//            }
//
//            scores.put(val, score);
//            grid.set(r,c,-1);
//        }
//
//        return scores.keySet().stream()
//                .sorted((a,b) -> scores.get(a) - scores.get(b))
//                .toList();
//    }
//
//    public boolean solveLCV(Grid grid) {
//        Position pos = findEmpty(grid);
//        if (pos == null) return true;
//
//        int r = pos.r, c = pos.c;
//
//        List<Integer> valuesToTry = getLCVValues(grid, r, c); // calcule LCV
//        for (int val : valuesToTry) {
//            grid.set(r, c, val);
//            if (grid.isValidPartial() && solveLCV(grid)) return true;
//            grid.set(r, c, -1);
//        }
//
//        return false;
//    }
//
//    public boolean solveOptimized(Grid grid) {
//        grid.initDomains();          // 1. initialiser les domaines
//        AC3 ac3 = new AC3();
//        if (!ac3.runAC3(grid)) return false;  // 2. propaguer les contraintes
//
//        return backtrackFC(grid);    // 3. backtracking avec FC
//    }
//
//    private boolean backtrackFC(Grid grid) {
//        Position pos = findEmpty(grid);
//        if (pos == null) return true;
//
//        int r = pos.r, c = pos.c;
//
//        for (int val : new ArrayList<>(grid.getDomain(r,c))) {
//            if (grid.forwardCheck(r, c, val)) {
//                if (backtrackFC(grid)) return true;
//            }
//            grid.set(r,c,-1);       // backtrack
//            grid.initDomains();     // réinitialiser les domaines
//        }
//        return false;
//    }
//
//
//}
package Solver;

import model.Grid;
import java.util.*;

public class BacktrackingSolver {

    // ======== Méthode principale combinée ========
    public boolean solveOptimized(Grid grid) {
        grid.initDomains();          // 1. initialiser les domaines
        AC3 ac3 = new AC3();
        if (!ac3.runAC3(grid)) return false;  // 2. propagation initiale AC-3

        return backtrack(grid);      // 3. backtracking avec FC + LCV
    }

    // ======== Backtracking avec Forward Checking + LCV ========
    private boolean backtrack(Grid grid) {
        Position pos = findEmpty(grid);
        if (pos == null) return true; // plus de cases vides → fini

        int r = pos.r, c = pos.c;

        // LCV : trier les valeurs selon le moins contraignant
        List<Integer> valuesToTry = getLCVValues(grid, r, c);

        for (int val : valuesToTry) {
            grid.set(r, c, val);

            // Forward Checking : vérifier que les voisins restent valides
            if (grid.forwardCheck(r, c, val)) {
                if (backtrack(grid)) return true;
            }

            // Retour arrière : restaurer la cellule et les domaines
            grid.set(r, c, -1);
            grid.initDomains();       // réinitialiser les domaines
            new AC3().runAC3(grid);   // propager de nouveau
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

    // ======== LCV : valeur qui enlève le moins de choix aux voisins ========
    private List<Integer> getLCVValues(Grid grid, int r, int c) {
        Map<Integer, Integer> scores = new HashMap<>();
        for (int val : new int[]{0, 1}) {
            grid.set(r, c, val);
            int score = 0;
            int size = grid.getSize();

            // compter combien de choix restent pour les voisins
            for (int i = 0; i < size; i++) {
                if (i != c && grid.get(r, i) == -1 && !grid.isValidPartial()) score++;
                if (i != r && grid.get(i, c) == -1 && !grid.isValidPartial()) score++;
            }

            scores.put(val, score);
            grid.set(r, c, -1);
        }

        return scores.keySet().stream()
                .sorted(Comparator.comparingInt(scores::get)) // LCV → valeur avec score minimal
                .toList();
    }

    // ======== Classe interne simple pour représenter une position ========
    private static class Position {
        int r, c;
        Position(int r, int c) { this.r = r; this.c = c; }
    }

    // ======== Backtracking classique ========
    public boolean solve(Grid grid) {
        Position pos = findEmpty(grid);
        if (pos == null) return true;

        int r = pos.r, c = pos.c;

        for (int val : new int[]{0,1}) {
            grid.set(r, c, val);
            if (grid.isValidPartial() && solve(grid)) return true;
            grid.set(r, c, -1); // backtrack
        }

        return false;
    }


}

