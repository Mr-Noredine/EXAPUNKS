package model;

import java.util.List;

public class Niveau7 extends Niveau {

    private int nbNiveau;
    private int nbRobot;

    public Niveau7() {
        super();
        this.nbRobot = 2;
        this.nbNiveau = 7;

        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();

        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }

    @Override
    public String getDescription() {
        return "Deux robots coordonnes via M : Robot1 lit F200 (11), Robot2 lit F201 (7), somme = 18.";
    }

    @Override
    public String getMission() {
        return "-> 2 robots | Robot1 lit F200, envoie via M | Robot2 lit F201, recoit M, somme = 18";
    }

    @Override
    public boolean testVictoire() {
        Fichier f201 = grille.getFichierParId(201);
        if (f201 == null || f201.getPosX() != 2 || f201.getPosY() != 4) {
            return false;
        }

        List<String> content = f201.getGestionFichier().getContenuCommeListe();
        boolean found = false;
        for (String s : content) {
            if (s.equals("18")) { found = true; break; }
        }
        if (!found) return false;

        for (Robot r : grille.getListeRobots()) {
            if (r.estActif()) return false;
        }
        return true;
    }

        public void ajouterSolution() {
        // no predefined solution
    }

        public void InitialiserFichierLancement() {
        Fichier fichier200 = new Fichier(200, 4, 0);
        fichier200.ajouterContenu("11");
        fichiersLancement.add(fichier200);

        Fichier fichier201 = new Fichier(201, 4, 4);
        fichier201.ajouterContenu("7");
        fichiersLancement.add(fichier201);
    }

        public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));
        robotsLancement.add(new Robot(2, 0, 4));
    }

    @Override
    public int getTargetX() { return 2; }

    @Override
    public int getTargetY() { return 4; }

    @Override
    public int getNbSolution() { return nbSolutions; }

    @Override
    public int getNbNiveau() { return nbNiveau; }

    @Override
    public int getNbRobot() { return nbRobot; }
}
