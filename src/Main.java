//import controller.UserGame;
//import model.Grid;
//import model.GridGenerator;
//
//public class Main {
//    public static void main(String[] args) {
//
//        System.out.printf("Hello and welcome! \n");
//
////        Grid g2 = GridGenerator.intermediate8();
////        System.out.println(g2 + "\n");
//
////        Grid g = GridGenerator.generate(4,0.9);
////        System.out.println(g + "\n");
//
//
////        Grid g1 = GridGenerator.beginner6();
////        System.out.println(g1);
//
////        solver.BacktrackingSolver solver = new solver.BacktrackingSolver();
////        if (solver.solve(g)) {
////            System.out.println("Solution trouvée:\n" + g);
////        }
////        Grid g = new Grid(6);
////        UserGame game = new UserGame(g);
////
////        if (game.isSolvable()) {
////            System.out.println("✅ Cette grille a au moins une solution.");
////        } else {
////            System.out.println("❌ Cette grille n'est pas résoluble !");
////        }
////
////
////        System.out.println(game.playMove(0, 0, 1));
////        System.out.println(game.playMove(0, 1, 1));   // peut être invalide
////        System.out.println(game.playMove(1, 0, 0));
////
////        System.out.println(game.askForSolution());
//
//
//
//    }
//}

import model.Grid;
import model.GridGenerator;
import controller.UserGame;
import solver.BacktrackingSolver;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BacktrackingSolver solver = new BacktrackingSolver();

        System.out.println("===== TEST GLOBAL DU JEU BINAIRE =====\n");

        // --- 1. Génération des grilles ---
        System.out.println("1) Génération grille débutant 6x6 :");
        Grid g6 = GridGenerator.beginner6();
        System.out.println(g6);

        System.out.println("2) Génération grille intermédiaire 8x8 :");
        Grid g8 = GridGenerator.intermediate8();
        System.out.println(g8);

        System.out.println("3) Génération grille expert 10x10 :");
        Grid g10 = GridGenerator.expert10();
        System.out.println(g10);

        // --- 2. Vérification résolubilité ---
        UserGame game6 = new UserGame(g6);
        System.out.println("✅ Grille débutant résoluble ? " + game6.isSolvable());

        UserGame game8 = new UserGame(g8);
        System.out.println("✅ Grille intermédiaire résoluble ? " + game8.isSolvable());

        UserGame game10 = new UserGame(g10);
        System.out.println("✅ Grille expert résoluble ? " + game10.isSolvable());

        // --- 3. Jouer un coup manuel ---
        System.out.println("\n--- TEST MODE UTILISATEUR ---");
        Grid testGrid = GridGenerator.beginner6();
        UserGame userGame = new UserGame(testGrid);

        System.out.println(testGrid);

        while (true) {
            System.out.println("\n1: Jouer un coup  | 2: Demander suggestion | 3: Résoudre | 0: Quitter");
            int choice = sc.nextInt();
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("Ligne (0-" + (testGrid.getSize()-1) + "): ");
                    int r = sc.nextInt();
                    System.out.print("Colonne (0-" + (testGrid.getSize()-1) + "): ");
                    int c = sc.nextInt();
                    System.out.print("Valeur (0 ou 1): ");
                    int val = sc.nextInt();
                    System.out.println(userGame.playMove(r, c, val));
                    System.out.println(userGame.getGrid());
                    break;

                case 2:
                    System.out.println(userGame.suggestMove());
                    break;

                case 3:
                    Grid copy = userGame.getGrid().copy();
                    if (solver.solve(copy)) {
                        System.out.println("🔹 Solution complète :\n" + copy);
                    } else {
                        System.out.println("❌ Impossible de résoudre cette grille !");
                    }
                    break;

                default:
                    System.out.println("Choix invalide");
            }
        }

        System.out.println("\n===== FIN DES TESTS =====");
        sc.close();
    }
}
