//// src/main/java/generator/GridGenerator.java
//package generator;
//
//import Solver.MACSolver;
//import model.Grid;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//public class GridGenerator {
//    private static final Random rnd = new Random();
//
//    public static Grid generate(int size) {
//        Grid full = new Grid(size);
//
//        // On force le solveur à remplir toute la grille
//        MACSolver solver = new MACSolver();
//        boolean solved = solver.solve(full);
//
//        if (!solved || !full.isFilled()) {
//            // Si ça rate (très rare), on réessaie
//            return generate(size);
//        }
//
//        Grid puzzle = full.copy();
//        List<int[]> cells = new ArrayList<>();
//        for (int i = 0; i < size; i++)
//            for (int j = 0; j < size; j++)
//                cells.add(new int[]{i, j});
//
//        Collections.shuffle(cells, rnd);
//
//        int clues = size == 6 ? 20 : size == 8 ? 28 : 38;
//        int toRemove = size * size - clues - rnd.nextInt(5);
//
//        for (int i = 0; i < toRemove && i < cells.size(); i++) {
//            int[] cell = cells.get(i);
//            puzzle.set(cell[0], cell[1], -1);
//        }
//
//        // Marquer les cases restantes comme données
//        Grid result = new Grid(size);
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (puzzle.get(i, j) != -1) {
//                    result.setGiven(i, j, puzzle.get(i, j));
//                }
//            }
//        }
//        return result;
//    }
//}