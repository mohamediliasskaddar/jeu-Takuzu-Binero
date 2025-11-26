//
//
//import model.Grid;
//import model.GridGenerator;
//import controller.UserGame;
//import Solver.BacktrackingSolver;
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
////        Scanner sc = new Scanner(System.in);
////        BacktrackingSolver solver = new BacktrackingSolver();
////
////        System.out.println("===== TEST GLOBAL DU JEU BINAIRE =====\n");
////
////        // --- 1. Génération des grilles ---
////        System.out.println("1) Génération grille débutant 6x6 :");
////        Grid g6 = GridGenerator.beginner6();
////        System.out.println(g6);
////
////        System.out.println("2) Génération grille intermédiaire 8x8 :");
////        Grid g8 = GridGenerator.intermediate8();
////        System.out.println(g8);
////
////        System.out.println("3) Génération grille expert 10x10 :");
////        Grid g10 = GridGenerator.expert10();
////        System.out.println(g10);
////
////        // --- 2. Vérification résolubilité ---
////        UserGame game6 = new UserGame(g6);
////        System.out.println("✅ Grille débutant résoluble ? " + game6.isSolvable());
////
////        UserGame game8 = new UserGame(g8);
////        System.out.println("✅ Grille intermédiaire résoluble ? " + game8.isSolvable());
////
////        UserGame game10 = new UserGame(g10);
////        System.out.println("✅ Grille expert résoluble ? " + game10.isSolvable());
////
////        // --- 3. Jouer un coup manuel ---
////        System.out.println("\n--- TEST MODE UTILISATEUR ---");
////        Grid testGrid = GridGenerator.beginner6();
////        UserGame userGame = new UserGame(testGrid);
////
////        System.out.println(testGrid);
////
////        while (true) {
////            System.out.println("\n1: Jouer un coup  | 2: Demander suggestion | 3: Résoudre | 0: Quitter");
////            int choice = sc.nextInt();
////            if (choice == 0) break;
////
////            switch (choice) {
////                case 1:
////                    System.out.print("Ligne (0-" + (testGrid.getSize()-1) + "): ");
////                    int r = sc.nextInt();
////                    System.out.print("Colonne (0-" + (testGrid.getSize()-1) + "): ");
////                    int c = sc.nextInt();
////                    System.out.print("Valeur (0 ou 1): ");
////                    int val = sc.nextInt();
////                    System.out.println(userGame.playMove(r, c, val));
////                    System.out.println(userGame.getGrid());
////                    break;
////
////                case 2:
////                    System.out.println(userGame.suggestMove());
////                    break;
////
////                case 3:
////                    Grid copy = userGame.getGrid().copy();
////                    if (solver.solve(copy)) {
////                        System.out.println("🔹 Solution complète :\n" + copy);
////                    } else {
////                        System.out.println("❌ Impossible de résoudre cette grille !");
////                    }
////                    break;
////
////                default:
////                    System.out.println("Choix invalide");
////            }
////        }
////
////        System.out.println("\n===== FIN DES TESTS =====");
////        sc.close();
////    }
//
////
//        Grid[] grids = {
//                new Grid(6),
//                new Grid(8),
//                new Grid(10),
//                new Grid(11),
//                new Grid(12)
//        };
//
//        BacktrackingSolver solver = new BacktrackingSolver();
//
//        System.out.println("Solutions avec mesure de temps :\n");
//
//        for (Grid g : grids) {
//
//            // === TEST LCV ===
//            Grid gLCV = new Grid(g); // clone / copie de la grille de base
//
//            long startLCV = System.currentTimeMillis();
//            boolean okLCV = solver.solveLCV(gLCV);
//            long endLCV = System.currentTimeMillis();
//
//            System.out.println("LCV " + g.getSize() + "x" + g.getSize() +
//                    " -> " + (okLCV ? "Résolue" : "Impossible") +
//                    " | Temps : " + (endLCV - startLCV) + " ms");
//
//
//            // === TEST NORMAL ===
//            Grid gNormal = new Grid(g);  // nouvelle copie propre
//
//            long startNormal = System.currentTimeMillis();
//            boolean okNormal = solver.solve(gNormal);
//            long endNormal = System.currentTimeMillis();
//
//            System.out.println("Normal " + g.getSize() + "x" + g.getSize() +
//                    " -> " + (okNormal ? "Résolue" : "Impossible") +
//                    " | Temps : " + (endNormal - startNormal) + " ms");
//
//            System.out.println();
//        }
//    }
//}
////
////Solutions avec mesure de temps :
////
////Grille 6x6 -> Résolue | Temps : 4 ms
////Grille 8x8 -> Résolue | Temps : 80 ms
////Grille 10x10 -> Résolue | Temps : 16468 ms
////Grille 11x11 -> Résolue | Temps : 46337 ms

import model.Grid;
import Solver.BacktrackingSolver;

public class Main {

    public static void main(String[] args) {

        // Exemple de grilles (vide ou partiellement remplies)
        Grid[] grids = {
//                new Grid(6),
//                new Grid(8),
                new Grid(11)
        };

        BacktrackingSolver solver = new BacktrackingSolver();

        System.out.println("===== Backtracking classique =====");
        for (Grid g : grids) {
            long start = System.currentTimeMillis();
            boolean ok = solver.solve(g.copy());  // utiliser copy pour ne pas modifier la grille originale
            long end = System.currentTimeMillis();

            System.out.println("Grille " + g.getSize() + "x" + g.getSize() +
                    " -> " + (ok ? "Résolue" : "Impossible") +
                    " | Temps : " + (end - start) + " ms");
        }

        System.out.println("\n===== Solveur optimisé (AC-3 + FC + LCV) =====");
        for (Grid g : grids) {
            long start = System.currentTimeMillis();
            Grid gCopy = g.copy();
            boolean ok = solver.solveOptimized(gCopy);
            if (ok) {
                System.out.println("Solution :\n" + gCopy);
            }

            long end = System.currentTimeMillis();

            System.out.println("Grille " + g.getSize() + "x" + g.getSize() +
                    " -> " + (ok ? "Résolue" : "Impossible") +
                    " | Temps : " + (end - start) + " ms");

            if (ok) {
                System.out.println("Solution :\n" + g);
            }
        }
    }
}
