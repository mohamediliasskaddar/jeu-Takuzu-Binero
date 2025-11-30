package view;

import model.Grid;
import model.GridGenerator;
import controller.UserGame;
import Solver.BacktrackingSolver;
import utils.GridUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BinairoApp extends JFrame {

    private Grid grid;
    private UserGame userGame;

    private JButton[][] buttons;
    private JPanel gridPanel;
    private JLabel statusLabel;

    public BinairoApp() {
        setTitle(" Binairo / Takuzu ");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // zone de messages
        statusLabel = new JLabel("Bienvenue dans Binairo !");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(statusLabel, BorderLayout.SOUTH);

        // Menu complet
        setJMenuBar(buildMenu());

        // panneau vide initial
        gridPanel = new JPanel();
        add(gridPanel, BorderLayout.CENTER);

        // **Message d'accueil**
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenue !\nVeuillez créer une nouvelle grille ou choisir un modèle prêt à jouer.",
                    "Bienvenue",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        setVisible(true);
    }


    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();
        bar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        bar.setBackground(new Color(245, 245, 245));

        // ---------- FILE SHORTCUT BUTTONS ----------
        JButton btnSave = styleButton("💾 Sauvegarder");
        btnSave.addActionListener(e -> saveGrid());

        JButton btnLoad = styleButton("📂 Charger");
        btnLoad.addActionListener(e -> loadGridFromFile());

        JButton btnExit = styleButton("❌ Quitter");
        btnExit.addActionListener(e -> System.exit(0));

        bar.add(btnSave);
        bar.add(btnLoad);
        bar.add(btnExit);

        // Separator
        bar.add(Box.createHorizontalStrut(20));
        bar.add(new JSeparator(SwingConstants.VERTICAL));
        bar.add(Box.createHorizontalStrut(20));

        // ---------- GRID MENU ----------
        // Menu principal
        JMenu gridMenu = styleMenu("Grilles prêtes");

        // Bouton pour créer une grille manuellement (directement dans la bar)
        JButton btnManual = styleButton("➕ Créer une grille");
        btnManual.setToolTipText("Créer une nouvelle grille vide ou personnalisée");
        btnManual.addActionListener(e -> createGridDialog());
        bar.add(btnManual);

        // Options de grilles pré-générées
        JMenuItem beginner = styleMenuItem("Générer 6×6 (Débutant)");
        JMenuItem interm   = styleMenuItem("Générer 8×8 (Intermédiaire)");
        JMenuItem expert   = styleMenuItem("Générer 10×10 (Expert)");

        beginner.addActionListener(e -> loadGrid(GridGenerator.beginner6()));
        interm.addActionListener(e -> loadGrid(GridGenerator.intermediate8()));
        expert.addActionListener(e -> loadGrid(GridGenerator.expert10()));

        gridMenu.add(beginner);
        gridMenu.add(interm);
        gridMenu.add(expert);

        bar.add(gridMenu);

        // Separator
        bar.add(Box.createHorizontalStrut(20));
        bar.add(new JSeparator(SwingConstants.VERTICAL));
        bar.add(Box.createHorizontalStrut(20));

        // ---------- SOLVER MENU ----------
        JMenu solverMenu = styleMenu("Solveur");

        JMenuItem solveBasic  = styleMenuItem("Backtracking classique");
        JMenuItem solveOptim  = styleMenuItem("Optimisé (FC + AC3 + LCV)");
        JMenuItem suggest     = styleMenuItem("Suggestion IA");
        JMenuItem checkSolvable = styleMenuItem("Tester Résolubilité");

        solveBasic.addActionListener(e -> runSolver(true));
        solveOptim.addActionListener(e -> runSolver(false));
        suggest.addActionListener(e -> showSuggestion());
        checkSolvable.addActionListener(e -> testSolvability());

        solverMenu.add(solveBasic);
        solverMenu.add(solveOptim);
        solverMenu.add(suggest);
        solverMenu.add(checkSolvable);

        // Bouton "Règles du jeu"
        JButton btnRules = styleButton("📖 Règles");
        btnRules.setToolTipText("Cliquez pour voir les règles du jeu Binairo");
        btnRules.addActionListener(e -> showRules());
        bar.add(btnRules);

        bar.add(solverMenu);

        return bar;
    }
    private JButton styleButton(String text) { JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(230, 230, 230));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(210, 210, 210)); }
            public void mouseExited(java.awt.event.MouseEvent evt)
            { b.setBackground(new Color(230, 230, 230)); } });
        return b;
    }

    // Style uniforme pour les menus
    private JMenu styleMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.setOpaque(true);
        menu.setBackground(new Color(230, 230, 230));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    // Style uniforme pour les items du menu
    private JMenuItem styleMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Arial", Font.PLAIN, 12));
        item.setBackground(new Color(230, 230, 230));
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(210, 210, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(230, 230, 230));
            }
        });

        return item;
    }

    private void showRules() {
        String rules = """
        Règles du jeu Binairo :
        1. Maximum deux chiffres identiques côte à côte.
        2. Même nombre de 0 et de 1 dans chaque ligne et dans chaque colonne 
        ( paires: Egalité parfaite et impaires: différence d'une unité).
        3. Toutes les lignes et colonnes doivent être uniques.
        4. Les cases déjà données ne peuvent pas être modifiées.
        """;
        JOptionPane.showMessageDialog(this, rules, "Règles du jeu", JOptionPane.INFORMATION_MESSAGE);
    }


    // =========================
    //  AFFICHAGE DE LA GRILLE
    // =========================
    private void loadGrid(Grid g) {
        this.grid = g;
        this.userGame = new UserGame(g);

        int size = g.getSize();
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(size, size, 2, 2));
        buttons = new JButton[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton b = new JButton();
                b.setFont(new Font("Arial", Font.BOLD, 22));
                b.setFocusPainted(false);

                int val = g.get(r, c);
                if (val != -1) {
                    b.setText(String.valueOf(val));
                    b.setBackground(new Color(210,210,210));
                    b.setEnabled(false);
                } else {
                    b.setText("");
                    b.setBackground(Color.WHITE);
                    b.setEnabled(true);
                }

                int finalR = r, finalC = c;
                b.addActionListener(e -> handleCellClick(finalR, finalC));

                buttons[r][c] = b;
                gridPanel.add(b);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
        statusLabel.setText("Nouvelle grille chargée ("+size+"×"+size+").");
    }

    // =========================
    //  CLICK D’UNE CASE
    // =========================
    private void handleCellClick(int r, int c) {
        if (grid == null) return;
        if (grid.isGiven(r, c)) {
            statusLabel.setText(" Case donnée — modification interdite.");
            return;
        }

        // Valeurs cycliques : -1 (vide) → 0 → 1 → -1
        int current = grid.get(r, c);
        int next;
        if (current == -1) next = 0;
        else if (current == 0) next = 1;
        else next = -1; // 1 devient vide

        // Jouer le coup
        String result = userGame.playMove(r, c, next);

        // Mettre à jour l'affichage
        if (next == -1) buttons[r][c].setText("");
        else buttons[r][c].setText(String.valueOf(next));

        // Changer la couleur si invalidité
        if (!grid.isValidPartial()) {
            buttons[r][c].setBackground(new Color(255, 190, 190));
            statusLabel.setText(" Règle violée !");
        } else {
            buttons[r][c].setBackground(Color.WHITE);
            statusLabel.setText(result);
        }
    }


    // =========================
    //  SOLUTION AUTOMATIQUE
    // =========================
    private void runSolver(boolean optimized) {
        if (grid == null) return;

        Grid copy = grid.copy();
        BacktrackingSolver solver = new BacktrackingSolver();
        long startTime = System.currentTimeMillis(); // début
        boolean ok = optimized ? solver.solveOptimized(copy) : solver.solve(copy);
        long endTime = System.currentTimeMillis();   // fin


        if (!ok) {
            JOptionPane.showMessageDialog(this, "Pas de solution !");
            return;
        }

        // afficher solution
        int size = grid.getSize();
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++) {
                buttons[r][c].setText(String.valueOf(copy.get(r,c)));
                buttons[r][c].setBackground(new Color(200, 255, 200));
            }

        // afficher le temps
        long duration = endTime - startTime;
        JOptionPane.showMessageDialog(this, "Solution trouvée en " + duration + " ms !");
    }

    // =========================
    //  SUGGESTION (Valide à partir d'un solver)
    // =========================
    private void showSuggestion() {
        if (grid == null) return;
        String s = userGame.suggestMove();
        statusLabel.setText(s);
        JOptionPane.showMessageDialog(this, s);
    }

    // =========================
    //  DIALOGUE DE CRÉATION (taille + empty / difficulty)
    // =========================
    private void createGridDialog() {
        JPanel panel = new JPanel(new GridLayout(0,1,4,4));
        JComboBox<String> sizeBox = new JComboBox<>(new String[]{"3","4","5","6","7","8","9","10","11"});
        JComboBox<String> diffBox = new JComboBox<>(new String[]{"Empty","Easy","Medium","Hard"});
        panel.add(new JLabel("Taille :"));
        panel.add(sizeBox);
        panel.add(new JLabel("Type :"));
        panel.add(diffBox);

        int res = JOptionPane.showConfirmDialog(this, panel, "Créer une grille", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        int size = Integer.parseInt((String)sizeBox.getSelectedItem());
        String diff = (String)diffBox.getSelectedItem();

        if ("Empty".equals(diff)) {
            Grid g = new Grid(size); // totalement vide
            loadGrid(g);
        } else {
            double perc = switch (diff) {
                case "Easy" -> 0.40;
                case "Medium" -> 0.55;
                case "Hard" -> 0.70;
                default -> 0.55;
            };
            Grid g = GridGenerator.generate(size, perc);
            loadGrid(g);
        }
    }

    // =========================
    //  SAUVEGARDE
    // =========================
    private void saveGrid() {
        if (grid == null) return;

        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(fc.getSelectedFile())) {
                int size = grid.getSize();
                pw.println(size);
                for (int r = 0; r < size; r++) {
                    for (int c = 0; c < size; c++) {
                        int v = grid.get(r,c);
                        // on écrit -1 pour vide pour être explicite
                        pw.print(v + (c + 1 < size ? " " : ""));
                    }
                    pw.println();
                }
                JOptionPane.showMessageDialog(this,"Grille sauvegardée !");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Erreur de sauvegarde : " + ex.getMessage());
            }
        }
    }

    // =========================
    //  CHARGEMENT (UI) -> utilise readGridFromFile
    // =========================
    private void loadGridFromFile() {
        JOptionPane.showMessageDialog(
                this,
                "Structure attendue du fichier :\n" +
                        "- Première ligne : taille de la grille (ex : 6)\n" +
                        "- Lignes suivantes : chaque ligne contient 'size' nombres séparés par espaces (0 ou 1)\n" +
                        "- Ou chaque ligne peut être une suite de 0,1 ou '-1' pour vide\n" +
                        "- Exemple pour 4x4 :\n" +
                        "4\n" +
                        "0 1 0 1\n" +
                        "1 0 1 0\n" +
                        "0 1 0 1\n" +
                        "1 0 1 0",
                "Info fichier",
                JOptionPane.INFORMATION_MESSAGE
        );
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Grid g = readGridFromFile(f);
                loadGrid(g);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Erreur de chargement : " + ex.getMessage());
            }
        }
    }

    /**
     * Lecture robuste d'un fichier de grille.
     * Supporte deux formats communs :
     * - Première ligne = taille (N), puis N lignes avec N entiers séparés par espaces (ou -1 pour vide)
     * - Ou fichier où chaque ligne contient N caractères ('0','1','.' ou '-','1'...) ; on ignore espaces.
     */
    private Grid readGridFromFile(File file) throws IOException {


        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.strip().isEmpty()) continue;
                lines.add(l);
            }
        }

        if (lines.isEmpty()) throw new IOException("Fichier vide");

        // si première ligne est un entier (taille), on lit le reste en nombres
        String first = lines.get(0).trim();
        if (first.matches("^\\d+$") && lines.size() >= Integer.parseInt(first) + 1) {
            int size = Integer.parseInt(first);
            Grid g = new Grid(size);
            for (int r = 0; r < size; r++) {
                String rowLine = lines.get(r+1).trim();
                String[] tokens = rowLine.split("\\s+");
                if (tokens.length < size) throw new IOException("Ligne " + (r+1) + " a moins de " + size + " éléments.");
                for (int c = 0; c < size; c++) {
                    int v = Integer.parseInt(tokens[c]);
                    g.set(r, c, v);
                    if (v != -1) g.setGiven(r, c, v);
                }
            }
            return g;
        }

        // sinon on suppose chaque ligne est une suite de caractères (ex: ".01.10")
        int size = lines.size();
        Grid g = new Grid(size);
        for (int r = 0; r < size; r++) {
            String rowLine = lines.get(r).replaceAll("\\s+", "");
            if (rowLine.length() != size) throw new IOException("La ligne " + (r+1) + " n'a pas la bonne longueur.");
            for (int c = 0; c < size; c++) {
                char ch = rowLine.charAt(c);
                if (ch == '.' || ch == '-' ) {
                    g.set(r, c, -1);
                } else if (ch == '0' || ch == '1' || Character.isDigit(ch)) {
                    int v = Character.getNumericValue(ch);
                    g.set(r, c, v);
                    g.setGiven(r, c, v);
                } else {
                    throw new IOException("Caractère invalide: '" + ch + "' sur la ligne " + (r+1));
                }
            }
        }
        return g;
    }

    private void testSolvability() {
        if (grid == null) {
            JOptionPane.showMessageDialog(this, "Aucune grille chargée.");
            return;
        }

        boolean solvable = GridUtils.isSolvable(grid);

        if (solvable) {
            JOptionPane.showMessageDialog(this, " La grille est solvable !");
            statusLabel.setText("✔ Grille solvable.");
        } else {
            JOptionPane.showMessageDialog(this, " La grille n'est PAS solvable.");
            statusLabel.setText(" Grille irrésoluble.");
        }
    }

    // =========================
    //  MAIN
    // =========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinairoApp::new);
    }
}
