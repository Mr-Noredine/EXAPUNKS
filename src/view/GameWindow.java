package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JPanel{
   
    JFrame frame;
    JFrame frame2;
    JButton jouerButton;
    JButton parametreButton;
    JButton quitterButton;
    TextZone textZone;
    TextZone textZone2;
    GamePanel gamePanel;
    Controller controller;
    Niveau niveau;
    JButton avancerButton;
    JButton stopButton;

    public GameWindow() { 
        fenetre_avant_jeu();
        
    }



   
    public void fenetre_avant_jeu() {
        System.out.println("affichage fenetre avant jeu");
         SwingUtilities.invokeLater(() -> {
            frame = new JFrame("EXAPUNKS - System Boot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1265, 665);
            frame.getContentPane().setBackground(Color.BLACK);

            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("src/assets/images/im.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create a layered pane to manage z-index
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(1265, 665));

            if (image != null) {
                JLabel backgroundLabel = new JLabel(new ImageIcon(image));
                backgroundLabel.setBounds(0, 0, 1265, 665);
                layeredPane.add(backgroundLabel, Integer.valueOf(1));
            }

            // Create a transparent panel for buttons
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(15, 15, 15, 15);

            // Add the "Jouer" button
            jouerButton = createButton("INITIALIZE MISSION", Color.BLACK, e -> {
                afficherOptionsNiveau();
                frame.dispose();
            });
            constraints.gridx = 0;
            constraints.gridy = 0;
            buttonPanel.add(jouerButton, constraints);

            // Add the "Quitter" button
            quitterButton = createButton("TERMINATE SESSION", Color.BLACK, e -> {
                JOptionPane.showMessageDialog(null, "Session Terminated.");
                System.exit(0);
            });
            constraints.gridy = 1;
            buttonPanel.add(quitterButton, constraints);

            // Position des bouttons dans les fentres
            buttonPanel.setBounds(432, 200, 400, 400); 
            layeredPane.add(buttonPanel, Integer.valueOf(2));

            // Set the layered pane as the content pane
            frame.setContentPane(layeredPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    
    private static JButton createButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(40, 40, 45));
        button.setForeground(new Color(51, 255, 51));
        button.setFocusPainted(false);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet de survol simple (optionnel, via un MouseListener si on voulait aller plus loin)
        button.addActionListener(actionListener);
        return button;
    }

  
    public void afficherFenetreJeu(Niveau niveau) {
        frame2 = new JFrame("EXAPUNKS Clone - Console");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(1265, 750);
        frame2.setLayout(new BorderLayout());
        frame2.getContentPane().setBackground(Color.BLACK);
    
        // Création d'un JLayeredPane pour la gestion des superpositions
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1265, 535)); 
    
        // Configuration du grillePanel avec la grille de jeu
        JPanel grillePanel = new JPanel();
        grillePanel.setOpaque(false);
        gamePanel = new GamePanel(niveau);
        grillePanel.add(gamePanel);
        grillePanel.setBounds(0, 50, 1000, 500); 
        layeredPane.add(grillePanel, JLayeredPane.DEFAULT_LAYER); 
    
        // Ajout de l'image de fond (on peut la garder ou la remplacer par un fond uni)
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setOpaque(true);
        backgroundLabel.setBackground(new Color(15, 15, 20));
        backgroundLabel.setBounds(0, 0, 1000, 600);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); 
    
        // Configuration du JScrollPane pour les règles du jeu
        JTextArea rulesTextArea = new JTextArea(" MISSION OBJECTIVE:\n ------------------\n " + niveau.getMission());
        rulesTextArea.setEditable(false);
        rulesTextArea.setBackground(new Color(5, 5, 10));
        rulesTextArea.setForeground(new Color(0, 200, 255));
        rulesTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rulesTextArea.setBorder(BorderFactory.createLineBorder(new Color(0, 50, 100)));
        
        JScrollPane rulesScrollPane = new JScrollPane(rulesTextArea);
        rulesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rulesScrollPane.setPreferredSize(new Dimension(frame2.getWidth(), 100));
        rulesScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rulesScrollPane.setBackground(Color.BLACK);
    
        // Panneau central incluant le layeredPane
        JPanel panneaucentral = new JPanel(new BorderLayout());
        panneaucentral.setOpaque(false);
        panneaucentral.add(layeredPane, BorderLayout.CENTER);
        panneaucentral.add(rulesScrollPane, BorderLayout.SOUTH);
    
        // Configuration du panel pour les textes et boutons
        JPanel textAndButtonsPanel = setupTextAndButtonsPanel(niveau);
        textAndButtonsPanel.setBackground(Color.BLACK);
        textAndButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Assemblage finale dans le JFrame
        frame2.add(textAndButtonsPanel, BorderLayout.LINE_START);
        frame2.add(panneaucentral, BorderLayout.CENTER);
        frame2.setVisible(true);
        
        controller = new Controller(this, niveau);
        controller.startGameThread();
    }
    
    private JPanel setupTextAndButtonsPanel(Niveau niveau) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        textZone = new TextZone();
        container.add(textZone, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        
        JButton stopButton = new JButton("RESET");
        JButton avancerButton = new JButton("STEP >>");
        
        styleActionButton(stopButton, new Color(150, 0, 0));
        styleActionButton(avancerButton, new Color(0, 100, 200));
        
        buttonPanel.add(stopButton);
        buttonPanel.add(avancerButton);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        stopButton.addActionListener(e -> textZone.reinitialiserJeu());
        avancerButton.addActionListener(e -> SharedSemaphore.release());
        
        if (niveau instanceof Niveau3) {
            textZone2 = new TextZone();
            JPanel textZonesPanel = new JPanel();
            textZonesPanel.setOpaque(false);
            textZonesPanel.setLayout(new BoxLayout(textZonesPanel, BoxLayout.PAGE_AXIS));
            textZonesPanel.add(textZone);
            textZonesPanel.add(textZone2);
            container.add(textZonesPanel, BorderLayout.CENTER);
        }
        
        return container;
    }

    private void styleActionButton(JButton button, Color accentColor) {
        button.setPreferredSize(new Dimension(110, 40));
        button.setBackground(Color.BLACK);
        button.setForeground(accentColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Monospaced", Font.BOLD, 12));
        button.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public TextZone getTextZone() {
        return textZone;
    }

    public TextZone getTextZone2() {
        return textZone2;
    }

   

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gamePanel.paintComponent(g);
      
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
   


    public void afficherOptionsNiveau() {
        JFrame niveauFrame = new JFrame("Choix du Niveau");
        niveauFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        niveauFrame.setSize(1265, 665);
    
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/assets/images/im.png")); // Utilise le même chemin d'image que `fenetre_avant_jeu`
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Création d'un panneau en couches pour gérer l'indice Z
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    
        JLabel backgroundLabel = new JLabel(new ImageIcon(image));
        backgroundLabel.setBounds(0, 0, image.getWidth(), image.getHeight());
        layeredPane.add(backgroundLabel, Integer.valueOf(1));
    
        // Créer un panneau transparent pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0)); // Espace entre les boutons et autour
    
        // Ajout des boutons de niveau avec personnalisation
        JButton niveau1Button = new JButton("Niveau 1");
        JButton niveau2Button = new JButton("Niveau 2");
        JButton niveau3Button = new JButton("Niveau 3");
        
        // Personnalisation des boutons
        niveau1Button.setPreferredSize(new Dimension(200, 50));
        niveau2Button.setPreferredSize(new Dimension(200, 50));
        niveau3Button.setPreferredSize(new Dimension(200, 50));
        
        niveau1Button.setBackground(Color.LIGHT_GRAY);
        niveau2Button.setBackground(Color.LIGHT_GRAY);
        niveau3Button.setBackground(Color.LIGHT_GRAY);
    
        niveau1Button.addActionListener(e -> {
            niveau = new Niveau1();
            afficherFenetreJeu(niveau);
            niveauFrame.dispose();
        });
    
        niveau2Button.addActionListener(e -> {
            niveau = new Niveau2();
            afficherFenetreJeu(niveau);
            niveauFrame.dispose();
        });
    
        niveau3Button.addActionListener(e -> {
            niveau = new Niveau3();
            afficherFenetreJeu(niveau);
            niveauFrame.dispose();
        });
    
        buttonPanel.add(niveau1Button);
        buttonPanel.add(niveau2Button);
        buttonPanel.add(niveau3Button);
    
        // Placer le panneau de boutons en bas
        buttonPanel.setBounds(0, image.getHeight() - 100, image.getWidth(), 100);
        layeredPane.add(buttonPanel, Integer.valueOf(2));
    
        // Définir le panneau en couches comme le contenu du panneau
        niveauFrame.setContentPane(layeredPane);
        niveauFrame.setVisible(true);
    }
  
    public JButton getAvancerButton() {
        return avancerButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void setAvancerButton(JButton avancerButton) {
        this.avancerButton = avancerButton;
    }

    public void setStopButton(JButton stopButton) {
        this.stopButton = stopButton;
    }
    
   
    
}
