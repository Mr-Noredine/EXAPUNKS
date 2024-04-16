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
    this.setBackground(Color.DARK_GRAY);
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
    g2d.setColor(Color.WHITE); // Définir la couleur des cases de la grille

    for (int row = 0; row < maxGrilleRow; row++) {
        for (int col = 0; col < maxGrilleCol; col++) {
            int x = col * tileSize;
            int y = row * tileSize;
            g2d.fillRect(x, y, tileSize, tileSize); // Remplir la case avec la couleur rouge
        }
    }

    g2d.setColor(Color.BLACK); // Définir la couleur du contour
    for (int row = 0; row < maxGrilleRow; row++) {
        for (int col = 0; col < maxGrilleCol; col++) {
            int x = col * tileSize;
            int y = row * tileSize;
            g2d.drawRect(x, y, tileSize, tileSize); // Dessiner le contour de chaque case
            if (niveau.getGrille().estPorteNumerotee(col, row)) {
                g.drawImage(porte, x, y, tileSize, tileSize, this);
            }
            if (niveau.getGrille().estZoneOccupee(col, row)) {
                g.drawImage(CaseOcup, x, y, tileSize*2/3, tileSize*2/3, this);
            }
        }
    }
    for (Robot robot : niveau.getGrille().getListeRobots()) {
        int x = robot.getPositionX();
        int y = robot.getPositionY();
        // Vérifiez si le robot tient un fichier
        if (robot.getFichierEnMain() != null) {
            g.drawImage(robotHoldingFileImage,  x * tileSize, y * tileSize  , tileSize, tileSize, this);
        } else {
            // Dessinez l'image standard du robot
            g.drawImage(robotImage,  x * tileSize, y * tileSize + tileSize/4 , tileSize/2, tileSize/2, this);
        }
    }
    

    for (Fichier fichier : niveau.getGrille().getListFichiers()) {
        int x = fichier.getPosX();
        int y = fichier.getPosY();
        if (!fichier.estTenuParRobot()){
        g.drawImage(fileImage, x * tileSize + tileSize/2, y * tileSize + tileSize/4, tileSize/2, tileSize/2, this);
    }
}
}
public void rafraichirAffichage() {
    // Force le composant à se redessiner
    this.repaint();
}


}