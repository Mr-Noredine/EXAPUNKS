package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.*;
import model.*;

public class GamePanel extends JPanel{
private BufferedImage robotImage;
private BufferedImage fileImage;
private BufferedImage CaseOcup;
private BufferedImage robotHoldingFileImage;
private BufferedImage porte;
private Niveau niveau;



final int originalTileize = 22;
final int scale = 3;

final int tileSize = originalTileize * scale;   // taille de la celleule
final int maxGrilleCol = 5;
final int maxGrilleRow = 5;
final int panelWidth = tileSize * maxGrilleCol;
final int panelHeight = tileSize * maxGrilleRow;

public GamePanel(Niveau niveau) {

    this.setPreferredSize(new Dimension(panelWidth, panelHeight));
    this.setBackground(new Color(20, 20, 25));
    this.setDoubleBuffered(true);
    this.niveau = niveau;

    try {
        robotImage = ImageIO.read(new File("src/assets/images/robot.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        porte = ImageIO.read(new File("src/assets/images/porte.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    try {
        CaseOcup = ImageIO.read(new File("src/assets/images/interdit.png")); 
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        fileImage = ImageIO.read(new File("src/assets/images/fichier.png")); 
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        robotHoldingFileImage = ImageIO.read(new File("src/assets/images/robottiensfichier.png")); 
    } catch (IOException e) {
        e.printStackTrace();
    }
}

protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

    // Dessiner le fond de la grille avec un léger dégradé ou couleur sombre uniforme
    g2d.setColor(new Color(30, 30, 40)); 
    g2d.fillRect(0, 0, panelWidth, panelHeight);

    // Dessiner les cases de la grille
    for (int row = 0; row < maxGrilleRow; row++) {
        for (int col = 0; col < maxGrilleCol; col++) {
            int x = col * tileSize;
            int y = row * tileSize;
            
            // Case de fond
            g2d.setColor(new Color(40, 40, 50));
            g2d.fillRect(x + 1, y + 1, tileSize - 2, tileSize - 2);

            // Contour subtil
            g2d.setColor(new Color(60, 60, 80));
            g2d.drawRect(x, y, tileSize, tileSize); 
            
            TypeTerritoire tt = niveau.getGrille().getTerritoire(col, row);
            if (tt != TypeTerritoire.LIBRE && tt != TypeTerritoire.OCCUPE) {
                drawPortal(g2d, x, y, getPortalColor(tt));
            }
            if (niveau.getGrille().estZoneOccupee(col, row)) {
                // Amélioration de l'icône interdite (plus centrée)
                int margin = tileSize / 4;
                g.drawImage(CaseOcup, x + margin, y + margin, tileSize/2, tileSize/2, this);
            }
            
            // Draw Target Zone (Outbox)
            if (col == niveau.getTargetX() && row == niveau.getTargetY()) {
                drawTargetZone(g2d, x, y);
            }
        }
    }
    
    // Ajout d'une bordure lumineuse pour toute la grille
    g2d.setColor(new Color(0, 255, 255, 100));
    g2d.drawRect(0, 0, panelWidth - 1, panelHeight - 1);

    for (Robot robot : niveau.getGrille().getListeRobots()) {
        int x = robot.getPositionX();
        int y = robot.getPositionY();
        // Vérifiez si le robot tient un fichier
        if (robot.getFichierEnMain() != null) {
            g.drawImage(robotHoldingFileImage,  x * tileSize, y * tileSize  , tileSize, tileSize, this);
        } else {
            // Dessinez l'image standard du robot, mieux centrée
            g.drawImage(robotImage,  x * tileSize + tileSize/6, y * tileSize + tileSize/6 , (int)(tileSize * 0.7), (int)(tileSize * 0.7), this);
        }
    }
    

    for (Fichier fichier : niveau.getGrille().getListFichiers()) {
        int x = fichier.getPosX();
        int y = fichier.getPosY();
        if (!fichier.estTenuParRobot()){
            // Fichier plus visible
            g.drawImage(fileImage, x * tileSize + tileSize/4, y * tileSize + tileSize/4, tileSize/2, tileSize/2, this);
        }
    }
}
public void rafraichirAffichage() {
    // Force le composant à se redessiner
    this.repaint();
}

private void drawTargetZone(Graphics2D g2d, int x, int y) {
    Color targetColor = new Color(0, 255, 100);
    // Semi-transparent fill
    g2d.setColor(new Color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 30));
    g2d.fillRect(x + 2, y + 2, tileSize - 4, tileSize - 4);
    
    // Dashed border
    float[] dash = {5f, 5f};
    g2d.setStroke(new java.awt.BasicStroke(2, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
    g2d.setColor(targetColor);
    g2d.drawRect(x + 4, y + 4, tileSize - 8, tileSize - 8);
    
    // "OUT" Text
    g2d.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 12));
    java.awt.FontMetrics fm = g2d.getFontMetrics();
    String txt = "OUTBOX";
    int tx = x + (tileSize - fm.stringWidth(txt)) / 2;
    int ty = y + (tileSize + fm.getAscent()) / 2 - 5;
    
    // Shadow for text
    g2d.setColor(Color.BLACK);
    g2d.drawString(txt, tx + 1, ty + 1);
    g2d.setColor(targetColor);
    g2d.drawString(txt, tx, ty);
    
    g2d.setStroke(new java.awt.BasicStroke(1));
}

private Color getPortalColor(TypeTerritoire tt) {
    return switch (tt) {
        case PORTE2, PORTE5 -> new Color(0, 255, 255); // Cyan pair
        case PORTE3, PORTE4 -> new Color(255, 0, 255); // Magenta pair
        default -> Color.WHITE;
    };
}

private void drawPortal(Graphics2D g2d, int x, int y, Color color) {
    int margin = 8;
    int size = tileSize - (margin * 2);
    
    // Portal background glow
    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
    g2d.fillOval(x + margin - 2, y + margin - 2, size + 4, size + 4);
    
    // Main portal ring
    g2d.setStroke(new java.awt.BasicStroke(3, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
    g2d.setColor(color);
    g2d.drawOval(x + margin, y + margin, size, size);
    
    // Inner pulsing effect (simulated)
    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
    g2d.drawOval(x + margin + 6, y + margin + 6, size - 12, size - 12);
    
    // Center glow
    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
    g2d.fillOval(x + margin + 10, y + margin + 10, size - 20, size - 20);
    
    // Reset stroke
    g2d.setStroke(new java.awt.BasicStroke(1));
}

}