package model;


public class Niveau2 extends Niveau {


    private int nbNiveau;
    private int  nbRobot;
    private int nbSolutions;

    public Niveau2() {
        super();
        this.nbSolutions = 0;
        this.nbRobot = 1;
        this.nbNiveau = 2;
        
        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();
        
        grille = new Grille(robotsLancement, fichiersLancement);
        for (Fichier fichier : fichiersLancement) {
            if (fichier.getId() == 200) {
                fichier.afficherContenu();
                break; 
            }
        }
    }

    public String getDescription() {
        String description = " Add the first two values of file 200, "
        + "multiply the result by the third value, and then substract the fourth value. \n" +
        " Append the result to the end of the file and then move it to the position (3, 4).";

        return description;
    }

    public String getMission() {
        String mission = " -> Append the correct value to the end of file 200.\n";
        mission += "-> Move file 200 to the area (3, 4)";
        mission += "-> Leave no trace";

        return mission;
    }

    public boolean testVectoire() {
        // tester si le dernier element de fichier est egale a 436 
        Fichier file200 = grille.getFichierParId(200);
       // if (Integer.valueOf(file200.dernierElem()) != 436) {
        //    return false;
      //  }

        if ((file200.getPosX() != 3) && (file200.getPosY() != 4)) {
            return false;
        }
        
        if (grille.getListeRobots().size() != 0) {
            return false;
        }

        return true;
    }

    public void ajouterSolution() {
    
    }


    public void InitialiserFichierLancement() {
      
        Fichier fichier200 = new Fichier(200, 4, 0);
    
        int[] entiersPourFichier200 = {72, 52, 4, 60};
        for (int entier : entiersPourFichier200) {
            fichier200.ajouterContenu(String.valueOf(entier));
        }

        fichiersLancement.add(fichier200);
    }


    public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));  
    
        
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