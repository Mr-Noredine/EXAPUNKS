package view;

import javax.swing.*;
import java.awt.*;

/**
 * TextZone: code editor + register display.
 * Uses BorderLayout so the text editor fills all available height/width,
 * and the 4 registers (X, T, F, M) are stacked in a fixed-width column on the right.
 */
public class TextZone extends JPanel {

    private JTextArea textArea;
    public  JTextArea memoryArea1;   // X
    private JTextArea memoryArea2;   // T
    private JTextArea memoryArea3;   // F
    private JTextArea memoryArea4;   // M

    public TextZone() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.BLACK);

        // ── Code editor (fills CENTER) ──────────────────────────────────────
        textArea = new JTextArea();
        textArea.setBackground(new Color(10, 10, 10));
        textArea.setForeground(new Color(51, 255, 51));
        textArea.setCaretColor(Color.GREEN);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(6, 8, 6, 8));

        JScrollPane editorScroll = new JScrollPane(textArea);
        editorScroll.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 0), 1));
        editorScroll.getViewport().setBackground(new Color(10, 10, 10));
        editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ── Register panel (EAST, fixed width so registers are always visible) ─
        JPanel memoryPanel = new JPanel(new GridLayout(4, 1, 2, 2));
        memoryPanel.setBackground(Color.BLACK);
        memoryPanel.setPreferredSize(new Dimension(72, 0));
        memoryPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 2));

        configureRegister(memoryArea1 = new JTextArea(), "X", memoryPanel);
        configureRegister(memoryArea2 = new JTextArea(), "T", memoryPanel);
        configureRegister(memoryArea3 = new JTextArea(), "F", memoryPanel);
        configureRegister(memoryArea4 = new JTextArea(), "M", memoryPanel);

        add(editorScroll,  BorderLayout.CENTER);
        add(memoryPanel,   BorderLayout.EAST);
    }

    private void configureRegister(JTextArea area, String title, JPanel parent) {
        area.setEditable(false);
        area.setBackground(Color.BLACK);
        area.setForeground(new Color(51, 255, 51));
        area.setFont(new Font("Monospaced", Font.BOLD, 16));
        area.setLineWrap(false);
        area.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 0), 1),
            title,
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Monospaced", Font.BOLD, 14),
            new Color(0, 200, 100)
        ));
        parent.add(area);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public JTextArea getTextArea()    { return textArea;    }
    public JTextArea getMemoryArea1() { return memoryArea1; }
    public JTextArea getMemoryArea2() { return memoryArea2; }
    public JTextArea getMemoryArea3() { return memoryArea3; }
    public JTextArea getMemoryArea4() { return memoryArea4; }

    // ── Setters (invoked from GameController on EDT) ──────────────────────────

    public void setMemoryArea1Text(String text) {
        SwingUtilities.invokeLater(() -> { memoryArea1.setText(text); repaint(); });
    }
    public void setMemoryArea2Text(String text) { memoryArea2.setText(text); }
    public void setMemoryArea3Text(String text) { memoryArea3.setText(text); }
    public void setMemoryArea4Text(String text) { memoryArea4.setText(text); }

    // ── Line highlight (current instruction) ─────────────────────────────────

    public void highlightLine(int lineIndex) {
        SwingUtilities.invokeLater(() -> {
            try {
                textArea.getHighlighter().removeAllHighlights();
                if (lineIndex < 0) return;
                String text = textArea.getText();
                if (text.isEmpty()) return;
                String[] lines = text.split("\n", -1);
                if (lineIndex >= lines.length) return;
                int start = 0;
                for (int i = 0; i < lineIndex; i++) start += lines[i].length() + 1;
                int end = Math.min(start + lines[lineIndex].length(), text.length());
                textArea.getHighlighter().addHighlight(start, end,
                    new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(
                        new Color(0, 100, 0, 180)));
                textArea.setCaretPosition(start);
            } catch (Exception e) { /* ignore */ }
        });
    }

    // ── Reset ─────────────────────────────────────────────────────────────────

    void reinitialiserJeu() {
        SwingUtilities.invokeLater(() -> {
            textArea.getHighlighter().removeAllHighlights();
            memoryArea1.setText("");
            memoryArea2.setText("");
            memoryArea3.setText("");
            memoryArea4.setText("");
        });
    }
}
