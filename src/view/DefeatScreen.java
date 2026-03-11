package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * DefeatScreen is a modal JDialog displayed when the robot encounters a fatal
 * error (invalid instruction, out-of-bounds access, etc.).
 *
 * Style is consistent with WinScreen: dark EXAPUNKS aesthetic,
 * black background, coloured borders, monospaced font.
 *
 * Buttons:
 *   - REESSAYER  : resets the level and resumes editing
 *   - RETOUR AU MENU : goes back to the level-select screen
 */
public class DefeatScreen extends JDialog {

    // Shared palette (same constants as WinScreen)
    private static final Color BG_DARK      = new Color(10, 10, 15);
    private static final Color BG_PANEL     = new Color(20, 14, 14);   // slight red tint
    private static final Color RED_BRIGHT   = new Color(255, 60, 60);
    private static final Color RED_DIM      = new Color(153, 0, 0);
    private static final Color RED_GLOW     = new Color(255, 60, 60, 60);
    private static final Color CYAN_ACCENT  = new Color(0, 200, 255);
    private static final Color TEXT_DIM     = new Color(150, 130, 130);
    private static final Font  MONO_LARGE   = new Font("Monospaced", Font.BOLD, 26);
    private static final Font  MONO_MEDIUM  = new Font("Monospaced", Font.BOLD, 15);
    private static final Font  MONO_SMALL   = new Font("Monospaced", Font.PLAIN, 13);

    /**
     * @param owner      parent JFrame (the game window)
     * @param reason     human-readable reason for the failure
     * @param onRetry    Runnable invoked when the player clicks "REESSAYER"
     * @param onMenu     Runnable invoked when the player clicks "RETOUR AU MENU"
     */
    public DefeatScreen(JFrame owner,
                        String reason,
                        Runnable onRetry,
                        Runnable onMenu) {
        super(owner, "EXECUTION ECHOUEE", true);
        setUndecorated(true);
        setBackground(BG_DARK);
        setSize(500, 360);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setContentPane(buildRoot(reason, onRetry, onMenu));
    }

    // ------------------------------------------------------------------
    // Panel construction
    // ------------------------------------------------------------------

    private JPanel buildRoot(String reason, Runnable onRetry, Runnable onMenu) {
        JPanel outer = new GlowBorderPanel();
        outer.setBackground(BG_DARK);
        outer.setLayout(new BorderLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JPanel inner = new JPanel();
        inner.setBackground(BG_PANEL);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(28, 40, 22, 40));

        inner.add(buildTitleSection());
        inner.add(Box.createVerticalStrut(20));
        inner.add(buildDivider());
        inner.add(Box.createVerticalStrut(16));
        inner.add(buildReasonSection(reason));
        inner.add(Box.createVerticalStrut(24));
        inner.add(buildDivider());
        inner.add(Box.createVerticalStrut(22));
        inner.add(buildButtonRow(onRetry, onMenu));

        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildTitleSection() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel prefix = makeLabel("// EXECUTION TERMINEE AVEC ERREUR", MONO_SMALL, RED_DIM);
        prefix.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = makeLabel("MISSION ECHOUEE", MONO_LARGE, RED_BRIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(prefix);
        panel.add(Box.createVerticalStrut(8));
        panel.add(title);
        return panel;
    }

    private JPanel buildReasonSection(String reason) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel reasonLabel = makeLabel("ERREUR :", MONO_MEDIUM, TEXT_DIM);
        reasonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Wrap long reason text
        JTextArea reasonText = new JTextArea(reason);
        reasonText.setOpaque(false);
        reasonText.setForeground(CYAN_ACCENT);
        reasonText.setFont(MONO_SMALL);
        reasonText.setEditable(false);
        reasonText.setFocusable(false);
        reasonText.setLineWrap(true);
        reasonText.setWrapStyleWord(true);
        reasonText.setAlignmentX(Component.CENTER_ALIGNMENT);
        reasonText.setMaximumSize(new Dimension(380, 80));

        panel.add(reasonLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(reasonText);
        return panel;
    }

    private JPanel buildButtonRow(Runnable onRetry, Runnable onMenu) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        row.setOpaque(false);

        JButton retryBtn = buildActionButton(
            "REESSAYER",
            new Color(0, 130, 60),
            e -> { dispose(); onRetry.run(); }
        );

        JButton menuBtn = buildActionButton(
            "RETOUR AU MENU",
            new Color(150, 30, 30),
            e -> { dispose(); onMenu.run(); }
        );

        row.add(retryBtn);
        row.add(menuBtn);
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
        btn.setPreferredSize(new Dimension(180, 44));
        btn.setBackground(Color.BLACK);
        btn.setForeground(accentColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    // Thin horizontal separator
    private JPanel buildDivider() {
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(RED_DIM);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }
        };
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        return line;
    }

    // ------------------------------------------------------------------
    // Custom panel with a glowing red outer border
    // ------------------------------------------------------------------

    private static class GlowBorderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int layers = 6;
            for (int i = layers; i >= 1; i--) {
                int alpha = (int) (RED_GLOW.getAlpha() * ((double)(layers - i + 1) / layers));
                g2.setColor(new Color(RED_BRIGHT.getRed(), RED_BRIGHT.getGreen(), RED_BRIGHT.getBlue(), alpha));
                g2.setStroke(new BasicStroke(i));
                g2.drawRect(i - 1, i - 1, getWidth() - (i * 2) + 1, getHeight() - (i * 2) + 1);
            }

            g2.setColor(RED_DIM);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(layers, layers, getWidth() - (layers * 2) - 1, getHeight() - (layers * 2) - 1);
        }
    }
}
