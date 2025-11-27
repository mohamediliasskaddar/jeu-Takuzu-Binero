package model;

import java.util.*;

public class Grid {
    private final int size;
    private final int[][] values;        // -1 = vide, 0 ou 1 = fixé ou rempli
    private final boolean[][] given;     // true si case donnée au départ
    private List<Integer>[][] domains; // pour chaque case, domaine possible (0 ou 1)


    public Grid(int size) {
        this.size = size;
        this.values = new int[size][size];
        this.given = new boolean[size][size];
        for (int[] row : values) Arrays.fill(row, -1);
    }

    public Grid(Grid other) {
        this.size = other.size;
        this.values = new int[size][size];
        this.given = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(other.values[i], 0, this.values[i], 0, size);
            System.arraycopy(other.given[i], 0, this.given[i], 0, size);
        }
    }



    public int getSize() { return size; }
    public int get(int r, int c) { return values[r][c]; }
    public boolean isGiven(int r, int c) { return given[r][c]; }

    public void set(int r, int c, int val) {
        if (!given[r][c]) values[r][c] = val;
    }

    public void setGiven(int r, int c, int val) {
        values[r][c] = val;
        given[r][c] = true;
    }

    public boolean isFilled() {
        for (int[] row : values)
            for (int v : row)
                if (v == -1) return false;
        return true;
    }

    // ============ VÉRIFICATIONS DES RÈGLES ============
    public boolean isValidPartial() {
        return noThreeInARow() && balanceOK() && uniquenessOK();
    }

    private boolean noThreeInARow() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (values[i][j] == -1) continue;
                // ligne : je dois lancer une erreur claire : comme meme nombre est juxtaposé plus  que 2 fosi
                if (j >= 2 && values[i][j] == values[i][j-1] && values[i][j] == values[i][j-2]) return false;
                // colonne : meme remaue ici
                if (i >= 2 && values[i][j] == values[i-1][j] && values[i][j] == values[i-2][j]) return false;
            }
        }
        return true;
    }

    private boolean balanceOK() {
        // pour les grilles paires
        for (int i = 0; i < size; i++) {
            int count0 = 0, count1 = 0;
            for (int j = 0; j < size; j++) {
                if (values[i][j] == 0) count0++;
                if (values[i][j] == 1) count1++;
            }
            if (count0 + count1 == size) {  // ligne complète
                if (size % 2 == 0 && count0 != count1) return false; // pair
                if (size % 2 == 1 && Math.abs(count0 - count1) != 1) return false; // imppair
            }

            // même chose pour colonnes
            count0 = count1 = 0;
            for (int j = 0; j < size; j++) {
                if (values[j][i] == 0) count0++;
                if (values[j][i] == 1) count1++;
            }
            if (count0 + count1 == size) {
                if (size % 2 == 0 && count0 != count1) return false;
                if (size % 2 == 1 && Math.abs(count0 - count1) != 1) return false;
            }
        }

        return true;
    }

    private boolean uniquenessOK() {
        Set<String> rows = new HashSet<>();
        Set<String> cols = new HashSet<>();

        for (int i = 0; i < size; i++) {
            StringBuilder row = new StringBuilder();
            StringBuilder col = new StringBuilder();
            boolean rowFull = true, colFull = true;
            for (int j = 0; j < size; j++) {
                int v = values[i][j]; row.append(v == -1 ? "?" : v);
                if (v == -1) rowFull = false;
                int w = values[j][i]; col.append(w == -1 ? "?" : w);
                if (w == -1) colFull = false;
            }
            if (rowFull && !rows.add(row.toString())) return false;
            if (colFull && !cols.add(col.toString())) return false;
        }
        return true;
    }

    public Grid copy() {
        Grid g = new Grid(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                g.values[i][j] = values[i][j];
                g.given[i][j] = given[i][j];
            }
        return g;
    }

// 2. Initialiser les domaines
    @SuppressWarnings("unchecked")
    public void initDomains() {
        domains = new List[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (values[r][c] == -1) {
                    domains[r][c] = new ArrayList<>(Arrays.asList(0,1));
                } else {
                    domains[r][c] = new ArrayList<>();
                    domains[r][c].add(values[r][c]);
                }
            }
        }
    }

// 3. Accéder au domaine d'une case
    public List<Integer> getDomain(int r, int c) {
        return domains[r][c];
    }

// 4. Forward Checking

    public boolean forwardCheck(int r, int c, int val) {
        values[r][c] = val;

        for (int i = 0; i < size; i++) {
            // pour la ligne
            if (i != c && values[r][i] == -1) {
                final int row = r;
                final int col = i;
                domains[row][col].removeIf(v -> !isValidPartialWith(row, col, v));
                if (domains[row][col].isEmpty()) return false;
            }

            // pour la colonne
            if (i != r && values[i][c] == -1) {
                final int row = i;
                final int col = c;
                domains[row][col].removeIf(v -> !isValidPartialWith(row, col, v));
                if (domains[row][col].isEmpty()) return false;
            }
        }
        return true;
    }


    public boolean isValidPartialWith(int r, int c, int val) {
        int old = values[r][c];
        values[r][c] = val;
        boolean ok = isValidPartial();
        values[r][c] = old;
        return ok;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : values) {
            for (int v : row) sb.append(v == -1 ? "." : v).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }
}