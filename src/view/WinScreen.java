package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

/**
 * WinScreen is a modal JDialog displayed when testVictoire() returns true.
 *
 * It renders a styled panel consistent with the game's dark EXAPUNKS aesthetic
 * (black background, green-on-black text, monospaced font, glowing borders).
 *
 * The caller supplies:
 *   - the level number
 *   - cycle count, code size, and network activity stats
 *   - callbacks for "Level Select" and "Quit"
 */
public class WinScreen extends JDialog {

    // Palette shared with the rest of the UI
    private static final Color BG_DARK       = new Color(10, 10, 15);
    private static final Color BG_PANEL      = new Color(20, 22, 28);
    private static final Color GREEN_BRIGHT  = new Color(51, 255, 51);
    private static final Color GREEN_DIM     = new Color(0, 153, 0);
    private static final Color GREEN_GLOW    = new Color(51, 255, 51, 60);
    private static final Color CYAN_ACCENT   = new Color(0, 200, 255);
    private static final Color GOLD_ACCENT   = new Color(255, 200, 0);
    private static final Color TEXT_DIM      = new Color(150, 150, 160);
    private static final Font  MONO_LARGE    = new Font("Monospaced", Font.BOLD, 28);
    private static final Font  MONO_MEDIUM   = new Font("Monospaced", Font.BOLD, 16);
    private static final Font  MONO_SMALL    = new Font("Monospaced", Font.PLAIN, 13);

    /**
     * @param owner          the parent JFrame (frame2 — the game window)
     * @param levelNumber    level that was just completed
     * @param cycles         number of execution cycles used
     * @param codeSize       total non-empty, non-comment instruction lines
     * @param networkActivity number of LINK instructions executed
     * @param onLevelSelect  Runnable invoked when the player clicks "LEVEL SELECT"
     * @param onQuit         Runnable invoked when the player clicks "QUIT"
     */
    public WinScreen(JFrame owner,
                     int levelNumber,
                     int cycles,
                     int codeSize,
                     int networkActivity,
                     Runnable onLevelSelect,
                     Runnable onQuit) {

        super(owner, "MISSION ACCOMPLISHED", true);
        setUndecorated(true);
        setBackground(BG_DARK);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel root = buildRootPanel(levelNumber, cycles, codeSize, networkActivity, onLevelSelect, onQuit);
        setContentPane(root);
    }

    // ------------------------------------------------------------------
    // Panel construction
    // ------------------------------------------------------------------

    private JPanel buildRootPanel(int levelNumber, int cycles, int codeSize,
                                  int networkActivity,
                                  Runnable onLevelSelect, Runnable onQuit) {

        // Outer panel draws the glowing border
        JPanel outer = new GlowBorderPanel();
        outer.setBackground(BG_DARK);
        outer.setLayout(new BorderLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        // Inner content panel
        JPanel inner = new JPanel();
        inner.setBackground(BG_PANEL);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(30, 40, 24, 40));

        inner.add(buildTitleSection(levelNumber));
        inner.add(Box.createVerticalStrut(24));
        inner.add(buildDivider());
        inner.add(Box.createVerticalStrut(20));
        inner.add(buildStatsSection(cycles, codeSize, networkActivity));
        inner.add(Box.createVerticalStrut(28));
        inner.add(buildDivider());
        inner.add(Box.createVerticalStrut(24));
        inner.add(buildButtonRow(onLevelSelect, onQuit));

        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    // Title: animated-style header lines + level badge
    private JPanel buildTitleSection(int levelNumber) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel prefix = makeLabel("// EXECUTION COMPLETE", MONO_SMALL, GREEN_DIM);
        prefix.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = makeLabel("MISSION ACCOMPLISHED", MONO_LARGE, GREEN_BRIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel badge = makeLabel(
            String.format("[ LEVEL %d CLEARED ]", levelNumber),
            MONO_MEDIUM, GOLD_ACCENT
        );
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(prefix);
        panel.add(Box.createVerticalStrut(8));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(badge);
        return panel;
    }

    // Stats grid: cycles / code size / network activity
    private JPanel buildStatsSection(int cycles, int codeSize, int networkActivity) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 12, 8));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addStatRow(panel, "CYCLES",           String.valueOf(cycles),          CYAN_ACCENT);
        addStatRow(panel, "CODE SIZE",        String.valueOf(codeSize),        CYAN_ACCENT);
        addStatRow(panel, "NETWORK ACTIVITY", String.valueOf(networkActivity), CYAN_ACCENT);

        // Wrap in a centered container so the grid does not stretch full-width
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(panel);
        return wrapper;
    }

    private void addStatRow(JPanel panel, String label, String value, Color valueColor) {
        JLabel lbl = makeLabel(label + ":", MONO_SMALL, TEXT_DIM);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel val = makeLabel(value, new Font("Monospaced", Font.BOLD, 15), valueColor);
        val.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(lbl);
        panel.add(val);
    }

    // Thin horizontal separator
    private JPanel buildDivider() {
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(GREEN_DIM);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }
        };
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        return line;
    }

    // "LEVEL SELECT" and "QUIT" buttons
    private JPanel buildButtonRow(Runnable onLevelSelect, Runnable onQuit) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        row.setOpaque(false);

        JButton levelSelectBtn = buildActionButton(
            "LEVEL SELECT",
            new Color(0, 100, 200),
            e -> {
                dispose();
                onLevelSelect.run();
            }
        );

        JButton quitBtn = buildActionButton(
            "QUIT",
            new Color(150, 30, 30),
            e -> {
                dispose();
                onQuit.run();
            }
        );

        row.add(levelSelectBtn);
        row.add(quitBtn);
        return row;
    }

    // ------------------------------------------------------------------
    // Widget helpers
    // ------------------------------------------------------------------

    private static JLabel makeLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setOpaque(false);
        return lbl;
    }

    private static JButton buildActionButton(String text, Color accentColor, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 44));
        btn.setBackground(Color.BLACK);
        btn.setForeground(accentColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover highlight
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(
                    accentColor.getRed()   / 6,
                    accentColor.getGreen() / 6,
                    accentColor.getBlue()  / 6
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.BLACK);
            }
        });

        btn.addActionListener(listener);
        return btn;
    }

    // ------------------------------------------------------------------
    // Custom panel that paints a glowing green outer border
    // ------------------------------------------------------------------

    private static class GlowBorderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Multi-layer glow: paint several translucent rectangles shrinking inward
            int layers = 6;
            for (int i = layers; i >= 1; i--) {
                int alpha = (int) (GREEN_GLOW.getAlpha() * ((double)(layers - i + 1) / layers));
                g2.setColor(new Color(GREEN_BRIGHT.getRed(), GREEN_BRIGHT.getGreen(), GREEN_BRIGHT.getBlue(), alpha));
                g2.setStroke(new BasicStroke(i));
                g2.drawRect(i - 1, i - 1, getWidth() - (i * 2) + 1, getHeight() - (i * 2) + 1);
            }

            // Solid innermost border line
            g2.setColor(GREEN_DIM);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(layers, layers, getWidth() - (layers * 2) - 1, getHeight() - (layers * 2) - 1);
        }
    }
}
