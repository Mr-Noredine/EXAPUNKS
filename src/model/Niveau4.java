package model;

import java.util.List;

public class Niveau4 extends Niveau {

    private int nbNiveau;
    private int nbRobot;

    public Niveau4() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 4;

        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();

        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }

    @Override
    public String getDescription() {
        return "Produit de deux fichiers : lire F200 (5) et F201 (9), calculer 5 x 9 = 45.";
    }

    @Override
    public String getMission() {
        return "-> Lire F200 (5) et F201 (9) | Calculer 5 x 9 | Deposer resultat en (0,4)";
    }

    @Override
    public boolean testVictoire() {
        Fichier f201 = grille.getFichierParId(201);
        if (f201 == null || f201.getPosX() != 0 || f201.getPosY() != 4) {
            return false;
        }

        List<String> content = f201.getGestionFichier().getContenuCommeListe();
        boolean found = false;
        for (String s : content) {
            if (s.equals("45")) { found = true; break; }
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
        fichier200.ajouterContenu("5");
        fichiersLancement.add(fichier200);

        Fichier fichier201 = new Fichier(201, 4, 4);
        fichier201.ajouterContenu("9");
        fichiersLancement.add(fichier201);
    }

        public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));
    }

    @Override
    public int getTargetX() { return 0; }

    @Override
    public int getTargetY() { return 4; }

    @Override
    public int getNbSolution() { return nbSolutions; }

    @Override
    public int getNbNiveau() { return nbNiveau; }

    @Override
    public int getNbRobot() { return nbRobot; }
}
