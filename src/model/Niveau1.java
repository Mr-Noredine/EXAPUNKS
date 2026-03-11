package model;

import java.util.List;

public class Niveau1 extends Niveau {

    private int nbNiveau;
    private int nbRobot;

    public Niveau1() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 1;

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
        return "Additionner les deux valeurs de F200 et deposer le resultat en OUTBOX.";
    }

    @Override
    public String getMission() {
        return "-> Lire F200 | Calculer 10 + 25 | Deposer resultat en OUTBOX (2,4)";
    }

    @Override
    public boolean testVictoire() {
        Fichier f200 = grille.getFichierParId(200);
        if (f200 == null || f200.getPosX() != 2 || f200.getPosY() != 4) {
            return false;
        }

        List<String> content = f200.getGestionFichier().getContenuCommeListe();
        boolean found = false;
        for (String s : content) {
            if (s.equals("35")) { found = true; break; }
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
        fichier200.ajouterContenu("10");
        fichier200.ajouterContenu("25");
        fichiersLancement.add(fichier200);
    }

        public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));
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
