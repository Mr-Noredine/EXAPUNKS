package model;


import java.util.List;

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
        
        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.PORTE2, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.PORTE3, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.PORTE5, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.PORTE4}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }

    public String getDescription() {
        return "Collect values from 3 hosts and sum them into a file in the OUTBOX.";
    }

    public String getMission() {
        return "-> Collect 10, 20, 30 from files\n-> Sum them and write 60 to a file\n-> Move result to OUTBOX";
    }

    public boolean testVictoire() {
        Fichier outbox = grille.getFichier(3, 4);
        if (outbox == null) return false;
        
        List<String> content = outbox.getGestionFichier().getContenuCommeListe();
        // Check if sum 60 is present
        boolean found60 = false;
        for (String s : content) {
            if (s.equals("60")) {
                found60 = true;
                break;
            }
        }
        if (!found60) return false;

        for (Robot r : grille.getListeRobots()) {
            if (r.estActif()) return false;
        }

        return true;
    }

    public void ajouterSolution() {
    
    }


    public void InitialiserFichierLancement() {
        Fichier f1 = new Fichier(200, 4, 0);
        f1.ajouterContenu("10");
        Fichier f2 = new Fichier(201, 0, 4);
        f2.ajouterContenu("20");
        Fichier f3 = new Fichier(202, 4, 4);
        f3.ajouterContenu("30");
        
        fichiersLancement.add(f1);
        fichiersLancement.add(f2);
        fichiersLancement.add(f3);
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