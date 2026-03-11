package model;

import java.util.List;

public class Niveau1 extends Niveau {

    private int nbNiveau;
    private int  nbRobot;

    public Niveau1() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 1;
        
        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();
        
        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
        for (Fichier fichier : fichiersLancement) {
            if (fichier.getId() == 200) {
                fichier.afficherContenu();
                break; 
            }
        }
    }

    public String getDescription() {
        String description = " Move file 200 to the OUTBOX area. \n";
        description += "This task requires you to leave no trace," +
        "and not make any changes to the network other than those specified !";

        return description;
    }

    public String getMission() {
        String mission = " -> Move file 200 to the OUTBOX \n";
        mission += "Leave no trace";

        return mission;
    }

    public boolean testVictoire() {
        Fichier f200 = grille.getFichierParId(200);
        if (f200 == null || f200.getPosX() != 3 || f200.getPosY() != 4) {
            return false;
        }
        
        List<String> content = f200.getGestionFichier().getContenuCommeListe();
        if (content.size() < 2 || !content.get(0).equals("50") || !content.get(1).equals("60")) {
            return false;
        }

        for (Robot r : grille.getListeRobots()) {
            if (r.estActif()) return false;
        }

        return true;
    }

    public void ajouterSolution() {
        

    }


    public void InitialiserFichierLancement() {

        Fichier fichier200 = new Fichier(200, 4, 0);
        fichier200.ajouterContenu("50");
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

}