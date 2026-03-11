package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class TextZone extends JPanel {
    private JTextArea textArea;
    public JTextArea memoryArea1;
    private JTextArea memoryArea2;
    private JTextArea memoryArea3;
    private JTextArea memoryArea4;
   


    public TextZone() {
        // Utilise BoxLayout pour aligner verticalement les composants
       
        
        // Panel contenant la zone de texte et les zones mémoire
        JPanel textAndMemoryPanel = new JPanel();
        textAndMemoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        // Zone de texte principale
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(220, 250)); // Taille spécifique pour la zone de texte
        textAndMemoryPanel.add(scrollPane); // Ajoute la zone de texte au panneau
        textArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        // Panneau pour les zones de mémoire
        JPanel memoryPanel = new JPanel(new GridLayout(4, 1, 5, 5)); // Avec un espacement
        memoryPanel.setPreferredSize(new Dimension(60, 250)); // Taille spécifique pour réduire la hauteur
        configureMemoryArea(memoryArea1 = new JTextArea(), "X", memoryPanel);
        configureMemoryArea(memoryArea2 = new JTextArea(), "T", memoryPanel);
        configureMemoryArea(memoryArea3 = new JTextArea(), "F", memoryPanel);
        configureMemoryArea(memoryArea4 = new JTextArea(), "M", memoryPanel);
        
        textAndMemoryPanel.add(memoryPanel); // Ajoute le panneau de mémoire au côté de la zone de texte

        add(textAndMemoryPanel); // Ajoute le panneau combiné au TextZone
       // add(Box.createVerticalStrut(300));
        
        // Configuration des boutons
        

        // Styles
        configureStyles();
    }

    private void configureMemoryArea(JTextArea memoryArea, String title, JPanel memoryPanel) {
        memoryArea.setEditable(false);
        memoryArea.setBackground(Color.BLACK);
        memoryArea.setForeground(new Color(51, 255, 51));
        memoryArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        memoryArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 0)), 
            title, 
            0, 0, 
            new Font("Monospaced", Font.PLAIN, 10), 
            new Color(0, 153, 0)
        ));
       
        JScrollPane scrollPane = new JScrollPane(memoryArea);
        scrollPane.setBorder(null);
        memoryPanel.add(scrollPane);
    }

    private void configureStyles() {
        textArea.setBackground(new Color(10, 10, 10));
        textArea.setForeground(new Color(51, 255, 51));
        textArea.setCaretColor(Color.GREEN);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(5, 5, 5, 5));
        
        this.setBackground(Color.BLACK);
    }
    // Getters et Setters

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextArea getMemoryArea1() {
        return memoryArea1;
    }
    
    public JTextArea getMemoryArea2() {
        return memoryArea2;
    }
    public JTextArea getMemoryArea3() {
        return memoryArea3;
    }
    public JTextArea getMemoryArea4() {
        return memoryArea4;
    }
    public void setMemoryArea1Text(String text) {
        SwingUtilities.invokeLater(() -> {
            memoryArea1.setText(text);
        });
        repaint();
    }
    
    public void setMemoryArea2Text(String text) {
        memoryArea2.setText(text);
    }
    
    public void setMemoryArea3Text(String text) {
        memoryArea3.setText(text);
    }
    
    public void setMemoryArea4Text(String text) {
        memoryArea4.setText(text);
    }

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
                for (int i = 0; i < lineIndex; i++) {
                    start += lines[i].length() + 1;
                }

                int end = start + lines[lineIndex].length();
                if (end > text.length()) end = text.length();

                textArea.getHighlighter().addHighlight(start, end, 
                    new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(0, 100, 0, 180)));
                
                textArea.setCaretPosition(start);
            } catch (Exception e) {
                // Ignore
            }
        });
    }

    void reinitialiserJeu() {
        // Exemple de réinitialisation, ajustez selon les besoins spécifiques de votre jeu
        SwingUtilities.invokeLater(() -> {
            textArea.setText(""); // Réinitialiser la zone de texte principale
            textArea.getHighlighter().removeAllHighlights();
            memoryArea1.setText(""); // Réinitialiser les zones de mémoire
            memoryArea2.setText("");
            memoryArea3.setText("");
            memoryArea4.setText("");
            // Si d'autres composants ou états doivent être réinitialisés, ajoutez les commandes ici
        });
    }
}