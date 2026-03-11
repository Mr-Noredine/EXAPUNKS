package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JPanel {

    // ---------- Level completion tracking (persists across resets) ----------
    public static int completedLevels = 0;

    public static void markLevelCompleted(int levelNumber) {
        if (levelNumber >= 1 && levelNumber <= 7)
            completedLevels |= (1 << (levelNumber - 1));
    }

    public static boolean isLevelCompleted(int levelNumber) {
        if (levelNumber < 1 || levelNumber > 7) return false;
        return (completedLevels & (1 << (levelNumber - 1))) != 0;
    }
    // -------------------------------------------------------------------------

    public JFrame frame;
    public JFrame frame2;
    JButton jouerButton;
    JButton quitterButton;
    TextZone textZone;
    TextZone textZone2;
    GamePanel gamePanel;
    Controller controller;
    Niveau niveau;
    JButton avancerButton;
    JButton stopButton;
    /** Panneau droit — affichage du contenu des fichiers en temps réel */
    private JTextArea fileContentsArea;

    public GameWindow() {
        fenetre_avant_jeu();
    }

    // ── Boot screen ───────────────────────────────────────────────────────────

    public void fenetre_avant_jeu() {
        SwingUtilities.invokeLater(() -> {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int w = (int)(screen.width  * 0.9);
            int h = (int)(screen.height * 0.9);

            frame = new JFrame("EXAPUNKS - System Boot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(w, h);
            frame.setResizable(true);
            frame.getContentPane().setBackground(Color.BLACK);

            BufferedImage image = null;
            try { image = ImageIO.read(new File("src/assets/images/im.png")); }
            catch (IOException e) { e.printStackTrace(); }

            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(w, h));

            if (image != null) {
                // Scale background to window size
                JLabel bg = new JLabel(new ImageIcon(
                    image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH)));
                bg.setBounds(0, 0, w, h);
                layeredPane.add(bg, Integer.valueOf(1));
            }

            jouerButton   = createButton("INITIALIZE MISSION", Color.BLACK,
                    e -> { afficherOptionsNiveau(); frame.dispose(); });
            quitterButton = createButton("TERMINATE SESSION", Color.BLACK, e -> {
                JOptionPane.showMessageDialog(null, "Session Terminated.");
                System.exit(0);
            });

            // Row of buttons, centered, with gap
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
            btnRow.setOpaque(false);
            btnRow.add(jouerButton);
            btnRow.add(quitterButton);

            // Bottom strip with padding so buttons sit above the very edge
            JPanel bottomStrip = new JPanel(new BorderLayout());
            bottomStrip.setOpaque(false);
            bottomStrip.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
            bottomStrip.add(btnRow, BorderLayout.CENTER);

            // Full-window transparent overlay — layout handles all positioning
            JPanel overlay = new JPanel(new BorderLayout());
            overlay.setOpaque(false);
            overlay.add(bottomStrip, BorderLayout.SOUTH);
            overlay.setBounds(0, 0, w, h);
            layeredPane.add(overlay, Integer.valueOf(2));

            frame.setContentPane(layeredPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // ── Main game window ──────────────────────────────────────────────────────

    public void afficherFenetreJeu(Niveau niveau) {
        this.niveau = niveau;

        if (controller != null) { controller.stopGameThread(); controller = null; }
        if (frame2 != null)       frame2.dispose();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int winW = (int)(screen.width  * 0.9);
        int winH = (int)(screen.height * 0.9);

        frame2 = new JFrame("EXAPUNKS  //  " + niveau.getMission());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(winW, winH);
        frame2.setResizable(true);
        frame2.setLayout(new BorderLayout());
        frame2.getContentPane().setBackground(new Color(15, 15, 20));

        // LEFT: code editor + action buttons
        JPanel leftPanel = setupTextAndButtonsPanel(niveau);
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 6));
        // Preferred width ~ 30% of window; the layout will stretch/shrink it
        leftPanel.setPreferredSize(new Dimension((int)(winW * 0.30), winH));

        // CENTER: game grid — fills all remaining space
        gamePanel = new GamePanel(niveau);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(15, 15, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 8));
        centerPanel.add(gamePanel, BorderLayout.CENTER);

        // SOUTH: shortcuts on left, mission objective on right — no scroll
        JPanel bottomPanel = buildBottomInfoPanel(niveau);

        // EAST: file contents viewer
        JPanel filePanel = buildFileContentsPanel(niveau);

        frame2.add(leftPanel,    BorderLayout.WEST);
        frame2.add(centerPanel,  BorderLayout.CENTER);
        frame2.add(bottomPanel,  BorderLayout.SOUTH);
        frame2.add(filePanel,    BorderLayout.EAST);

        setupShortcuts(frame2);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);

        controller = new Controller(this, niveau);
        controller.startGameThread();
    }

    // ── File contents panel (EAST) ────────────────────────────────────────────

    private JPanel buildFileContentsPanel(Niveau niveau) {
        fileContentsArea = new JTextArea();
        fileContentsArea.setEditable(false);
        fileContentsArea.setBackground(new Color(8, 10, 16));
        fileContentsArea.setForeground(new Color(0, 220, 255));
        fileContentsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        fileContentsArea.setMargin(new Insets(10, 10, 10, 10));
        fileContentsArea.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(fileContentsArea);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(new Color(8, 10, 16));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(8, 10, 16));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0, 60, 110)));

        JLabel header = new JLabel("  // FILES");
        header.setFont(new Font("Monospaced", Font.BOLD, 12));
        header.setForeground(new Color(0, 130, 180));
        header.setBackground(new Color(5, 7, 12));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 60, 110)));
        header.setPreferredSize(new Dimension(0, 28));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);

        refreshFileContents();
        return panel;
    }

    /** Met à jour l'affichage du contenu des fichiers — appelé après chaque step. */
    public void refreshFileContents() {
        if (fileContentsArea == null || niveau == null) return;
        Grille grille = niveau.getGrille();
        StringBuilder sb = new StringBuilder();

        for (Fichier f : grille.getListFichiers()) {
            // En-tête du fichier
            sb.append("┌─ F").append(f.getId());
            if (f.estTenuParRobot()) {
                sb.append("  [HELD]\n");
            } else {
                sb.append("  (").append(f.getPosX()).append(",").append(f.getPosY()).append(")\n");
            }

            // Contenu ligne par ligne
            java.util.List<String> contenu = f.getGestionFichier().getContenuCommeListe();
            if (contenu.isEmpty()) {
                sb.append("│  <empty>\n");
            } else {
                for (String val : contenu) {
                    sb.append("│  ").append(val).append("\n");
                }
            }
            sb.append("└─────────\n\n");
        }

        final String text = sb.toString();
        SwingUtilities.invokeLater(() -> {
            int caret = fileContentsArea.getCaretPosition();
            fileContentsArea.setText(text);
            try { fileContentsArea.setCaretPosition(caret); } catch (Exception ignored) {}
        });
    }

    // ── Bottom info panel (shortcuts | mission objective) ─────────────────────

    private JPanel buildBottomInfoPanel(Niveau niveau) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 0, 0));
        panel.setBackground(new Color(8, 8, 14));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 60, 110)));
        panel.setPreferredSize(new Dimension(0, 130));

        // LEFT: keyboard shortcuts
        JLabel shortcutsLabel = new JLabel(
            "<html><div style='font-family:monospace; font-size:14px; padding:8px 16px;'>"
            + "<span style='color:#5a5a7a'>RACCOURCIS CLAVIER :</span>&nbsp;&nbsp;"
            + "<span style='color:#3a3a5c'>[ Ctrl+→ ]</span> <span style='color:#6060aa'>STEP</span>"
            + "&nbsp;&nbsp;&nbsp;"
            + "<span style='color:#3a3a5c'>[ Ctrl+Entrée ]</span> <span style='color:#6060aa'>RUN</span>"
            + "&nbsp;&nbsp;&nbsp;"
            + "<span style='color:#3a3a5c'>[ Ctrl+P ]</span> <span style='color:#6060aa'>PAUSE</span>"
            + "&nbsp;&nbsp;&nbsp;"
            + "<span style='color:#3a3a5c'>[ Ctrl+R ]</span> <span style='color:#6060aa'>RESET</span>"
            + "</div></html>"
        );
        shortcutsLabel.setVerticalAlignment(SwingConstants.CENTER);

        // RIGHT: mission objective
        JLabel missionLabel = new JLabel(
            "<html><div style='font-family:monospace; font-size:14px; padding:8px 16px;'>"
            + "<span style='color:#5a6a7a'>OBJECTIF :</span>&nbsp;&nbsp;"
            + "<span style='color:#00b8e6'>" + escapeHtml(niveau.getMission()) + "</span>"
            + "</div></html>"
        );
        missionLabel.setVerticalAlignment(SwingConstants.CENTER);
        missionLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0, 50, 80)));

        panel.add(shortcutsLabel);
        panel.add(missionLabel);
        return panel;
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ── Left panel (text zones + action buttons) ──────────────────────────────

    private JPanel setupTextAndButtonsPanel(Niveau niveau) {
        JPanel container = new JPanel(new BorderLayout(0, 4));
        container.setOpaque(false);

        // For levels with 2 robots, stack two TextZones vertically
        if (niveau.getNbRobot() >= 2) {
            textZone  = new TextZone();
            textZone2 = new TextZone();
            JPanel textZonesPanel = new JPanel(new GridLayout(2, 1, 0, 4));
            textZonesPanel.setOpaque(false);
            textZonesPanel.add(textZone);
            textZonesPanel.add(textZone2);
            container.add(textZonesPanel, BorderLayout.CENTER);
        } else {
            textZone = new TextZone();
            container.add(textZone, BorderLayout.CENTER);
        }

        container.add(buildActionPanel(), BorderLayout.SOUTH);
        return container;
    }

    private JPanel buildActionPanel() {
        JButton resetBtn    = new JButton("RESET");
        JButton stepBtn     = new JButton("STEP >>");
        JButton runBtn      = new JButton("RUN ALL");
        JButton pauseBtn    = new JButton("PAUSE");
        JButton quitBtn     = new JButton("QUIT LEVEL");

        styleActionButton(resetBtn,  new Color(150, 0,   0  ));
        styleActionButton(stepBtn,   new Color(0,   100, 200));
        styleActionButton(runBtn,    new Color(51,  153, 51 ));
        styleActionButton(pauseBtn,  new Color(200, 150, 0  ));
        styleActionButton(quitBtn,   new Color(180, 60,  60 ));

        JPanel buttonRow = new JPanel(new GridLayout(1, 5, 4, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(resetBtn);
        buttonRow.add(stepBtn);
        buttonRow.add(runBtn);
        buttonRow.add(pauseBtn);
        buttonRow.add(quitBtn);

        // Speed slider row
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 1000, 300);
        speedSlider.setOpaque(false);
        speedSlider.setInverted(true);
        JLabel speedLabel = new JLabel("DELAI: 300ms");
        speedLabel.setForeground(new Color(0, 200, 255));
        speedLabel.setFont(new Font("Monospaced", Font.BOLD, 11));

        JPanel sliderRow = new JPanel(new BorderLayout(6, 0));
        sliderRow.setOpaque(false);
        sliderRow.add(speedLabel,  BorderLayout.WEST);
        sliderRow.add(speedSlider, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(2, 1, 0, 4));
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        south.add(buttonRow);
        south.add(sliderRow);

        // Wire listeners
        resetBtn.addActionListener(e -> { if (controller != null) controller.getGameController().resetGame(); });
        stepBtn .addActionListener(e -> SharedSemaphore.release());
        runBtn  .addActionListener(e -> { if (controller != null) controller.getGameController().startAutoRun(); });
        pauseBtn.addActionListener(e -> { if (controller != null) controller.getGameController().stopAutoRun(); });
        speedSlider.addChangeListener(e -> {
            speedLabel.setText("DELAI: " + speedSlider.getValue() + "ms");
            if (controller != null) controller.getGameController().setSpeed(speedSlider.getValue());
        });
        quitBtn.addActionListener(e -> {
            if (controller != null) controller.getGameController().stopAutoRun();
            int choice = JOptionPane.showConfirmDialog(
                frame2,
                "Quitter ce niveau et retourner au menu ?",
                "Quitter le niveau",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                if (controller != null) { controller.stopGameThread(); controller = null; }
                if (frame2 != null)     { frame2.dispose(); frame2 = null; }
                afficherOptionsNiveau();
            }
        });

        return south;
    }

    // ── Button factories ──────────────────────────────────────────────────────

    private static JButton createButton(String text, Color color, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(10, 20, 10));
        btn.setForeground(new Color(51, 255, 51));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(720, 162));
        btn.setFont(new Font("Monospaced", Font.BOLD, 48));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 60, 0));
                btn.setBorder(BorderFactory.createLineBorder(new Color(51, 255, 51), 2));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(10, 20, 10));
                btn.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 2));
            }
        });
        btn.addActionListener(listener);
        return btn;
    }

    private void styleActionButton(JButton btn, Color accent) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(accent);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(accent, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(
                    Math.min(accent.getRed()   / 4, 255),
                    Math.min(accent.getGreen() / 4, 255),
                    Math.min(accent.getBlue()  / 4, 255)));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.BLACK);
            }
        });
    }

    // ── Keyboard shortcuts ────────────────────────────────────────────────────

    private void setupShortcuts(JFrame targetFrame) {
        JRootPane rp = targetFrame.getRootPane();
        InputMap  im = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rp.getActionMap();

        im.put(KeyStroke.getKeyStroke("ctrl RIGHT"), "step");
        am.put("step", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                SharedSemaphore.release();
            }
        });
        im.put(KeyStroke.getKeyStroke("ctrl ENTER"), "run");
        am.put("run", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller != null) controller.getGameController().startAutoRun();
            }
        });
        im.put(KeyStroke.getKeyStroke("ctrl P"), "pause");
        am.put("pause", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller != null) controller.getGameController().stopAutoRun();
            }
        });
        im.put(KeyStroke.getKeyStroke("ctrl R"), "reset");
        am.put("reset", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller != null) controller.getGameController().resetGame();
            }
        });
    }

    // ── Level select screen ───────────────────────────────────────────────────

    public void afficherOptionsNiveau() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)(screen.width  * 0.9);
        int h = (int)(screen.height * 0.9);

        JFrame niveauFrame = new JFrame("EXAPUNKS - Choix du Niveau");
        niveauFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        niveauFrame.setSize(w, h);
        niveauFrame.setResizable(true);

        BufferedImage image = null;
        try { image = ImageIO.read(new File("src/assets/images/im.png")); }
        catch (IOException e) { e.printStackTrace(); }

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(w, h));

        if (image != null) {
            JLabel bg = new JLabel(new ImageIcon(
                image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH)));
            bg.setBounds(0, 0, w, h);
            layeredPane.add(bg, Integer.valueOf(1));
        }

        String[] difficulties = {"MOYEN", "REFLECHIR", "DIFFICILE", "AVANCE", "EXPERT", "MASTER", "ULTIME"};
        JButton[] btns = new JButton[7];
        for (int i = 0; i < 7; i++) {
            btns[i] = buildLevelButtonWithDifficulty(i + 1, difficulties[i]);
        }

        btns[0].addActionListener(e -> { niveau = new Niveau1(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[1].addActionListener(e -> { niveau = new Niveau2(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[2].addActionListener(e -> { niveau = new Niveau3(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[3].addActionListener(e -> { niveau = new Niveau4(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[4].addActionListener(e -> { niveau = new Niveau5(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[5].addActionListener(e -> { niveau = new Niveau6(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });
        btns[6].addActionListener(e -> { niveau = new Niveau7(); afficherFenetreJeu(niveau); niveauFrame.dispose(); });

        // 7 boutons en grille 3 colonnes
        JPanel btnGrid = new JPanel(new GridLayout(0, 3, 16, 16));
        btnGrid.setOpaque(false);
        for (JButton btn : btns) {
            btnGrid.add(btn);
        }

        // Wrap in a bottom strip with padding
        JPanel bottomStrip = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomStrip.setOpaque(false);
        bottomStrip.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        bottomStrip.add(btnGrid);

        // Full-window transparent overlay — layout handles all positioning
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        overlay.add(bottomStrip, BorderLayout.SOUTH);
        overlay.setBounds(0, 0, w, h);
        layeredPane.add(overlay, Integer.valueOf(2));

        niveauFrame.setContentPane(layeredPane);
        niveauFrame.setLocationRelativeTo(null);
        niveauFrame.setVisible(true);
    }

    private JButton buildLevelButtonWithDifficulty(int levelNumber, String difficulty) {
        boolean done = isLevelCompleted(levelNumber);
        String label = done
            ? "NIVEAU " + levelNumber + " [" + difficulty + "] [OK]"
            : "NIVEAU " + levelNumber + " [" + difficulty + "]";
        Color fg = done ? new Color(0, 220, 110) : new Color(51, 255, 51);

        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(300, 60));
        btn.setBackground(new Color(10, 18, 10));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createLineBorder(fg, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverBg  = done ? new Color(0, 45, 22) : new Color(0, 35, 0);
        Color hoverBrd = fg.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.setBorder(BorderFactory.createLineBorder(hoverBrd, 2));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(10, 18, 10));
                btn.setBorder(BorderFactory.createLineBorder(fg, 2));
            }
        });
        return btn;
    }

    // ── Win / Defeat screens ──────────────────────────────────────────────────

    public void showWinScreen(int cycles, int codeSize, int networkActivity) {
        int levelNumber = (niveau != null) ? niveau.getNbNiveau() : 0;
        markLevelCompleted(levelNumber);
        WinScreen dialog = new WinScreen(
            frame2, levelNumber, cycles, codeSize, networkActivity,
            () -> { if (frame2 != null) frame2.dispose(); afficherOptionsNiveau(); },
            () -> { JOptionPane.showMessageDialog(null, "Session Terminated."); System.exit(0); }
        );
        dialog.setVisible(true);
    }

    public void showDefeatScreen(String reason) {
        DefeatScreen dialog = new DefeatScreen(
            frame2, reason,
            () -> { if (controller != null) controller.getGameController().resetGame(); },
            () -> {
                if (controller != null) { controller.stopGameThread(); controller = null; }
                if (frame2 != null)     { frame2.dispose(); frame2 = null; }
                afficherOptionsNiveau();
            }
        );
        dialog.setVisible(true);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public TextZone   getTextZone()   { return textZone;   }
    public TextZone   getTextZone2()  { return textZone2;  }
    public GamePanel  getGamePanel()  { return gamePanel;  }
    public Controller getController() { return controller; }
    public Niveau     getNiveau()     { return niveau;     }

    public JButton getAvancerButton()              { return avancerButton; }
    public JButton getStopButton()                 { return stopButton;    }
    public void    setAvancerButton(JButton b)     { avancerButton = b;    }
    public void    setStopButton   (JButton b)     { stopButton    = b;    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gamePanel != null) gamePanel.paintComponent(g);
    }
}
