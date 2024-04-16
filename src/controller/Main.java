package controller;

import javax.swing.SwingUtilities;

import model.*;
import view.*;


public class Main {
 

    public static void main(String[] args) {
    
        // Création du contrôleur de jeu
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow();
        });
    }

}
  