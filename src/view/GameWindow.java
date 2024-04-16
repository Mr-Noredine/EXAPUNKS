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
            frame = new JFrame("Avant le jeu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1265, 665);

            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("src/assets/images/im.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create a layered pane to manage z-index
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(800, 600));

            JLabel backgroundLabel = new JLabel(new ImageIcon(image));
            backgroundLabel.setBounds(0, 0, image.getWidth(), image.getHeight());
            layeredPane.add(backgroundLabel, Integer.valueOf(1));

            // Create a transparent panel for buttons
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10);

            // Add the "Jouer" button
            jouerButton = createButton("Jouer", Color.LIGHT_GRAY, e -> afficherOptionsNiveau());
            constraints.gridx = 0;
            constraints.gridy = 0;
            buttonPanel.add(jouerButton, constraints);

            // Add the "Paramètre" button
            parametreButton = createButton("Paramètre", Color.LIGHT_GRAY, e -> {
                // Your parameter settings action here
            });
            constraints.gridy = 1;
            buttonPanel.add(parametreButton, constraints);

            // Add the "Quitter" button
            quitterButton = createButton("Quitter", Color.LIGHT_GRAY, e -> {
                JOptionPane.showMessageDialog(null, "Au revoir !");
                System.exit(0);
            });
            constraints.gridy = 2;
            buttonPanel.add(quitterButton, constraints);

            // Position des bouttons dans les fentres
            buttonPanel.setBounds(100, 100, 400, 400); // Set this as needed
            layeredPane.add(buttonPanel, Integer.valueOf(2));

            // Set the layered pane as the content pane
            frame.setContentPane(layeredPane);
            frame.setVisible(true);
        });
    }

    
    private static JButton createButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(color);
        button.addActionListener(actionListener);
        return button;
    }

  
    public void afficherFenetreJeu(Niveau niveau) {
        frame2 = new JFrame("Jeu");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(1265, 665);
        frame2.setLayout(new BorderLayout());
    
        // Création d'un JLayeredPane pour la gestion des superpositions
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1265, 535)); 
    
        // Configuration du grillePanel avec la grille de jeu
        JPanel grillePanel = new JPanel();
        grillePanel.setBackground(new Color(0,0,0,0)); // Fond transparent pour voir l'image derrière
        gamePanel = new GamePanel(niveau);
        grillePanel.add(gamePanel, BorderLayout.CENTER);
        grillePanel.setBounds(-120, 70, 1265, 535); // Coordonnées et taille du panel de jeu
        layeredPane.add(grillePanel, JLayeredPane.DEFAULT_LAYER); // Ajout au layer par défaut
    
        // Ajout de l'image de fond
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/assets/images/fond.png"));
        backgroundLabel.setBounds(0, 0, 1000, 535);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); // Place derrière le grillePanel
    
        // Configuration du JScrollPane pour les règles du jeu
        JTextArea rulesTextArea = new JTextArea("Ici vos règles du jeu...");
        rulesTextArea.setEditable(false);
        JScrollPane rulesScrollPane = new JScrollPane(rulesTextArea);
        rulesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rulesScrollPane.setPreferredSize(new Dimension(frame2.getWidth(), 130));
    
        // Panneau central incluant le layeredPane
        JPanel panneaucentral = new JPanel(new BorderLayout());
        panneaucentral.add(layeredPane, BorderLayout.CENTER);
        panneaucentral.add(rulesScrollPane, BorderLayout.SOUTH);
    
        // Configuration du panel pour les textes et boutons
        JPanel textAndButtonsPanel = setupTextAndButtonsPanel(niveau);
    
        // Assemblage finale dans le JFrame
        frame2.add(textAndButtonsPanel, BorderLayout.LINE_START);
        frame2.add(panneaucentral, BorderLayout.CENTER);
        frame2.setVisible(true);
        
        controller = new Controller(this, niveau);
        controller.startGameThread();
    }
    
    private JPanel setupTextAndButtonsPanel(Niveau niveau) {
        JPanel textAndButtonsPanel = new JPanel(new BorderLayout());
        textZone = new TextZone();
        textAndButtonsPanel.add(textZone, BorderLayout.PAGE_START);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JButton stopButton = new JButton("Stop");
        JButton avancerButton = new JButton("Avancer");
        buttonPanel.add(stopButton);
        buttonPanel.add(avancerButton);
        textAndButtonsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        stopButton.addActionListener(e -> textZone.reinitialiserJeu());
        avancerButton.addActionListener(e -> SharedSemaphore.release());
        
        if (niveau instanceof Niveau3) {
            textZone2 = new TextZone();
            JPanel textZonesPanel = new JPanel();
            textZonesPanel.setLayout(new BoxLayout(textZonesPanel, BoxLayout.PAGE_AXIS));
            textZonesPanel.add(textZone);
            textZonesPanel.add(textZone2);
            textAndButtonsPanel.add(textZonesPanel, BorderLayout.PAGE_START);
        }
        
        return textAndButtonsPanel;
    }
    
    public TextZone getTextZone() {
        return textZone;
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
