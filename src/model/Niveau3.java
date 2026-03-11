package model;

import java.util.ArrayList;

import java.util.List;

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
        
        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.PORTE2, TypeTerritoire.OCCUPE, TypeTerritoire.PORTE3, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.PORTE5, TypeTerritoire.OCCUPE, TypeTerritoire.PORTE4, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }



    public void ajouterSolution() {
       
       
    }

    public String getDescription() {
        return "Filter values > 100 from file 199 and send them to OUTBOX.";
    }

    public String getMission() {
        return "-> Read file 199 (at 0,4)\n-> Filter values > 100\n-> Write them to a new file in OUTBOX";
    }

    public boolean testVictoire() {
        Fichier outbox = grille.getFichier(3, 4);
        if (outbox == null) return false;
        
        List<String> content = outbox.getGestionFichier().getContenuCommeListe();
        // Values > 100 from {50, 120, 80, 200} should be {120, 200}
        if (content.size() < 2 || !content.get(0).equals("120") || !content.get(1).equals("200")) {
            return false;
        }
        
        if (grille.getFichierParId(199) != null) {
            return false;
        }
        
        for (Robot r : grille.getListeRobots()) {
            if (r.estActif()) return false;
        }
        return true;
    }

    public void InitialiserFichierLancement() {
      
        Fichier fichier199 = new Fichier(199, 0, 4);
    
        String[] contenuFichier199 = {"50", "120", "80", "200"};
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