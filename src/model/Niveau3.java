package model;

import java.util.ArrayList;

public class Niveau3 extends Niveau {


    private int nbNiveau;
    private int  nbRobot;
    private int nbSolutions;

    public Niveau3() {
        super();
        this.nbSolutions = 0;
        this.nbRobot = 2;
        this.nbNiveau = 3;
        
        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();
        
        grille = new Grille(robotsLancement, fichiersLancement);
        for (Fichier fichier : fichiersLancement) {
            if (fichier.getId() == 199) {
                fichier.afficherContenu();
                break; 
            }
        }
    }



    public void ajouterSolution() {
       
       
    }

    public String getDescription() {
        String description = " File 199 contains exactly two values: a Keyword and a number. "
        + "Create a new file in the position (3, 4) and copy those two values to it, swapping their order" +
        " so that the nubmer is first. \n When you are finished, delete file 199.";

        return description;
    }

    public String getMission() {
        String mission = " -> Create the specified file in the area (3, 4).\n";
        mission += "-> Delete file 199.";
        mission += "-> Leave no trace";

        return mission;
    }

    public boolean testVectoire() {
        // tester si le dernier element de fichier est egale a 436 
        
        if (!grille.contientFichier(3, 4)) {
            return false;
        }
        
        if (grille.getFichierParId(199) != null) {
            return false;
        }
        
        if (grille.getListeRobots().size() != 0) {
            return false;
        }
        Fichier file = grille.getFichier(3, 4);
       // ArrayList<String> list = file.contenuList();
       // if ((list.get(1) != "ECHO") && (Integer.valueOf((list.get(0))) != 9780)){
         //   return false;
       // }
        return true;
    }

    public void InitialiserFichierLancement() {
      
        Fichier fichier199 = new Fichier(199, 0, 4);
    
        String[] contenuFichier199 = {"ECHO", "9780"};
        for (String e : contenuFichier199) {
            fichier199.ajouterContenu(e);
        }

        fichiersLancement.add(fichier199);
      
    }


    public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));  
        robotsLancement.add(new Robot(2, 0, 1));  
    }


    @Override
    public int getNbSolution() {
        return nbSolutions;
    }

    @Override
    public int getNbNiveau() {
        return nbNiveau;
    }

    @Override
    public int getNbRobot() {
        return nbRobot;
    }

    public void setNbSolution(int nb) {
        nbSolutions = nb;
    }
    
}