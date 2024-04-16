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
        memoryArea.setBorder(BorderFactory.createTitledBorder(title));
       
        JScrollPane scrollPane = new JScrollPane(memoryArea);
        memoryPanel.add(scrollPane);
    }

    private void configureStyles() {
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
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
    void reinitialiserJeu() {
        // Exemple de réinitialisation, ajustez selon les besoins spécifiques de votre jeu
        SwingUtilities.invokeLater(() -> {
            textArea.setText(""); // Réinitialiser la zone de texte principale
            memoryArea1.setText(""); // Réinitialiser les zones de mémoire
            memoryArea2.setText("");
            memoryArea3.setText("");
            memoryArea4.setText("");
            // Si d'autres composants ou états doivent être réinitialisés, ajoutez les commandes ici
        });
    }
}