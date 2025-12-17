package Solver;

import model.Grid;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AC3 {

    // Classe Arc statique : Représente une contrainte binaire entre deux cases du Sudoku
    static class Arc {
        int r1, c1, r2, c2;// cood
        Arc(int r1, int c1, int r2, int c2) {
            this.r1 = r1; this.c1 = c1; this.r2 = r2; this.c2 = c2;
        }
    }

    public boolean runAC3(Grid grid) {

        Queue<Arc> queue = new LinkedList<>();//File d'attente tous les arcs à traiter
        int n = grid.getSize();

        // Ajouter tous les arcs ligne & colonne
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid.get(r, c) != -1) continue; // seulement cases vides

                for (int k = 0; k < n; k++) {
                    if (k != c) queue.add(new Arc(r, c, r, k)); // ligne
                    if (k != r) queue.add(new Arc(r, c, k, c)); // colonne
                }
            }
        }

        // Boucle AC-3
        while (!queue.isEmpty()) {
            Arc arc = queue.poll();
            if (revise(grid, arc)) {
                // Domaine vide ?
                if (grid.getDomain(arc.r1, arc.c1).isEmpty()) return false;

                // Ajouter arcs voisins
                for (int k = 0; k < n; k++) {
                    if (k != arc.c1) queue.add(new Arc(arc.r1, arc.c1, arc.r1, k));
                    if (k != arc.r1) queue.add(new Arc(arc.r1, arc.c1, k, arc.c1));
                }
            }
        }

        return true;
    }

    // Revise : supprime les valeurs impossibles du domaine de chaque
    private boolean revise(Grid grid, Arc arc) {
        int r = arc.r1, c = arc.c1;
        List<Integer> domain = grid.getDomain(r, c);
        Iterator<Integer> it = domain.iterator();
        boolean revised = false;

        while (it.hasNext()) {
            int val = it.next();
            if (!grid.isValidPartialWith(r, c, val)) {// Si la valeur ne peut pas être placée* sans violer les règles du Sudoku,
                it.remove();
                revised = true;
            }
        }

        return revised;
    }
}
